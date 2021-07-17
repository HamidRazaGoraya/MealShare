package com.raza.mealshare.HomeScreen.Fragments.Share.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.AllProductsFills.MyFavoruitProduct;
import com.raza.mealshare.Database.AllProductsFills.MyProduct;
import com.raza.mealshare.Database.AllProductsFills.OtherProduct;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.Models.SeperatedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FullScreenFoodBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

;


public class FoodICategoryAdopter extends RecyclerView.Adapter<FoodICategoryAdopter.ViewHolder> {

    private ArrayList<OtherProduct> postedItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public FoodICategoryAdopter(Context context, ArrayList<OtherProduct> postedItems) {
        this.mInflater   = LayoutInflater.from(context);
        this.postedItems = postedItems;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FullScreenFoodBinding.inflate(mInflater,parent,false));
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        OtherProduct postedItem= this.postedItems.get(position);
        List<All_Images> postedImages=postedItem.getAllImages();
        if (postedImages!=null){
            if (postedImages.size()>0){
                Glide.with(mContext).load(Utilities.StorageReference(postedImages.get(0).getData_fullimage_storage_path())).into(holder.binding.foodImage);
            }
        }
        Log.i("loadImage","007");
        Glide.with(mContext).load(Utilities.StorageReference(postedItem.getCategory().getIcon())).into(holder.binding.categorayImage);
        Glide.with(mContext).load(Utilities.StorageReference(postedItem.getUserInfo().getUser_profile_pic())).placeholder(R.drawable.ic_baseline_perm_identity_24).into(holder.binding.userImage);
        holder.binding.itemName.setText(postedItem.getItemName());
        holder.binding.itemDescription.setText(postedItem.getItemDescription());
        holder.binding.connectAndGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    mClickListener.ShowSignIn();
                }else {
                    mClickListener.onConnectClicked(postedItem);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(postedItem);
            }
        });
        holder.binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    mClickListener.ShowSignIn();
                }else {
                    mClickListener.onUserAddClicked(postedItem,position);
                }

            }
        });
        holder.binding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    mClickListener.ShowSignIn();
                }else {
                    mClickListener.onHearClick(postedItem,position);
                }

            }
        });
        AllDataBaseConstant.getInstance(mContext).allFavoritItemsDeo().CheckIfThisFavor(postedItem.baseKey).observeForever(new Observer<List<MyFavoruitProduct>>() {
            @Override
            public void onChanged(List<MyFavoruitProduct> myFavoruitProducts) {
                if (myFavoruitProducts.size()>0){
                    holder.binding.heart.setImageDrawable(mContext.getDrawable(R.drawable.ic_heart_filled));
                }else {
                    holder.binding.heart.setImageDrawable(mContext.getDrawable(R.drawable.ic_heart_not_filled));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return postedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private FullScreenFoodBinding binding;
        ViewHolder(FullScreenFoodBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(OtherProduct postedItems);
        void onConnectClicked(OtherProduct postedItems);
        void onHearClick(OtherProduct otherProduct,int position);
        void onUserAddClicked(OtherProduct otherProduct,int position);
        void ShowSignIn();
    }
    public void insertItems(OtherProduct category){
        this.postedItems.add(category);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(OtherProduct category){
         this.postedItems.add(0,category);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= postedItems.size();
        postedItems.clear();
        notifyItemRangeRemoved(0,size);
    }
    public OtherProduct getItem(int position){
        return postedItems.get(position);
    }
    public void UpdateAll(List<OtherProduct> myProducts){
        postedItems.clear();
        postedItems.addAll(myProducts);
        notifyDataSetChanged();
    }
}
