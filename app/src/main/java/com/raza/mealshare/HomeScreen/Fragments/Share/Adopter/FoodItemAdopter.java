package com.raza.mealshare.HomeScreen.Fragments.Share.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raza.mealshare.Database.AllProductsFills.MyProduct;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.ImageAndCard;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

;


public class FoodItemAdopter extends RecyclerView.Adapter<FoodItemAdopter.ViewHolder> {

    private ArrayList<MyProduct> postedItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public FoodItemAdopter(Context context, ArrayList<MyProduct> postedItems) {
        this.mInflater   = LayoutInflater.from(context);
        this.postedItems = postedItems;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = mInflater.inflate(R.layout.recycle_food_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MyProduct postedItem= this.postedItems.get(position);
        holder.title.setText(postedItem.getItemName());
        holder.Description.setText("\t"+postedItem.getItemDescription());
        holder.Condition.setText(postedItem.getCondition());
        if (postedItem.getAllImages()!=null){
            for (int i=0;i<postedItem.getAllImages().size();i++){
                holder.Cards.get(i).ShowImage(postedItem.getAllImages().get(i).getData_thumbnail_storage_path(), mContext, new ImageAndCard.Failed() {
                    @Override
                    public void reload() {
                        notifyDataSetChanged();
                    }
                });
                int finalI = i;
                holder.Cards.get(i).getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.OnImageClick(postedItem.getAllImages().get(finalI));
                    }
                });
            }
        }
        Calendar time = Calendar.getInstance();
        time.setTime(postedItem.getData_submission_time());
        holder.postTime.setText(Utilities.PostTime(time));
        holder.itemView.findViewById(R.id.mainCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener!=null){
                    mClickListener.onItemClick(postedItem);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return postedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private TextView title,Condition,Description,postTime;
       private ArrayList<ImageAndCard> Cards=new ArrayList<>();
       private ConstraintLayout imageOneCard,imageTwoCard,imageThreeCard;
       private ImageView ItemImageOne,ItemImageTwo,ItemImageThree;
        ViewHolder(View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            postTime=itemView.findViewById(R.id.postTime);
            Condition=itemView.findViewById(R.id.Condition);
            Description=itemView.findViewById(R.id.description);
            imageOneCard=itemView.findViewById(R.id.pickImageOne);
            imageTwoCard=itemView.findViewById(R.id.pickImageTwo);
            imageThreeCard=itemView.findViewById(R.id.pickImageThree);
            ItemImageOne=itemView.findViewById(R.id.ItemImageOne);
            ItemImageTwo=itemView.findViewById(R.id.ItemImageTwo);
            ItemImageThree=itemView.findViewById(R.id.ItemImageThree);
            Cards.add(new ImageAndCard(imageOneCard,ItemImageOne));
            Cards.add(new ImageAndCard(imageTwoCard,ItemImageTwo));
            Cards.add(new ImageAndCard(imageThreeCard,ItemImageThree));
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(MyProduct postedItems);
        void OnImageClick(All_Images all_images);
    }
    public void insertItems(MyProduct category){
        this.postedItems.add(category);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(MyProduct category){
         this.postedItems.add(0,category);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= postedItems.size();
        postedItems.clear();
        notifyItemRangeRemoved(0,size);
    }
    public void UpdateAll(List<MyProduct> myProducts){
        postedItems.clear();
        postedItems.addAll(myProducts);
        notifyDataSetChanged();
    }
    public MyProduct getItem(int position){
        return postedItems.get(position);
    }

}
