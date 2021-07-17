package com.raza.mealshare.HomeScreen.Fragments.Share;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.ConfirmDialog;
import com.raza.mealshare.CustomDialogs.DialogButtons;
import com.raza.mealshare.CustomDialogs.ShowCategoryPicker;
import com.raza.mealshare.CustomDialogs.ShowConditionPicker;
import com.raza.mealshare.CustomDialogs.ShowImagePickDialog;
import com.raza.mealshare.Database.AllProductsFills.MyProduct;
import com.raza.mealshare.ExtraFiles.CustomToast;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.HomeScreen.Fragments.Share.Models.SelectedImage;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.R;
import com.raza.mealshare.Services.MyUploadService;
import com.raza.mealshare.Services.UploadFilesModel;
import com.raza.mealshare.Utilities.CheckForPermissions;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.ActivityAddItemBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class EditItem extends AppCompatActivity {
private ActivityAddItemBinding binding;
private ProfileInfo profileInfo;
private int shareOrRequest=1;
private ArrayList<SelectedImage> selectedImages;
private File mTempCameraPhotoFile;
private SelectedImage clickedImage;
    private int GalleryImage =103;
    private int CameraImage=104;
    private LoadingDialog loadingDialog;
    private FirebaseRef ref=new FirebaseRef();
    private final String tag="uploadImages";
    private ArrayList<UploadFilesModel> selectedFiles=new ArrayList<>();
    private FirebaseUser firebaseUser;
    private MyProduct myProduct;
    private Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        loadingDialog=new LoadingDialog(this, R.style.DialogLoadingTheme);
        profileInfo=Utilities.UserProfile(this);
        myProduct =new Gson().fromJson(getIntent().getStringExtra("myProduct"),MyProduct.class);
        setUpButtons();
        setUpToolbar();
    }


    private void setUpToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Item");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_delete){
            ConfirmDialog.ShowConfirmDeleteItem(EditItem.this, new DialogButtons() {
                @Override
                public void OnApprove() {
                    loadingDialog.show();
                    FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).collection(ref.AllFoodShared).document(myProduct.getBaseKey()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            try {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()){
                                    new CustomToast(EditItem.this,"Delete");
                                    finish();
                                }else {
                                    new CustomToast(EditItem.this,task.getException().getMessage());
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void OnReject() {

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setUpButtons() {
        if (myProduct.getShare()){
            binding.Requested.setChecked(false);
            binding.Share.setChecked(true);
        }else {
            binding.Share.setChecked(false);
            binding.Requested.setChecked(true);
        }
        category=myProduct.getCategory();
        binding.Category.setText(category.getName());
        binding.ItemName.setText(myProduct.getItemName());
        binding.ItemDescription.setText(myProduct.getItemDescription());
        binding.ItemDescriptionLong.setText(myProduct.ItemDescriptionLong);
        binding.Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowCategoryPicker(new ShowCategoryPicker.Buttons() {
                    @Override
                    public void SelectedCategory(Category category) {
                        EditItem.this.category=category;
                        binding.Category.setText(category.getName());
                    }
                }).show(getSupportFragmentManager(),"Category");
            }
        });
        selectedImages=new ArrayList<>();
        selectedImages.add(new SelectedImage(binding.imageCardOne,binding.ItemImageOne,binding.pickImageOne));
        selectedImages.add(new SelectedImage(binding.imageCardTwo,binding.ItemImageTwo,binding.pickImageTwo));
        selectedImages.add(new SelectedImage(binding.imageCardThree,binding.ItemImageThree,binding.pickImageThree));
        for (int i=0;i<selectedImages.size();i++){
            int finalI = i;
            selectedImages.get(i).getClicker().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedImage=selectedImages.get(finalI);
                    ShowSelector();
                }
            });
        }
        if (myProduct.getAllImages()!=null){
            for (int i = 0; i< myProduct.getAllImages().size(); i++){
                selectedImages.get(i).AddImage(myProduct.getAllImages().get(i).getData_thumbnail_storage_path(),EditItem.this);
            }
        }
        binding.UploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Varified()){
                    new CustomToast(EditItem.this,"Posting...");
                    loadingDialog.show();
                    HashMap<String, Object> map=new HashMap<>();
                    map.put(ref.ItemName,binding.ItemName.getText().toString());
                    map.put(ref.ItemDescription,binding.ItemDescription.getText().toString());
                    map.put(ref.ItemDescriptionLong,binding.ItemDescriptionLong.getText().toString());
                    map.put(ref.Condition,"");
                    map.put(ref.Share,binding.ShareOrRequest.getCheckedRadioButtonId()==binding.Share.getId());
                    map.put(ref.user_location,profileInfo.getUser_location());
                    map.put(ref.Category,category);
                    HashMap<String, Object> userInfo=new HashMap<>();
                    userInfo.put(ref.user_name,profileInfo.getUser_name());
                    userInfo.put(ref.user_email,profileInfo.getUser_email());
                    userInfo.put(ref.user_profile_pic,profileInfo.getUser_profile_pic());
                    userInfo.put(ref.user_uid, firebaseUser.getUid());
                    map.put(ref.userInfo,userInfo);
                    FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).collection(ref.AllFoodShared).document(myProduct.getBaseKey()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            try {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()){
                                    new CustomToast(EditItem.this,"Added");
                                    selectedFiles=new ArrayList<>();
                                    for (int i=0;i<selectedImages.size();i++){
                                        if (selectedImages.get(i).isAdded()&&!selectedImages.get(i).Uploaded()){
                                            selectedFiles.add(new UploadFilesModel(selectedImages.get(i).getUri(), myProduct.getBaseKey()));
                                        }
                                    }
                                    if (selectedFiles.size()>0){
                                        uploadFromUri();
                                    }
                                    finish();
                                }else {
                                    new CustomToast(EditItem.this,"Failed try again later");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean Varified() {
        if (binding.ItemName.getText().toString().replace(" ","").length()<4){
            binding.ItemName.setError("Minim 4 Characters required");
            binding.ItemName.requestFocus();
            return false;
        }
        if (binding.ItemDescription.getText().toString().replace(" ","").length()<10){
            binding.ItemDescription.setError("Minim 10 Characters required");
            binding.ItemDescription.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(binding.Category.getText().toString())){
            new CustomToast(EditItem.this,"Pick  Category");
            binding.Category.callOnClick();
            return false;
        }
        for (int i=0;i<selectedImages.size();i++){
            if (selectedImages.get(i).isAdded()){
                return true;
            }
        }
        new CustomToast(EditItem.this,"Add One image");
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent();
            }
        }
    }

    private void ShowSelector() {
        ShowImagePickDialog showImagePickDialog=new ShowImagePickDialog(new ShowImagePickDialog.Buttons() {
            @Override
            public void SelectGallery() {
                setUpGalleryImages();
            }

            @Override
            public void SelectCamera() {
                CheckForPermissions.CheckForCameraPermission(EditItem.this, EditItem.this, 101, new CheckForPermissions.Results() {
                    @Override
                    public void HavePermission() {
                        openCameraIntent();
                    }

                    @Override
                    public void Requested() {

                    }
                });
            }
        },"Upload image");
        showImagePickDialog.show(EditItem.this.getSupportFragmentManager(),"camera");
    }
    private void setUpGalleryImages() {
        //Todo Gallery Images
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GalleryImage);
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            mTempCameraPhotoFile = new File(getFilesDir()+ "/" + Utilities.AudioFileName().format(Calendar.getInstance().getTime())+ ".jpg");
            Uri photoURI = FileProvider.getUriForFile(EditItem.this, "com.raza.mealshare.provider", mTempCameraPhotoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI);
            startActivityForResult(pictureIntent,
                    CameraImage);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Data","Got Data");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GalleryImage){
            Log.i("Data","Got Data3");
            try {
                if(resultCode == Activity.RESULT_OK) {
                    clickedImage.AddImage(data.getData(), EditItem.this);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (requestCode==CameraImage){
            if (resultCode==Activity.RESULT_OK){
                clickedImage.AddImage(mTempCameraPhotoFile, EditItem.this);
            }
        }
    }



    private void uploadFromUri() {
        if (isMyServiceRunning()){
            Bundle bundle=new Bundle();
            bundle.putSerializable(MyUploadService.EXTRA_FILE_URI,selectedFiles);
            Log.d(tag, "uploadFromUri:src:" + selectedFiles.toString());
            startService(new Intent(EditItem.this, MyUploadService.class)
                    .putExtras(bundle)
                    .setAction(MyUploadService.Add_New_File));
        }else {
            Bundle bundle=new Bundle();
            bundle.putSerializable(MyUploadService.EXTRA_FILE_URI,selectedFiles);
            Log.d(tag, "uploadFromUri:src:" + selectedFiles.toString());
            startService(new Intent(EditItem.this, MyUploadService.class)
                    .putExtras(bundle)
                    .setAction(MyUploadService.ACTION_UPLOAD));
        }
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : Objects.requireNonNull(manager).getRunningServices(Integer.MAX_VALUE)) {
            if (MyUploadService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}