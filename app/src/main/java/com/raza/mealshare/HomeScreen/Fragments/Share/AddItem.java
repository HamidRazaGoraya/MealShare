package com.raza.mealshare.HomeScreen.Fragments.Share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
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
import com.raza.mealshare.ExtraFiles.Category;
import com.raza.mealshare.ExtraFiles.CustomToast;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.HomeScreen.Fragments.Share.Models.SelectedImage;
import com.raza.mealshare.R;
import com.raza.mealshare.Services.MyUploadService;
import com.raza.mealshare.Services.UploadFilesModel;
import com.raza.mealshare.Services.UploadFilesOrignal;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.ActivityAddItemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddItem extends AppCompatActivity {
private ActivityAddItemBinding binding;
private ProfileInfo profileInfo;
private int shareOrRequest=1;
private ArrayList<SelectedImage> selectedImages;
private File mTempCameraPhotoFile;
private SelectedImage clickedImage;
    private int GalleryImage =103;
    private int CameraImage=104;
    private Category category=null;
    private LoadingDialog loadingDialog;
    private FirebaseRef ref=new FirebaseRef();
    private final String tag="uploadImages";
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
    }

    private void setUpButtons() {
        if (shareOrRequest==1){
            binding.Requested.setChecked(false);
            binding.Share.setChecked(true);
        }else {
            binding.Share.setChecked(false);
            binding.Requested.setChecked(true);
        }
        binding.pickCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCategoryPicker showCategoryPicker=new ShowCategoryPicker(new ShowCategoryPicker.Buttons() {
                    @Override
                    public void SelectedCategory(Category category) {
                        AddItem.this.category=category;
                        binding.CategoryText.setText(category.getName());
                        Glide.with(AddItem.this).load(Utilities.StorageReference(category.getIcon())).into(binding.CategoryImage);
                    }
                });
                showCategoryPicker.show(getSupportFragmentManager(),"CategoryPick");
            }
        });
        binding.FoodCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConditionPicker conditionPicker=new ShowConditionPicker(new ShowConditionPicker.Buttons() {
                    @Override
                    public void SelectedCondition(String condition) {
                        binding.FoodCondition.setText(condition);
                    }
                });
                conditionPicker.show(getSupportFragmentManager(),"Condition");
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
                    map.put(ref.Category,category);
                    map.put(ref.data_status,0);
                    map.put(ref.data_submission_time, FieldValue.serverTimestamp());
                    map.put(ref.Condition,binding.FoodCondition.getText().toString());
                    map.put(ref.Share,binding.ShareOrRequest.getCheckedRadioButtonId()==binding.Share.getId());
                    map.put(ref.user_location,profileInfo.getUser_location());
                    HashMap<String, Object> userInfo=new HashMap<>();
                    userInfo.put(ref.user_name,profileInfo.getUser_name());
                    userInfo.put(ref.user_email,profileInfo.getUser_email());
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
        if (TextUtils.isEmpty(binding.FoodCondition.getText().toString())){
            new CustomToast(AddItem.this,"Pick Food Condition");
            binding.FoodCondition.callOnClick();
            return false;
        }
        if (category==null){
            new CustomToast(AddItem.this,"Pick Category");
            binding.pickCategory.callOnClick();
            return false;
        }
        return true;
    }

    private void ShowSelector() {
        ShowImagePickDialog showImagePickDialog=new ShowImagePickDialog(new ShowImagePickDialog.Buttons() {
            @Override
            public void SelectGallery() {
                setUpGalleryImages();
            }

            @Override
            public void SelectCamera() {
                openCameraIntent();
            }
        },"Upload image");
        showImagePickDialog.show(AddItem.this.getSupportFragmentManager(),"camera");
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
            Uri photoURI = FileProvider.getUriForFile(AddItem.this, "com.raza.mealshare.provider", mTempCameraPhotoFile);
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