package com.raza.mealshare.HomeScreen.Fragments.Share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.ShowCategoryPicker;
import com.raza.mealshare.CustomDialogs.ShowConditionPicker;
import com.raza.mealshare.CustomDialogs.ShowImagePickDialog;
import com.raza.mealshare.ExtraFiles.CustomToast;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.HomeScreen.Fragments.Share.Models.SelectedImage;
import com.raza.mealshare.Location.PickUpLocation;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.R;
import com.raza.mealshare.Services.MyUploadService;
import com.raza.mealshare.Services.UploadFilesModel;
import com.raza.mealshare.Utilities.CheckForPermissions;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.ActivityAddItemBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Intent.CATEGORY_OPENABLE;

public class AddItem extends AppCompatActivity {
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
    private Category category;
    private ArrayList<UploadFilesModel> selectedFiles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog=new LoadingDialog(this, R.style.DialogLoadingTheme);
        profileInfo=new Gson().fromJson(getIntent().getStringExtra("data"),ProfileInfo.class);
        shareOrRequest=getIntent().getIntExtra("starting",1);
        setUpButtons();
        setUpToolbar();
    }

    private void setUpToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Item");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void setUpButtons() {
        if (shareOrRequest==1){
            binding.Requested.setChecked(false);
            binding.Share.setChecked(true);
        }else {
            binding.Share.setChecked(false);
            binding.Requested.setChecked(true);
        }
        binding.Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new  ShowCategoryPicker(new ShowCategoryPicker.Buttons() {
                   @Override
                   public void SelectedCategory(Category category) {
                       AddItem.this.category=category;
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
        binding.UploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Varified()){
                    new CustomToast(AddItem.this,"Posting...");
                    loadingDialog.show();
                    HashMap<String, Object> map=new HashMap<>();
                    map.put(ref.ItemName,binding.ItemName.getText().toString());
                    map.put(ref.ItemDescription,binding.ItemDescription.getText().toString());
                    map.put(ref.ItemDescriptionLong,binding.ItemDescriptionLong.getText().toString());
                    map.put(ref.data_status,0);
                    map.put(ref.data_submission_time, FieldValue.serverTimestamp());
                    map.put(ref.Condition,"");
                    map.put(ref.Share,binding.ShareOrRequest.getCheckedRadioButtonId()==binding.Share.getId());
                    map.put(ref.user_location,profileInfo.getUser_location());
                    map.put(ref.Category,category);
                    HashMap<String, Object> userInfo=new HashMap<>();
                    userInfo.put(ref.user_name,profileInfo.getUser_name());
                    userInfo.put(ref.user_email,profileInfo.getUser_email());
                    userInfo.put(ref.user_profile_pic,profileInfo.getUser_profile_pic());
                    userInfo.put(ref.user_uid, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put(ref.userInfo,userInfo);
                    FirebaseFirestore.getInstance().collection(ref.users).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(ref.AllFoodShared).add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                          try {
                              loadingDialog.dismiss();
                              if (task.isSuccessful()){
                                  new CustomToast(AddItem.this,"Added");
                                  selectedFiles=new ArrayList<>();
                                  for (int i=0;i<selectedImages.size();i++){
                                      if (selectedImages.get(i).isAdded()){
                                          selectedFiles.add(new UploadFilesModel(selectedImages.get(i).getUri(),task.getResult().getId()));
                                      }
                                  }
                                  if (selectedFiles.size()>0){
                                      uploadFromUri();
                                  }
                                  finish();
                              }else {
                                  new CustomToast(AddItem.this,"Failed try again later");
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
        if (binding.ItemName.getText().toString().replace(" ","").length()<2){
            binding.ItemName.setError("Minim 2 Characters required");
            binding.ItemName.requestFocus();
            return false;
        }
        if (binding.ItemDescription.getText().toString().replace(" ","").length()<4){
            binding.ItemDescription.setError("Minim 4 Characters required");
            binding.ItemDescription.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(binding.Category.getText().toString())){
            new CustomToast(AddItem.this,"Pick  Category");
            binding.Category.callOnClick();
            return false;
        }
        for (int i=0;i<selectedImages.size();i++){
            if (selectedImages.get(i).isAdded()){
                return true;
            }
        }
        new CustomToast(AddItem.this,"Add One image");
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent();
            }
        }else {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(AddItem.this,"Permission required",Toast.LENGTH_SHORT).show();
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
                CheckForPermissions.CheckForCameraPermission(AddItem.this, AddItem.this, 101, new CheckForPermissions.Results() {
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
        showImagePickDialog.show(AddItem.this.getSupportFragmentManager(),"camera");
    }
    private void setUpGalleryImages() {
        //Todo Gallery Images
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GalleryImage);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mTempCameraPhotoFile= File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return mTempCameraPhotoFile;
    }

    private void openCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
              ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.raza.mealshare.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CameraImage);
            }
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    clickedImage.AddImage(data.getData(),AddItem.this);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (requestCode==CameraImage){
            if (resultCode==Activity.RESULT_OK){
                clickedImage.AddImage(mTempCameraPhotoFile,AddItem.this);
            }
        }
    }




    private void uploadFromUri() {
        if (isMyServiceRunning()){
            Bundle bundle=new Bundle();
            bundle.putSerializable(MyUploadService.EXTRA_FILE_URI,selectedFiles);
            Log.d(tag, "uploadFromUri:src:" + selectedFiles.toString());
            startService(new Intent(AddItem.this, MyUploadService.class)
                    .putExtras(bundle)
                    .setAction(MyUploadService.Add_New_File));
        }else {
            Bundle bundle=new Bundle();
            bundle.putSerializable(MyUploadService.EXTRA_FILE_URI,selectedFiles);
            Log.d(tag, "uploadFromUri:src:" + selectedFiles.toString());
            startService(new Intent(AddItem.this, MyUploadService.class)
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