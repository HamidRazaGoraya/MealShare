package com.raza.mealshare.HomeScreen.Fragments.Share.Results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.raza.mealshare.CustomDialogs.ConfirmDialog;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.CustomDialogs.DialogButtons;
import com.raza.mealshare.CustomDialogs.ShowImage;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Share.Results.Adopter.FoodItemAdopterWithOptions;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.databinding.FragmentAllProductsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PeoplesWhichHaveShownIntrest extends Fragment {
private FragmentAllProductsBinding binding;
private boolean share=false;
private FirebaseRef ref=new FirebaseRef();
private FirebaseUser firebaseUser;
private FoodItemAdopterWithOptions adopterWithOptions;
private LoadingDialog loadingDialog;
    public PeoplesWhichHaveShownIntrest() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentAllProductsBinding.inflate(inflater,container,false);
        binding.emptyList.setText(R.string.not_one_found);

        share=getArguments().getBoolean(RequestAndShareResults.Type);
        loadingDialog=new LoadingDialog(requireContext(),R.style.DialogLoadingTheme);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (share){
            loadShared();
        }else {
            loadRequested();
        }
        return binding.getRoot();
    }

    private void loadRequested() {
        binding.title.setText(R.string.select_person);
        adopterWithOptions=new FoodItemAdopterWithOptions(getContext(),new ArrayList<>());
        binding.allProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.allProducts.setAdapter(adopterWithOptions);
        adopterWithOptions.setClickListener(new FoodItemAdopterWithOptions.ItemClickListener() {
            @Override
            public void onItemClick(PostedItems postedItems) {
                new CustomToast(requireActivity(),"Edit coming soon");
            }

            @Override
            public void OnImageClick(All_Images all_images) {
                new ShowImage(all_images.getData_fullimage_storage_path()).show(getChildFragmentManager(),"Image");

            }

            @Override
            public void OnDeleteClick(PostedItems postedItems) {
                DeleteButton(postedItems);
            }

            @Override
            public void OnMarkCompleteClicked(PostedItems postedItems) {

            }
        });
        FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).collection(ref.AllFoodShared).whereEqualTo(ref.Share,true).orderBy(ref.data_submission_time, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    adopterWithOptions.deleteAllItems();
                    binding.emptyList.setVisibility(View.VISIBLE);
                    assert value != null;
                    for (DocumentSnapshot snapshot:value.getDocuments()){
                        binding.emptyList.setVisibility(View.GONE);
                        adopterWithOptions.insertItems(snapshot.toObject(PostedItems.class).withId(snapshot.getId()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadShared() {
        binding.title.setText(R.string.select_reciver);
        adopterWithOptions=new FoodItemAdopterWithOptions(getContext(),new ArrayList<>());
        binding.allProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.allProducts.setAdapter(adopterWithOptions);
        adopterWithOptions.setClickListener(new FoodItemAdopterWithOptions.ItemClickListener() {
            @Override
            public void onItemClick(PostedItems postedItems) {
                new CustomToast(requireActivity(),"Edit coming soon");
            }

            @Override
            public void OnImageClick(All_Images all_images) {
                new ShowImage(all_images.getData_fullimage_storage_path()).show(getChildFragmentManager(),"Image");

            }

            @Override
            public void OnDeleteClick(PostedItems postedItems) {
                DeleteButton(postedItems);
            }

            @Override
            public void OnMarkCompleteClicked(PostedItems postedItems) {

            }
        });
        FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).collection(ref.AllFoodShared).whereEqualTo(ref.Share,true).orderBy(ref.data_submission_time, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    adopterWithOptions.deleteAllItems();
                    binding.emptyList.setVisibility(View.VISIBLE);
                    assert value != null;
                    for (DocumentSnapshot snapshot:value.getDocuments()){
                        binding.emptyList.setVisibility(View.GONE);
                        adopterWithOptions.insertItems(snapshot.toObject(PostedItems.class).withId(snapshot.getId()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void DeleteButton(PostedItems postedItems){
        ConfirmDialog.ShowConfirmDeleteItem(requireContext(), new DialogButtons() {
            @Override
            public void OnApprove() {
                loadingDialog.show();
                FirebaseFirestore.getInstance().collection(ref.users).document(firebaseUser.getUid()).collection(ref.AllFoodShared).document(postedItems.getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try {
                            loadingDialog.dismiss();
                            if (task.isSuccessful()){
                                new CustomToast(requireActivity(),"Delete");

                            }else {
                               new  CustomToast(requireActivity(),task.getException().getMessage());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void OnReject() {

            }
        });
    }
}