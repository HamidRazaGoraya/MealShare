package com.raza.mealshare.HomeScreen.Fragments.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.ConfirmDialog;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.DialogButtons;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Favourite.FSearchActivity;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.Location.PickUpLocation;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FragmentProfileBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ProfileFragments extends Fragment {
private View view;
private FirebaseUser firebaseUser;
private FirebaseRef ref=new FirebaseRef();
private FragmentProfileBinding fragmentProfileBinding;
private ProfileInfo profileInfo=null;
    public ProfileFragments() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding=FragmentProfileBinding.inflate(inflater,container,false);
        view=fragmentProfileBinding.getRoot();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        setUpAllButtons();
        getUserProfile();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentProfileBinding = null;
    }

    private void getUserProfile() {
        FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                  try {
                      assert value != null;
                      if(value.exists()){
                          fillValues(Objects.requireNonNull(value.toObject(ProfileInfo.class)));
                      }
                  }catch (Exception e){
                      e.printStackTrace();
                  }
            }
        });
    }

    private void fillValues(@NotNull ProfileInfo profileInfo) {
        this.profileInfo=profileInfo;
      fragmentProfileBinding.profileUserName.setText(profileInfo.getUser_name());
      if (!TextUtils.isEmpty(profileInfo.getUser_profile_pic())){
          Glide.with(requireContext()).load(FirebaseStorage.getInstance().getReference().child(profileInfo.getUser_profile_pic())).into(fragmentProfileBinding.profileImage);
      }
        Utilities.SaveProfile(requireActivity(),profileInfo);
    }


    @Override
    public void onResume() {
        super.onResume();

    }





    private void setUpAllButtons() {
        fragmentProfileBinding.myFavourt.title.setText(R.string.my_favourite);
        fragmentProfileBinding.myFavourt.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), FSearchActivity.class));
            }
        });
        view.findViewById(R.id.editPicLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(requireActivity(), PickUpLocation.class));
            }
        });
        view.findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfirmDialog().ShowConfirmDialogLogOut(requireContext(), new DialogButtons() {
                    @Override
                    public void OnApprove() {
                       FirebaseAuth.getInstance().signOut();
                       startActivity(new Intent(requireContext(),MainActivity.class));
                       requireActivity().finish();
                    }

                    @Override
                    public void OnReject() {

                    }
                });
            }
        });
        view.findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileInfo!=null){
                    Bundle bundle = new Bundle();
                    bundle.putString("data",new Gson().toJson(profileInfo));
                    startActivity(new Intent(requireActivity(),UpdateUserProfile.class).putExtras(bundle));
                }else {
                    new CustomToast(requireActivity(),"Check network or sign in again");
                }
            }
        });

    }


}