package com.raza.mealshare.HomeScreen.Fragments.Search.ActivityAlItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.ShowImage;
import com.raza.mealshare.Database.AllProductsFills.MyProduct;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.FoodItemAdopter;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.Models.SeperatedItems;
import com.raza.mealshare.databinding.ActivityShowAllItemsBinding;

public class ShowAllItems extends AppCompatActivity {
    private ActivityShowAllItemsBinding binding;
    private FoodItemAdopter foodItemAdopter;
    private FirebaseRef ref=new FirebaseRef();
    private SeperatedItems seperatedItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShowAllItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        seperatedItems =new Gson().fromJson(getIntent().getStringExtra("data"), SeperatedItems.class);
        recycleViewSetUp();
    }
    private void recycleViewSetUp() {
        binding.exitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        foodItemAdopter=new FoodItemAdopter(this,seperatedItems.getPostedItems());
        binding.itemList.setLayoutManager(new LinearLayoutManager(this));
        binding.itemList.setAdapter(foodItemAdopter);
        binding.emptyList.setVisibility(View.GONE);
        foodItemAdopter.setClickListener(new FoodItemAdopter.ItemClickListener() {
            @Override
            public void onItemClick(MyProduct postedItems) {
                new CustomToast(ShowAllItems.this,"Detailed view coming soon");
            }
            @Override
            public void OnImageClick(All_Images all_images) {
                new ShowImage(all_images.getData_fullimage_storage_path()).show(getSupportFragmentManager(),"Image");

            }
        });
    }
}