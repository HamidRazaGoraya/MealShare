package com.raza.mealshare.HomeScreen.Fragments.Profile.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileButtonsModel;
import com.raza.mealshare.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;


public class ProfileButtonsAdopter extends RecyclerView.Adapter<ProfileButtonsAdopter.ViewHolder> {

    private ArrayList<ProfileButtonsModel> profileButtonsModels;
    private ArrayList<String> allIds;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public ProfileButtonsAdopter(Context context, ArrayList<ProfileButtonsModel> profileButtonsModels) {
        this.mInflater   = LayoutInflater.from(context);
        this.profileButtonsModels = profileButtonsModels;
        mContext=context;
        allIds=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = mInflater.inflate(R.layout.recycle_profile_next_button, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ProfileButtonsModel itemModel= profileButtonsModels.get(position);
        holder.title.setText(itemModel.getTitle());
        holder.itemView.findViewById(R.id.mainCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener!=null){
                    mClickListener.onItemClick(view,position,itemModel.getId());
                }
            }
        });
        if (itemModel.getId().equals("6") || itemModel.getId().equals("2")){
             if (itemModel.getId().equals("6")){
                 TextView versionNumber=holder.itemView.findViewById(R.id.secondText);
                 versionNumber.setVisibility(View.VISIBLE);
             }else {
                 holder.itemView.findViewById(R.id.secondText).setVisibility(View.GONE);
                 holder.itemView.findViewById(R.id.nextOption).setVisibility(View.GONE);
                 holder.itemView.findViewById(R.id.yesNo).setVisibility(View.VISIBLE);
                 RadioGroup yesNo=holder.itemView.findViewById(R.id.yesNo);

                 yesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(RadioGroup radioGroup, int i) {
                         RadioButton yesButton=holder.itemView.findViewById(R.id.yessButton);
                         RadioButton noButton=holder.itemView.findViewById(R.id.noButton);
                         if (radioGroup.getCheckedRadioButtonId()==R.id.yessButton){
                             yesButton.setTextColor(mContext.getResources().getColor(R.color.themColorGreen));
                             noButton.setTextColor(mContext.getResources().getColor(R.color.weight));
                             mClickListener.ChangeNotification("Y");
                         }else {
                             yesButton.setTextColor(mContext.getResources().getColor(R.color.weight));
                             noButton.setTextColor(mContext.getResources().getColor(R.color.themColorGreen));
                             mClickListener.ChangeNotification("N");
                         }
                     }
                 });
             }
        }else {
            holder.itemView.findViewById(R.id.secondText).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.nextOption).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.yesNo).setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return profileButtonsModels.size();
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
        void onItemClick(View view, int position,String id);
        void ChangeNotification(String value);
    }
    public void insertItems(ProfileButtonsModel profileButtonsModel){
              if (allIds.contains(profileButtonsModel.getId())){
                  UpdateItem(profileButtonsModel);
              }else {
                  allIds.add(profileButtonsModel.getId());
                  this.profileButtonsModels.add(profileButtonsModel);
                  notifyItemInserted(getItemCount() - 1);
                  notifyDataSetChanged();
              }
    }

    private void UpdateItem(ProfileButtonsModel profileButtonsModel) {
        for (int i = 0; i< profileButtonsModels.size(); i++){
            if (profileButtonsModel.getId().equals(profileButtonsModels.get(i).getId())){
                profileButtonsModels.remove(i);
                profileButtonsModels.add(i,profileButtonsModel);
                notifyItemChanged(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void insertAtTop(ProfileButtonsModel profileButtonsModel){
         this.profileButtonsModels.add(0,profileButtonsModel);
            notifyItemInserted(0);
            notifyDataSetChanged();

    }
    public void deleteAllItems(){
        int size= profileButtonsModels.size();
        profileButtonsModels.clear();
        notifyItemRangeRemoved(0,size);
    }
    public ProfileButtonsModel getItem(int position){
        return profileButtonsModels.get(position);
    }

}
