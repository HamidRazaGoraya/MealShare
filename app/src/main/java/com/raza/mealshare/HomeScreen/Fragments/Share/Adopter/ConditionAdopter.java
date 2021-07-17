package com.raza.mealshare.HomeScreen.Fragments.Share.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raza.mealshare.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;


public class ConditionAdopter extends RecyclerView.Adapter<ConditionAdopter.ViewHolder> {

    private List<String> conditionS;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public ConditionAdopter(Context context, List<String> conditionS) {
        this.mInflater   = LayoutInflater.from(context);
        this.conditionS = conditionS;
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
        final String condition= conditionS.get(position);
        holder.title.setText(condition);
        holder.itemView.findViewById(R.id.mainCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener!=null){
                    mClickListener.onItemClick(condition);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return conditionS.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

       private TextView title;
        ViewHolder(View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(String condition);
    }
    public void insertItems(String condition){
        this.conditionS.add(condition);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(String condition){
         this.conditionS.add(0,condition);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= conditionS.size();
        conditionS.clear();
        notifyItemRangeRemoved(0,size);
    }
    public String getItem(int position){
        return conditionS.get(position);
    }

}
