package com.raza.mealshare.HomeScreen.Fragments.Share.Results.Adopter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.ImageAndCard;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.RecycleFoodItemBinding;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;


public class FoodItemAdopterWithOptions extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostedItems> postedItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public FoodItemAdopterWithOptions(Context context, ArrayList<PostedItems> postedItems) {
        this.mInflater   = LayoutInflater.from(context);
        this.postedItems = postedItems;
        mContext=context;
    }

    @NonNull
    @Override
    public ItemValues onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemValues(RecycleFoodItemBinding.inflate(mInflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FillValues((ItemValues) holder,postedItems.get(position),position);
    }

    private void FillValues(ItemValues holder, PostedItems postedItem, int position) {
        holder.binding.title.setText(postedItem.getItemName());
        holder.binding.description.setText("\t"+postedItem.getItemDescription());
        holder.binding.Condition.setText(postedItem.getCondition());
        if (postedItem.getAll_Images()!=null){
            for (int i=0;i<postedItem.getAll_Images().size();i++){
                holder.Cards.get(i).ShowImage(postedItem.getAll_Images().get(i).getData_thumbnail_storage_path(), mContext, new ImageAndCard.Failed() {
                    @Override
                    public void reload() {
                        notifyDataSetChanged();
                    }
                });
                int finalI = i;
                holder.Cards.get(i).getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.OnImageClick(postedItem.getAll_Images().get(finalI));
                    }
                });
            }
        }
        Calendar time = Calendar.getInstance();
        time.setTime(postedItem.getData_submission_time());
        holder.binding.postTime.setText(Utilities.PostTime(time));
        holder.itemView.findViewById(R.id.mainCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.results.getVisibility()==View.VISIBLE){
                    holder.binding.results.setVisibility(View.GONE);
                }else {
                    holder.binding.results.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mClickListener.OnDeleteClick(postedItem);
            }
        });
        holder.binding.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.OnMarkCompleteClicked(postedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postedItems.size();
    }

    public static class ItemValues extends RecyclerView.ViewHolder {
       private ArrayList<ImageAndCard> Cards=new ArrayList<>();
       private RecycleFoodItemBinding binding;
        ItemValues(RecycleFoodItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            Cards.add(new ImageAndCard(binding.pickImageOne,binding.ItemImageOne));
            Cards.add(new ImageAndCard(binding.pickImageTwo,binding.ItemImageTwo));
            Cards.add(new ImageAndCard(binding.pickImageThree,binding.ItemImageThree));
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(PostedItems postedItems);
        void OnImageClick(All_Images all_images);
        void OnDeleteClick(PostedItems postedItems);
        void OnMarkCompleteClicked(PostedItems postedItems);
    }
    public void insertItems(PostedItems category){
        this.postedItems.add(category);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(PostedItems category){
         this.postedItems.add(0,category);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= postedItems.size();
        postedItems.clear();
        notifyItemRangeRemoved(0,size);
    }
    public PostedItems getItem(int position){
        return postedItems.get(position);
    }

}
