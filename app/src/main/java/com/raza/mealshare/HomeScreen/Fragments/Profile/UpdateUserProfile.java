package com.raza.mealshare.HomeScreen.Fragments.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.ShortCutDialogs;
import com.raza.mealshare.CustomDialogs.ShowImagePickDialog;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.UploadImage;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.MyToasts;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.LoginFragmentRegisteruserInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


public class UpdateUserProfile extends AppCompatActivity {
String userEmail,userName,gender;
private Calendar DateOfBirthCal=null;
    private int UserImage =103;
    private int CAMERA_REQUEST=104;
    private int cameraPermission=105;
    private Bitmap profileImage=null;
    private LoadingDialog loadingDialog;
    private File mTempCameraPhotoFile;
    private FirebaseRef ref=new FirebaseRef();
    private FirebaseUser firebaseUser;
    private LoginFragmentRegisteruserInfoBinding binding;
    private ProfileInfo profileInfo;
    public UpdateUserProfile() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=LoginFragmentRegisteruserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profileInfo=new Gson().fromJson(getIntent().getStringExtra("data"),ProfileInfo.class);
        loadingDialog=new LoadingDialog(UpdateUserProfile.this,R.style.DialogLoadingTheme);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        setUpButtons();
        fillValues();
    }


    private void fillValues(){
        if (!TextUtils.isEmpty(profileInfo.getUser_profile_pic())){
            Glide.with(this).load(FirebaseStorage.getInstance().getReference().child(profileInfo.getUser_profile_pic())).into(binding.userImage);
        }
        binding.userName.setText(profileInfo.getUser_name());
        binding.email.setText(profileInfo.getUser_email());
        int id=R.id.Share;
        if (!TextUtils.isEmpty(profileInfo.getUser_gender())){

            if (profileInfo.getUser_gender().equals("F")){
                id=R.id.Requested;
            }
        }
        RadioButton radioButton=findViewById(id);
        radioButton.setChecked(true);
        if (profileInfo.getUser_date_birth()!=null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(profileInfo.getUser_date_birth().getTime());
            DateOfBirthCal=calendar;
        }
        setDateOfBirthCal();
    }

    private void  setDateOfBirthCal(){
        if (DateOfBirthCal==null){
            return;
        }
        binding.dateOfBirth.setText(Utilities.getDateFormat().format(DateOfBirthCal.getTime()));
    }
    private void setUpButtons() {
        binding.dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ShortCutDialogs.PickDate(DateOfBirthCal, getSupportFragmentManager(), new ShortCutDialogs.DateResults() {
                        @Override
                        public void OnPicked(Calendar calendar) {
                            DateOfBirthCal=calendar;
                           setDateOfBirthCal();
                        }

                        @Override
                        public void OnCancelled(Calendar calendar) {
                           DateOfBirthCal=calendar;
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSelector();
            }
        });
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (Verified()){
                   showLoading();
                   if (profileImage!=null){
                       UploadImage.UploadProfileBitMap(profileImage,firebaseUser.getUid(),new UploadImage.Uploaded() {
                           @Override
                           public void Status(boolean bol, String path) {
                               if (bol){
                                   UploadUserDetails(path);
                               }else {
                                   UploadUserDetails("");
                               }
                           }
                       });
                   }else {
                       UploadUserDetails("");
                   }
               }else {
                   dismissLoading();
               }
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void UploadUserDetails(@NotNull String profileImage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put(ref.user_name, userName);
        user.put(ref.user_email,userEmail);
        if (DateOfBirthCal!=null){
            user.put(ref.user_date_birth,DateOfBirthCal.getTime());
        }
        user.put(ref.user_gender,gender);
        user.put(ref.user_profile_pic,profileImage);
        db.collection(ref.users).document(firebaseUser.getUid()).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new CustomToast(UpdateUserProfile.this,"Updated");
                dismissLoading();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new CustomToast(UpdateUserProfile.this,"Try again");
                loadingDialog.dismiss();
            }
        });
    }
    private void ShowSelector() {
        ShowImagePickDialog showImagePickDialog=new ShowImagePickDialog(new ShowImagePickDialog.Buttons() {
            @Override
            public void SelectGallery() {
                setUpGalleryImages(UserImage);
            }

            @Override
            public void SelectCamera() {
                openCameraIntent();
            }
        },"Upload image");
        showImagePickDialog.show(UpdateUserProfile.this.getSupportFragmentManager(),"camera");
    }



    private void setUpGalleryImages(int Code) {
        //Todo Gallery Images
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Code);
    }
    private boolean Verified() {
        userName=binding.userName.getText().toString();


        gender="F";
        if (binding.genderSelector.getCheckedRadioButtonId()==R.id.Share){
            gender="M";
        }
        if (TextUtils.isEmpty(userName) ){
            MyToasts.requiredFields(UpdateUserProfile.this,4);
            return false;
        }
        userEmail=binding.email.getText().toString();
         if (!TextUtils.isEmpty(userEmail)){
             if (!isValidEmail(userEmail)){
                 MyToasts.emailNotVelied(UpdateUserProfile.this);
                 return false;
             }
         }

        return true;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Data","Got Data");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== UserImage){
            Log.i("Data","Got Data3");
            try {
                if(resultCode == Activity.RESULT_OK) {
                    Uri userImage=data.getData();
                    Glide.with(UpdateUserProfile.this).asBitmap().load(userImage).into(new BitmapImageViewTarget(binding.userImage){
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            profileImage=resource;
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (requestCode==CAMERA_REQUEST){
                     if (resultCode==Activity.RESULT_OK){
                         Glide.with(UpdateUserProfile.this).asBitmap().load(mTempCameraPhotoFile.getPath()).into(new BitmapImageViewTarget(binding.userImage){
                             @Override
                             protected void setResource(Bitmap resource) {
                                 super.setResource(resource);
                                 profileImage=resource;
                             }
                         });
                     }
        }
    }


    private void  dismissLoading(){
        try {
            loadingDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void  showLoading(){
        try {
            loadingDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            mTempCameraPhotoFile = new File(getFilesDir()+ "/" + Utilities.AudioFileName().format(Calendar.getInstance().getTime())+ ".jpg");
            Uri photoURI = FileProvider.getUriForFile(UpdateUserProfile.this, "com.raza.mealshare.provider", mTempCameraPhotoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI);
            startActivityForResult(pictureIntent,
                    CAMERA_REQUEST);
        }
    }


}