package com.raza.mealshare.LogIn;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raza.mealshare.CustomDialogs.ShortCutDialogs;
import com.raza.mealshare.CustomDialogs.ShowImagePickDialog;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.UploadImage;
import com.raza.mealshare.HomeScreen.Fragments.Share.AddItem;
import com.raza.mealshare.Intro.IntroImageWizard;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.CheckForPermissions;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.MyToasts;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.LoginSignUpNumberBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpEmail extends Fragment implements IOnBackPressed {
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
    private LoginSignUpNumberBinding binding;
    private SharedPreferences sharedPref;
    private Bundle bundle;
    public SignUpEmail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=LoginSignUpNumberBinding.inflate(inflater,container,false);

        bundle=getArguments();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        loadingDialog = new LoadingDialog(requireContext(), R.style.DialogLoadingTheme);
        sharedPref = requireActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean(ref.NewUser,true).apply();
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                if (Verified()){
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
                    loadingDialog.dismiss();
                }
            }

        });
        fillValues();
        binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSelector();
            }
        });
        binding.dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ShortCutDialogs.PickDate(DateOfBirthCal, getChildFragmentManager(), new ShortCutDialogs.DateResults() {
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
        return binding.getRoot();
    }
    private void  setDateOfBirthCal(){
        if (DateOfBirthCal==null){
            return;
        }
        binding.dateOfBirth.setText(Utilities.getDateFormat().format(DateOfBirthCal.getTime()));
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
                setUpGalleryImages(UserImage);
            }

            @Override
            public void SelectCamera() {
                CheckForPermissions.CheckForCameraPermission(requireActivity(), requireContext(), 101, new CheckForPermissions.Results() {
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
        showImagePickDialog.show(requireActivity().getSupportFragmentManager(),"camera");
    }
    private boolean Verified() {
        userName=binding.userName.getText().toString();


        gender="F";
        if (binding.genderSelector.getCheckedRadioButtonId()==R.id.Share){
            gender="M";
        }
        if (TextUtils.isEmpty(userName) ){
            MyToasts.requiredFields(requireContext(),4);
            return false;
        }
        userEmail=binding.email.getText().toString();
        if (!TextUtils.isEmpty(userEmail)){
            if (!isValidEmail(userEmail)){
                MyToasts.emailNotVelied(requireContext());
                return false;
            }
        }
        return true;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void setUpGalleryImages(int Code) {
        //Todo Gallery Images
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Code);
    }
   private void fillValues(){
        if (bundle.containsKey(ref.user_email)){
            binding.email.setText(bundle.getString(ref.user_email,""));
            binding.userName.setText(bundle.getString(ref.user_name,"")+" "+bundle.getString(ref.LastName,""));
        }

       try {
           if (bundle.containsKey(ref.user_profile_pic)){
               Glide.with(requireContext()).asBitmap().load(bundle.getString(ref.user_profile_pic)).into(new BitmapImageViewTarget(binding.userImage){
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
    private void loadFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "signIn");
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    private void UploadUserDetails(@NotNull String profileImage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put(ref.user_name, userName);
        user.put(ref.user_email,userEmail);
        user.put(ref.user_joining_time, FieldValue.serverTimestamp());
        user.put(ref.user_profile_pic,profileImage);
        user.put(ref.user_gender,gender);
        if (DateOfBirthCal!=null){
            user.put(ref.user_date_birth,DateOfBirthCal.getTime());
        }
        db.collection(ref.users).document(firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                sharedPref.edit().putBoolean(ref.NewUser,false).apply();
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               loadingDialog.dismiss();
            }
        });
    }

    private void updateUI() {
        loadingDialog.dismiss();
        if (firebaseUser!=null){
            if (getContext()!=null){
                startActivity(new Intent(getContext(), IntroImageWizard.class));
                requireActivity().finish();
            }
        }
    }


    @Override
    public boolean onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        loadFragment(new SignIn());
        return true;
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
                    Glide.with(requireContext()).asBitmap().load(userImage).into(new BitmapImageViewTarget(binding.userImage){
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
                Glide.with(requireContext()).asBitmap().load(mTempCameraPhotoFile.getPath()).into(new BitmapImageViewTarget(binding.userImage){
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        profileImage=resource;
                    }
                });
            }
        }
    }
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(requireActivity().getPackageManager()) != null){
            //Create a file to store the image
            mTempCameraPhotoFile = new File(requireActivity().getFilesDir()+ "/" + Utilities.AudioFileName().format(Calendar.getInstance().getTime())+ ".jpg");
            Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.raza.mealshare.provider", mTempCameraPhotoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI);
            startActivityForResult(pictureIntent,
                    CAMERA_REQUEST);
        }
    }
}
