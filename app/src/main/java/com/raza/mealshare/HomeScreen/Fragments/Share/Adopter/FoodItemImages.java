package com.raza.mealshare.HomeScreen.Fragments.Share.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.SeperatedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.AllFoodImagesBinding;
import com.raza.mealshare.databinding.FullScreenFoodBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;


public class FoodItemImages extends RecyclerView.Adapter<FoodItemImages.ViewHolder> {

    private List<All_Images> all_images;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public FoodItemImages(Context context, List<All_Images> all_images) {
        this.mInflater   = LayoutInflater.from(context);
        this.all_images = all_images;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AllFoodImagesBinding.inflate(mInflater,parent,false));
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        All_Images all_images= this.all_images.get(position);
        Log.i("loadImage","006");
        Log.i("loadImage",all_images.getData_fullimage_storage_path());
        Glide.with(mContext).load(Utilities.StorageReference(all_images.getData_fullimage_storage_path())).into(holder.binding.foodImage);
    }
    @Override
    public int getItemCount() {
        return all_images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private AllFoodImagesBinding binding;
        ViewHolder(AllFoodImagesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(SeperatedItems postedItems);
    }
    public void insertItems(All_Images category){
        this.all_images.add(category);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(All_Images category){
         this.all_images.add(0,category);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= all_images.size();
        all_images.clear();
        notifyItemRangeRemoved(0,size);
    }
    public All_Images getItem(int position){
        return all_images.get(position);
    }

}
