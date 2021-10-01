package com.raza.mealshare.HomeScreen.Fragments.Search;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.GetCategoryWithFIlter;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.RadiusFiles.AllRadius;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.SettingsModel;
import com.raza.mealshare.HomeScreen.Fragments.Search.Fragments.Requested;
import com.raza.mealshare.HomeScreen.Fragments.Search.Fragments.Share;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FragmentSearchItemsBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SearchFragment extends Fragment {

    private FragmentSearchItemsBinding binding;
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
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsModel settingsModel= new Gson().fromJson(Utilities.getSharedPreferences(requireActivity()).getString(ref.Settings,""),SettingsModel.class);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<RadiusAndCategory> andCategories= AllDataBaseConstant.getInstance(requireContext()).allRadius().getAllOnce();
                        if (andCategories.size()>0){
                            new GetCategoryWithFIlter(settingsModel.getCategory(),andCategories.get(0)).show(getChildFragmentManager(),"Filter");
                        }else {
                            RadiusAndCategory andCategory=new RadiusAndCategory("ALL",0,6000);
                            new GetCategoryWithFIlter(settingsModel.getCategory(),andCategory).show(getChildFragmentManager(),"Filter");
                        }
                    }
                });
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