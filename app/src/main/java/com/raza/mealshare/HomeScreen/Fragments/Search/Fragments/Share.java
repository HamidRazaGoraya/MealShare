package com.raza.mealshare.HomeScreen.Fragments.Search.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.MustSignIn;
import com.raza.mealshare.CustomDialogs.ShowIntrest;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.AllProductsFills.AllFavoritItemsDeo;
import com.raza.mealshare.Database.AllProductsFills.MyFavoruitProduct;
import com.raza.mealshare.Database.AllProductsFills.OtherProduct;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;
import com.raza.mealshare.ExtraFiles.CustomToast;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.ItemsDetails.ItemDetails;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.HomeScreen.Fragments.Search.ActivityAlItems.ShowAllItems;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.FoodICategoryAdopter;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.Models.SeperatedItems;
import com.raza.mealshare.Models.UserInfo;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FragmentMyFeedBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Share extends Fragment {
    private FragmentMyFeedBinding binding;
    private FoodICategoryAdopter foodICategoryAdopter;
    private FirebaseRef ref=new FirebaseRef();
    private LoadingDialog loadingDialog;
    private LiveData<List<OtherProduct>> listLiveData;
    private Observer<List<OtherProduct>> observer;
    public Share() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentMyFeedBinding.inflate(inflater);
        loadingDialog=new LoadingDialog(requireContext(), R.style.DialogLoadingTheme);
        recycleViewSetUp();
        setupObserver();
        return binding.getRoot();
    }
    private void setupObserver(){
        observer=new Observer<List<OtherProduct>>() {
            @Override
            public void onChanged(List<OtherProduct> myProducts) {
                try {
                    try {
                        foodICategoryAdopter.deleteAllItems();
                        binding.emptyList.setVisibility(View.VISIBLE);
                        foodICategoryAdopter.UpdateAll(myProducts);
                        if (myProducts.size()<1){
                            binding.emptyList.setVisibility(View.VISIBLE);
                        }else {
                            binding.emptyList.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
    private void recycleViewSetUp() {
        foodICategoryAdopter =new FoodICategoryAdopter(getContext(),new ArrayList<>());
        binding.allProduccts.setAdapter(foodICategoryAdopter);
        binding.allProduccts.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        foodICategoryAdopter.setClickListener(new FoodICategoryAdopter.ItemClickListener() {
            @Override
            public void onItemClick(OtherProduct postedItems) {
                startActivity(new Intent(requireContext(), ItemDetails.class).putExtra("otherProduct",new Gson().toJson(postedItems)));
            }

            @Override
            public void onConnectClicked(OtherProduct postedItems) {
                if (postedItems.getUserInfo().getUser_uid().equals(FirebaseAuth.getInstance().getCurrentUser())){
                    new CustomToast(requireActivity(),"Can't send message to your self");
                    return;
                }
                if (postedItems.getAllInterested()!=null){
                    for (int i=0;i<postedItems.getAllInterested().size();i++){
                        if (postedItems.getAllInterested().get(i).getUser_uid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            new CustomToast(requireActivity(),"Already sent message for this product check messages");
                            return;
                        }
                    }
                }
                     new ShowIntrest(new ShowIntrest.Buttons() {
                         @Override
                         public void SendMessage() {
                             ProfileInfo profileInfo= Utilities.UserProfile(requireActivity());
                             if (profileInfo.getUser_location()==null){
                                 return;
                             }
                             HashMap<String, Object> userInfo=new HashMap<>();
                             userInfo.put(ref.user_name,profileInfo.getUser_name());
                             userInfo.put(ref.user_profile_pic,profileInfo.getUser_profile_pic());
                             userInfo.put(ref.user_uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
                             userInfo.put(ref.user_location,profileInfo.getUser_location());
                             HashMap<String, Object> allInterested = new HashMap<>();
                             allInterested.put(ref.interested_users,userInfo);
                             allInterested.put(ref.user_uid,postedItems.getUserInfo().getUser_uid());
                             allInterested.put(ref.product_id,postedItems.baseKey);
                             loadingDialog.show();
                             FirebaseFirestore.getInstance().collection(ref.interested_users).add(allInterested).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                 @Override
                                 public void onComplete(@NonNull Task<DocumentReference> task) {
                                     try {
                                         loadingDialog.dismiss();
                                         if (task.isSuccessful()){
                                             new CustomToast(requireActivity(),"Sent");
                                         }else {
                                             new CustomToast(requireActivity(),task.getException().getMessage());
                                         }
                                     }catch (Exception e){
                                         e.printStackTrace();
                                     }
                                 }
                             });
                         }
                     }).show(getChildFragmentManager(),"Interest");
            }

            @Override
            public void onHearClick(OtherProduct otherProduct, int position) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AllFavoritItemsDeo allFavoritItemsDeo=AllDataBaseConstant.getInstance(requireContext()).allFavoritItemsDeo();
                        if (allFavoritItemsDeo.CheckOnce(otherProduct.baseKey).size()>0){
                              allFavoritItemsDeo.deleteOne(otherProduct.baseKey);
                        }else {
                            allFavoritItemsDeo.insert(new Gson().fromJson(new Gson().toJson(otherProduct),MyFavoruitProduct.class));
                        }
                    }
                });
            }

            @Override
            public void onUserAddClicked(OtherProduct otherProduct, int position) {

            }

            @Override
            public void ShowSignIn() {
                new MustSignIn(new MustSignIn.Buttons() {
                    @Override
                    public void SelectedSign() {
                        startActivity(new Intent(requireContext(), MainActivity.class));
                        requireActivity().finishAffinity();
                    }
                }).show(getChildFragmentManager(),"SignIn");
            }
        });

        AllDataBaseConstant.getInstance(requireContext()).allRadius().getLiveAll().observeForever(new Observer<List<RadiusAndCategory>>() {
            @Override
            public void onChanged(List<RadiusAndCategory> radiusAndCategories) {
                if (radiusAndCategories.size()==0){
                    return;
                }
                if (listLiveData!=null&& observer!=null){
                    listLiveData.removeObserver(observer);
                }
                SupportSQLiteQuery query = new SimpleSQLiteQuery(Utilities.QuerryString(radiusAndCategories.get(0),1), new Object[]{});
                listLiveData=AllDataBaseConstant.getInstance(requireContext()).allOtherItemsDeo().getProduct(query);
                listLiveData.observeForever(observer);
            }
        });
    }
}