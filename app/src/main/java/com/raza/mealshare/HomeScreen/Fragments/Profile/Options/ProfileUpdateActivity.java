package com.raza.mealshare.HomeScreen.Fragments.Profile.Options;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.raza.mealshare.CustomDialogs.ShowImagePickDialog;
import com.raza.mealshare.R;
import com.raza.mealshare.Registration.Models.LoginModel;
import com.raza.mealshare.Utilities.CheckForPermissions;
import com.raza.mealshare.Utilities.DateUtilities;
import com.raza.mealshare.Utilities.InputFields;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.MyToasts;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class ProfileUpdateActivity extends AppCompatActivity {
    TextInputEditText EmailText;
    String userEmail,userName, DateOfBirth,gender,preferFoot, bestPositionOneValue,bestPositionTwoValue;
    private Calendar DateOfBirthCal;
    private int UserImage =103;
    private int CAMERA_REQUEST=104;
    private Bitmap profileImage=null;
    private LoadingDialog loadingDialog;
    private LoginModel loginModel;
    private File mTempCameraPhotoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        loadingDialog=new LoadingDialog(ProfileUpdateActivity.this,R.style.DialogLoadingTheme);
        setUpButtons();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        fillValues();
    }


    private void setUpButtons() {
        findViewById(R.id.userImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckForPermissions.checkForReadAndWrite(ProfileUpdateActivity.this,ProfileUpdateActivity.this, new CheckForPermissions.Results() {
                    @Override
                    public void HavePermission() {
                        ShowSelector();
                    }

                    @Override
                    public void Requested() {

                    }
                });

            }
        });
        EmailText=findViewById(R.id.email);
        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Verified()){

                }
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
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
        },"上传头像");
        showImagePickDialog.show(getSupportFragmentManager(),"camera");
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
        RadioGroup sideSelector=findViewById(R.id.sideSelector);
        RadioButton selected=findViewById(sideSelector.getCheckedRadioButtonId());
        preferFoot=selected.getText().toString();
        InputFields inputFields=new InputFields();
        userName=inputFields.getTextFromIdAIET((TextInputEditText)findViewById(R.id.userName));
        DateOfBirth =inputFields.getTextFromIdAIET((TextInputEditText)findViewById(R.id.dateOfBirth));
        RadioGroup radioGroup=findViewById(R.id.ShareOrRequest);
        gender="F";
        if (radioGroup.getCheckedRadioButtonId()==R.id.Share){
            gender="M";
        }
        if (TextUtils.isEmpty(userName)){
            MyToasts.requiredFields(ProfileUpdateActivity.this);
            return false;
        }
        if (TextUtils.isEmpty(DateOfBirth)){
            MyToasts.requiredFields(ProfileUpdateActivity.this);
            return false;
        }
        if (TextUtils.isEmpty(bestPositionOneValue)){
            MyToasts.requiredFields(ProfileUpdateActivity.this);
            return false;
        }
        if (TextUtils.isEmpty(bestPositionTwoValue)){
            MyToasts.requiredFields(ProfileUpdateActivity.this);
            return false;
        }
        userEmail=EmailText.getText().toString();
        if (!TextUtils.isEmpty(userEmail)){
            if (!isValidEmail(userEmail)){
                MyToasts.emailNotVelied(ProfileUpdateActivity.this);
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
            try {
                if(resultCode == Activity.RESULT_OK) {
                    Uri userImage=data.getData();
                    Glide.with(ProfileUpdateActivity.this).asBitmap().load(userImage).into(new BitmapImageViewTarget((CircularImageView) findViewById(R.id.userImage)){
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
        }
        else if (requestCode==CAMERA_REQUEST){
            if (resultCode==Activity.RESULT_OK){
                try {
                        Glide.with(ProfileUpdateActivity.this).asBitmap().load(mTempCameraPhotoFile.getPath()).into(new BitmapImageViewTarget((CircularImageView) findViewById(R.id.userImage)){
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                profileImage=resource;
                            }
                        });
                }catch (Exception e){
                    e.printStackTrace();
                }
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
    private void fillValues(){
        LoginModel.User user=loginModel.getData().getUser();
        Glide.with(ProfileUpdateActivity.this).asBitmap().load(user.getAvatar()).placeholder(R.drawable.icon_upload).into((CircularImageView) findViewById(R.id.userImage));
        TextInputEditText userName=findViewById(R.id.userName);
        userName.setText(user.getName());
        int id=R.id.Share;
        if (user.getGender().equals("F")){
            id=R.id.Requested;
        }


        RadioButton radioButton=findViewById(id);
        radioButton.setChecked(true);
        TextInputEditText dateOfBirth=findViewById(R.id.dateOfBirth);
        DateOfBirthCal= DateUtilities.GetCalenderFromServer(user.getBirthday());
        dateOfBirth.setText(DateUtilities.BirthDay().format(DateOfBirthCal.getTime()));
        bestPositionOneValue =user.getBestPosition();
        bestPositionTwoValue=user.getBestPosition2();
        TextView bestPositionView=findViewById(R.id.bestPositionOne);
        if (bestPositionOneValue !=null){
            if (!TextUtils.isEmpty(bestPositionOneValue)){
                bestPositionView.setText(bestPositionOneValue);
                findViewById(R.id.lableBest).setVisibility(View.VISIBLE);
                TextView bestPositionTwo=findViewById(R.id.bestPositiontwo);
                if (user.getBestPosition2()!=null){
                    bestPositionTwo.setText(user.getBestPosition2());
                }
            }
        }
        TextInputEditText email=findViewById(R.id.email);
        email.setText(user.getEmail());
    }
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
           try {
                mTempCameraPhotoFile = createImageFile();
            } catch (IOException ex) { }
            if (mTempCameraPhotoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.raza.mealshare.provider", mTempCameraPhotoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_REQUEST); } } }
    @NotNull
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",   storageDir);
    }
}