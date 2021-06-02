package com.raza.mealshare.HomeScreen.Fragments.Share;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.raza.mealshare.ExtraFiles.CustomToast;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.HomeScreen.Fragments.Share.Fragements.MyItemRequested;
import com.raza.mealshare.HomeScreen.Fragments.Share.Fragements.MyItemShare;
import com.raza.mealshare.Location.PickUpLocation;
import com.raza.mealshare.R;
import com.raza.mealshare.databinding.FragmentShareHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ShareHome extends Fragment {
    private FragmentShareHomeBinding binding;
    private FirebaseUser firebaseUser;
    private ProfileInfo profileInfo=null;
    private FirebaseRef ref=new FirebaseRef();
    private Fragment current;
    private FragmentManager fragmentManager;
    private MyItemShare myItemShare=new MyItemShare();
    private MyItemRequested myItemRequested=new MyItemRequested();
    private int UserLocation=106;
    public ShareHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =FragmentShareHomeBinding.inflate(inflater,container,false);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        getUserProfile();
        setupButtons();
        return binding.getRoot();
    }

    private void setupButtons() {
        binding.editPicLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(requireActivity(), PickUpLocation.class),UserLocation);
            }
        });
        fragmentManager=getChildFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag("myItemRequested");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        oldFragment = fragmentManager.findFragmentByTag("myItemShare");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        fragmentManager.beginTransaction().add(R.id.subFragment, myItemRequested, "myItemRequested").hide(myItemRequested).commit();
        fragmentManager.beginTransaction().add(R.id.subFragment, myItemShare, "myItemShare").commit();
        current=myItemShare;
        binding.ShareOrRequest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==binding.Share.getId()){
                    fragmentManager.beginTransaction().hide(current).show(myItemShare).commit();
                    current=myItemShare;
                }else {
                    fragmentManager.beginTransaction().hide(current).show(myItemRequested).commit();
                    current=myItemRequested;
                }
            }
        });
        binding.AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileInfo!=null){
                    if (profileInfo.getUser_location()!=null){
                        if (binding.ShareOrRequest.getCheckedRadioButtonId()==binding.Share.getId()){
                            startActivity(new Intent(requireActivity(), AddItem.class).putExtra("data",new Gson().toJson(profileInfo)).putExtra("starting",1));
                        }else {
                            startActivity(new Intent(requireActivity(), AddItem.class).putExtra("data",new Gson().toJson(profileInfo)).putExtra("starting",2));
                        }
                    }else {
                        new CustomToast(requireActivity(),"Location is required before making post");
                        startActivityForResult(new Intent(requireActivity(), PickUpLocation.class),UserLocation);
                    }
                }else {
                    new CustomToast(requireActivity(),"Check internet connection");
                }
            }
        });
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
        binding.profileUserName.setText(profileInfo.getUser_name());
        if (!TextUtils.isEmpty(profileInfo.getUser_profile_pic())){
            Glide.with(requireContext()).load(FirebaseStorage.getInstance().getReference().child(profileInfo.getUser_profile_pic())).into(binding.profileImage);
        }
        binding.LevelName.setText(profileInfo.getUser_level()+" Level");
        binding.points.setText("( "+profileInfo.getUser_points()+") Points");
    }
}