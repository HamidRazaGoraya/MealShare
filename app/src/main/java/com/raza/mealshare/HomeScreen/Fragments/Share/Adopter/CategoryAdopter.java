package com.raza.mealshare.HomeScreen.Fragments.Share.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;


public class CategoryAdopter extends RecyclerView.Adapter<CategoryAdopter.ViewHolder> {

    private List<Category> categoryArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public CategoryAdopter(Context context, List<Category> categoryArrayList) {
        this.mInflater   = LayoutInflater.from(context);
        this.categoryArrayList = categoryArrayList;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = mInflater.inflate(R.layout.recycle_category, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Category category= categoryArrayList.get(position);
        holder.title.setText(category.getName());
        Glide.with(mContext).load(Utilities.StorageReference(category.getIcon())).into(holder.nextOption);
        holder.itemView.findViewById(R.id.mainCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener!=null){
                    mClickListener.onItemClick(category);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

       private TextView title;
       private ImageView nextOption;
        ViewHolder(View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            nextOption=itemView.findViewById(R.id.nextOption);
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(Category category);
    }
    public void insertItems(Category category){
        this.categoryArrayList.add(category);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(Category category){
         this.categoryArrayList.add(0,category);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= categoryArrayList.size();
        categoryArrayList.clear();
        notifyItemRangeRemoved(0,size);
    }
    public Category getItem(int position){
        return categoryArrayList.get(position);
    }

}
