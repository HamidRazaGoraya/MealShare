package com.raza.mealshare.HomeScreen.Fragments.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.RadiusPicker;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Search.Fragments.Requested;
import com.raza.mealshare.HomeScreen.Fragments.Search.Fragments.Share;
import com.raza.mealshare.Location.PickUpLocation;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FragmentSearchItemsBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SearchFragment extends Fragment {
private FragmentSearchItemsBinding binding;
private int LocationUpdate;
    private Fragment current;
    private FragmentManager fragmentManager;
    private Share shareItemsFragment =new Share();
    private Requested requestedItemFragment =new Requested();
    private FirebaseRef ref=new FirebaseRef();
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentSearchItemsBinding.inflate(inflater,container,false);
        setUpButtons();
        return binding.getRoot();
    }

    private void setUpButtons() {
        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(requireActivity(), PickUpLocation.class),LocationUpdate);
            }
        });
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadiusPicker radiusPicker=new RadiusPicker(new RadiusPicker.Buttons() {
                    @Override
                    public void SelectedRadius(int radius) {
                        Utilities.getSharedPreferences(requireActivity()).edit().putInt(ref.SearchRadius,radius).apply();
                    }
                });
                radiusPicker.show(getChildFragmentManager(),"pickRadius");
            }
        });

        fragmentManager=getChildFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag("requestedItemFragment");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        oldFragment = fragmentManager.findFragmentByTag("shareItemsFragment");
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }
        fragmentManager.beginTransaction().add(R.id.searchBottomFragement, requestedItemFragment, "requestedItemFragment").hide(requestedItemFragment).commit();
        fragmentManager.beginTransaction().add(R.id.searchBottomFragement, shareItemsFragment, "shareItemsFragment").commit();
        current= shareItemsFragment;
        binding.ShareOrRequest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==binding.Share.getId()){
                    fragmentManager.beginTransaction().hide(current).show(shareItemsFragment).commit();
                    current= shareItemsFragment;
                }else {
                    fragmentManager.beginTransaction().hide(current).show(requestedItemFragment).commit();
                    current= requestedItemFragment;
                }
            }
        });
    }


}