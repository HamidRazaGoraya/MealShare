package com.raza.mealshare.HomeScreen.Fragments.Profile.Favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Favourite.Fragments.FRequested;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Favourite.Fragments.FShare;
import com.raza.mealshare.LogIn.SignIn;
import com.raza.mealshare.R;
import com.raza.mealshare.databinding.FragmentSearchItemsBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FSearchActivity extends AppCompatActivity {
private FragmentSearchItemsBinding binding;
    private Fragment current;
    private FragmentManager fragmentManager;
    private FShare FShareItemsFragment =new FShare();
    private FRequested FRequestedItemFragment =new FRequested();
    private FirebaseRef ref=new FirebaseRef();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=FragmentSearchItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpButtons();
    }
    private void setUpButtons() {
        binding.filter.setVisibility(View.GONE);
        fragmentManager=getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag("requestedItemFragment");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        oldFragment = fragmentManager.findFragmentByTag("shareItemsFragment");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        fragmentManager.beginTransaction().add(R.id.searchBottomFragement, FRequestedItemFragment, "requestedItemFragment").hide(FRequestedItemFragment).commit();
        fragmentManager.beginTransaction().add(R.id.searchBottomFragement, FShareItemsFragment, "shareItemsFragment").commit();
        current= FShareItemsFragment;
        binding.ShareOrRequest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==binding.Share.getId()){
                    fragmentManager.beginTransaction().hide(current).show(FShareItemsFragment).commit();
                    current= FShareItemsFragment;
                }else {
                    fragmentManager.beginTransaction().hide(current).show(FRequestedItemFragment).commit();
                    current= FRequestedItemFragment;
                }
            }
        });
    }
}