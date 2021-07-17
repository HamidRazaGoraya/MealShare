package com.raza.mealshare.HomeScreen.Fragments.Messages.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.MessagesFiles.Messages;
import com.raza.mealshare.Database.MessagesFiles.UserDetails;
import com.raza.mealshare.Models.UserInfo;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.TimeUtilities;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FullScreenFoodBinding;
import com.raza.mealshare.databinding.RecycleMessagesHomeBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

;


public class HomeMessagesAdopter extends RecyclerView.Adapter<HomeMessagesAdopter.ViewHolder> {

    private ArrayList<Messages> messagesArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public HomeMessagesAdopter(Context context, ArrayList<Messages> messagesArrayList) {
        this.mInflater   = LayoutInflater.from(context);
        this.messagesArrayList = messagesArrayList;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecycleMessagesHomeBinding.inflate(mInflater,parent,false));
    }

    // binds the data to the TextView in each row
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Messages postedItem= this.messagesArrayList.get(position);
        holder.binding.time.setText(TimeUtilities.GetUpdateTime(postedItem.GetTimestamp()));
        holder.binding.userContent.setText(postedItem.content);
        try {
            AllDataBaseConstant.getInstance(mContext).allUsersDeo().findUserByUid(postedItem.ConversationsId).observeForever(new Observer<List<UserDetails>>() {
                @Override
                public void onChanged(List<UserDetails> userDetails) {
                    if (userDetails.size()==0){
                        mClickListener.DownloadProfile(postedItem.ConversationsId);
                    }else {
                        holder.binding.userName.setText(userDetails.get(0).user_name);
                        if (userDetails.get(0).user_profile_pic!=null){
                                Glide.with(mContext).load(Utilities.StorageReference(userDetails.get(0).user_profile_pic)).placeholder(mContext.getResources().getDrawable(R.drawable.profile_selected)).into(holder.binding.userImage);
                                holder.binding.userImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!TextUtils.isEmpty(userDetails.get(0).user_profile_pic)){
                                            mClickListener.onProfileClick(userDetails.get(0));
                                        }
                                    }
                                });

                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            String statement = "SELECT * FROM Messages WHERE ConversationsId = '"+postedItem.ConversationsId+"' AND readMessages = 0";
            SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
            AllDataBaseConstant.getInstance(mContext).allMessagesDeo().getMessagesRaw(query).observeForever(new Observer<List<Messages>>() {
                @Override
                public void onChanged(List<Messages> messages) {
                    if (messages.size()>0){
                        holder.binding.newMessages.setText(String.valueOf(messages.size()));
                        holder.binding.newMessages.setVisibility(View.VISIBLE);
                    }else {
                        holder.binding.newMessages.setVisibility(View.GONE);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onMessageClick(postedItem);
            }
        });
    }
    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private RecycleMessagesHomeBinding binding;
        ViewHolder(RecycleMessagesHomeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }


    }

   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onMessageClick(Messages messages);
        void onProfileClick(UserDetails userInfo);
        void DownloadProfile(String userId);
    }
    public void insertItems(Messages messages){
        this.messagesArrayList.add(messages);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }



    public void insertAtTop(Messages messages){
         this.messagesArrayList.add(0,messages);
            notifyItemInserted(0);
            notifyDataSetChanged();
    }
    public void deleteAllItems(){
        int size= messagesArrayList.size();
        messagesArrayList.clear();
        notifyItemRangeRemoved(0,size);
    }
    public Messages getItem(int position){
        return messagesArrayList.get(position);
    }
    public void UpdateAll(List<Messages> myProducts){
        messagesArrayList.clear();
        messagesArrayList.addAll(myProducts);
        notifyDataSetChanged();
    }
}
