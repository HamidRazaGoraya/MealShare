package com.raza.mealshare.HomeScreen.Fragments.Search.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Search.ActivityAlItems.ShowAllItems;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.FoodICategoryAdopter;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.Models.SeperatedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.databinding.FragmentMyFeedBinding;

import java.util.ArrayList;


public class Requested extends Fragment {
    private FragmentMyFeedBinding binding;
    private FoodICategoryAdopter foodICategoryAdopter;
    private FirebaseRef ref=new FirebaseRef();

    public Requested() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentMyFeedBinding.inflate(inflater);
        recycleViewSetUp();
        return binding.getRoot();
    }
    private void recycleViewSetUp() {
        foodICategoryAdopter =new FoodICategoryAdopter(getContext(),new ArrayList<>());
        binding.itemList.setLayoutManager(new GridLayoutManager(requireContext(),3));
        binding.itemList.setAdapter(foodICategoryAdopter);
        foodICategoryAdopter.setClickListener(new FoodICategoryAdopter.ItemClickListener() {
            @Override
            public void onItemClick(SeperatedItems postedItems) {
                startActivity(new Intent(requireContext(), ShowAllItems.class).putExtra("data",new Gson().toJson(postedItems)));
            }
        });
        FirebaseFirestore.getInstance().collectionGroup(ref.AllFoodShared).whereEqualTo(ref.Share,false).whereEqualTo(ref.data_status,0).orderBy(ref.data_submission_time, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    foodICategoryAdopter.deleteAllItems();
                    binding.emptyList.setVisibility(View.VISIBLE);
                    assert value != null;
                    ArrayList<Integer> uniqueId=new ArrayList<>();
                    ArrayList<SeperatedItems> seperatedItems=new ArrayList<>();
                    for (DocumentSnapshot snapshot:value.getDocuments()){
                        binding.emptyList.setVisibility(View.GONE);
                        PostedItems postedItems=snapshot.toObject(PostedItems.class).withId(snapshot.getId());
                        if (uniqueId.contains(postedItems.getCategory().getId())){
                            seperatedItems.get(uniqueId.indexOf(postedItems.getCategory().getId())).AddItem(postedItems);
                        }else {
                            uniqueId.add(postedItems.getCategory().getId());
                            seperatedItems.add(new SeperatedItems(postedItems));
                        }
                    }
                    for (int i=0;i<seperatedItems.size();i++){
                        foodICategoryAdopter.insertItems(seperatedItems.get(i));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}