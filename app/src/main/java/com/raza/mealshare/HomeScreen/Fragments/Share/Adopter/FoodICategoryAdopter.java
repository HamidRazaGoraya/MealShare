package com.raza.mealshare.HomeScreen.Fragments.Share.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.raza.mealshare.Models.SeperatedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;


public class FoodICategoryAdopter extends RecyclerView.Adapter<FoodICategoryAdopter.ViewHolder> {

    private ArrayList<SeperatedItems> seperatedItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public FoodICategoryAdopter(Context context, ArrayList<SeperatedItems> seperatedItems) {
        this.mInflater   = LayoutInflater.from(context);
        this.seperatedItems = seperatedItems;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = mInflater.inflate(R.layout.recycle_food_category, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SeperatedItems seperatedItems= this.seperatedItems.get(position);
        holder.CategoryName.setText(seperatedItems.getCategory().getName());
        Glide.with(mContext).load(Utilities.StorageReference(seperatedItems.getCategory().getIcon())).into(holder.CategoryIcon);
        holder.Counters.setText(String.valueOf(seperatedItems.getPostedItems().size())+" Items found");
        holder.itemView.findViewById(R.id.mainCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener!=null){
                    mClickListener.onItemClick(seperatedItems);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return seperatedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private TextView CategoryName,Counters;
       private ImageView CategoryIcon;
        ViewHolder(View itemView) {
            super(itemView);
            CategoryName =itemView.findViewById(R.id.CategoryName);
            Counters=itemView.findViewById(R.id.PostCounter);
            CategoryIcon=itemView.findViewById(R.id.CategoryIcon);
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(SeperatedItems postedItems);
    }
    public void insertItems(SeperatedItems category){
        this.seperatedItems.add(category);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(SeperatedItems category){
         this.seperatedItems.add(0,category);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= seperatedItems.size();
        seperatedItems.clear();
        notifyItemRangeRemoved(0,size);
    }
    public SeperatedItems getItem(int position){
        return seperatedItems.get(position);
    }

}
