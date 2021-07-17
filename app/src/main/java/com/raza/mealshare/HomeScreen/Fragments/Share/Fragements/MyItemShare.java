package com.raza.mealshare.HomeScreen.Fragments.Share.Fragements;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.rxjava3.core.Observable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.ShowImage;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.AllProductsFills.MyProduct;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.FoodItemAdopter;
import com.raza.mealshare.HomeScreen.Fragments.Share.EditItem;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.databinding.FragmentMyItemShareBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyItemShare extends Fragment {

    private FragmentMyItemShareBinding binding;
    private FoodItemAdopter foodItemAdopter;
    private FirebaseRef ref=new FirebaseRef();
    public MyItemShare() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentMyItemShareBinding.inflate(inflater,container,false);
        recycleViewSetUp();
        return binding.getRoot();
    }

    private void recycleViewSetUp() {
        foodItemAdopter=new FoodItemAdopter(getContext(),new ArrayList<>());
        binding.itemList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.itemList.setAdapter(foodItemAdopter);
        foodItemAdopter.setClickListener(new FoodItemAdopter.ItemClickListener() {
            @Override
            public void onItemClick(MyProduct myProduct) {
                startActivity(new Intent(requireContext(), EditItem.class).putExtra("myProduct",new Gson().toJson(myProduct)));

            }

            @Override
            public void OnImageClick(All_Images all_images) {
                new ShowImage(all_images.getData_fullimage_storage_path()).show(getChildFragmentManager(),"Image");

            }
        });
        AllDataBaseConstant.getInstance(requireContext()).allMyItemsDeo().LiveAllShared(1).observeForever(new Observer<List<MyProduct>>() {
            @Override
            public void onChanged(List<MyProduct> myProducts) {
                try {
                    try {
                        foodItemAdopter.deleteAllItems();
                        binding.emptyList.setVisibility(View.VISIBLE);
                        if (myProducts!=null){
                            foodItemAdopter.UpdateAll(myProducts);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}