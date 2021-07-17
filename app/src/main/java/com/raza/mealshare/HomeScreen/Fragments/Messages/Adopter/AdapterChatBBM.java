package com.raza.mealshare.HomeScreen.Fragments.Messages.Adopter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.MessagesFiles.Messages;
import com.raza.mealshare.Database.MessagesFiles.UserDetails;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.TimeUtilities;
import com.raza.mealshare.Utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterChatBBM extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int CHAT_ME = 100;
    private final int CHAT_YOU = 200;
    private String youName="A";
    private String meName="A";

    private List<Messages> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Messages obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterChatBBM(Activity activity, Context context, @NotNull Messages conversionsMessage) {
        ctx = context;
        AllDataBaseConstant.getInstance(context).allUsersDeo().findUserByUid(conversionsMessage.ConversationsId).observeForever(new Observer<List<UserDetails>>() {
            @Override
            public void onChanged(List<UserDetails> userDetails) {
                try {
                    if (userDetails.size()>0){
                        youName=String.valueOf(userDetails.get(0).user_name.charAt(0));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
       try {
           meName=String.valueOf(Utilities.UserProfile(activity).getUser_name().charAt(0));
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView text_content;
        public TextView text_time,name_first;
        public View lyt_parent;

        public ItemViewHolder(View v) {
            super(v);
            text_content = v.findViewById(R.id.text_content);
            text_time = v.findViewById(R.id.text_time);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            name_first=v.findViewById(R.id.name_first);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == CHAT_ME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bbm_me, parent, false);
            vh = new ItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bbm_you, parent, false);
            vh = new ItemViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final Messages m = items.get(position);
            ItemViewHolder vItem = (ItemViewHolder) holder;
            vItem.text_content.setText(m.getContent());
            vItem.text_time.setText(TimeUtilities.GetUpdateTime(m.GetTimestamp()));
            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, m, position);
                    }
                }
            });
           if (m.senderId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
               vItem.name_first.setText(meName);
           }else {
               vItem.name_first.setText(youName);
           }
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).senderId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return CHAT_ME;
        }else {
           return CHAT_YOU;
        }
    }

    public void insertItem(Messages item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
    public void UpdateAll(List<Messages> myProducts){
        items.clear();
        items.addAll(myProducts);
        notifyDataSetChanged();
    }
    public void setItems(List<Messages> items) {
        this.items = items;
    }
}