package com.raza.mealshare.HomeScreen.Fragments.Share.Fragements;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.ShowImage;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.FoodItemAdopter;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.databinding.FragmentMyItemShareBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
            public void onItemClick(PostedItems postedItems) {
                new CustomToast(requireActivity(),"Edit coming soon");
            }

            @Override
            public void OnImageClick(All_Images all_images) {
                new ShowImage(all_images.getData_fullimage_storage_path()).show(getChildFragmentManager(),"Image");

            }
        });
        FirebaseFirestore.getInstance().collection(ref.users).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(ref.AllFoodShared).whereEqualTo(ref.Share,true).orderBy(ref.data_submission_time, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               try {
                   foodItemAdopter.deleteAllItems();
                   binding.emptyList.setVisibility(View.VISIBLE);
                   assert value != null;
                   for (DocumentSnapshot snapshot:value.getDocuments()){
                       binding.emptyList.setVisibility(View.GONE);
                       foodItemAdopter.insertItems(snapshot.toObject(PostedItems.class).withId(snapshot.getId()));
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
    }
}