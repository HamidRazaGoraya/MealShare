package com.raza.mealshare.HomeScreen.Fragments.Messages;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.raza.mealshare.CustomDialogs.ShowImage;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.MessagesFiles.AllUsersDeo;
import com.raza.mealshare.Database.MessagesFiles.Messages;
import com.raza.mealshare.Database.MessagesFiles.UserDetails;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Messages.Adopter.HomeMessagesAdopter;
import com.raza.mealshare.Models.UserInfo;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.FragmentMessagesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    private FragmentMessagesBinding binding;
    private HomeMessagesAdopter adopter;
    private FirebaseRef ref=new FirebaseRef();
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentMessagesBinding.inflate(inflater,container,false);
        SetupAllMessages();
        MessagesTest();
        return binding.getRoot();
    }

    private void MessagesTest() {
        AllDataBaseConstant.getInstance(requireContext()).allMessagesDeo().AllMessages().observeForever(new Observer<List<Messages>>() {
            @Override
            public void onChanged(List<Messages> messages) {
                Log.i("messagesTest","001   :" + messages.size());
                for (int i=0;i<messages.size();i++){
                    Log.i("messagesTest","002   :" + messages.get(i).content);
                }
            }
        });
    }

    private void SetupAllMessages() {
        binding.allMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        adopter=new HomeMessagesAdopter(requireContext(),new ArrayList<>());
        binding.allMessages.setAdapter(adopter);
        String statement = "SELECT DISTINCT ConversationsId  FROM Messages ORDER BY time DESC";
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        AllDataBaseConstant.getInstance(requireContext()).allMessagesDeo().getMessagesRaw(query).observeForever(new Observer<List<Messages>>() {
            @Override
            public void onChanged(List<Messages> messages) {
                Log.i("messagesTest","003   :" + messages.size());
                adopter.deleteAllItems();
                for (int i=0;i<messages.size();i++){
                    Log.i("messagesTest","004   :" + messages.get(i).getContent()+ " :" +messages.get(i).ConversationsId);
                    int finalI = i;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                         if (getContext()!=null){
                             List<Messages>  singleMessage=  AllDataBaseConstant.getInstance(requireContext()).allMessagesDeo().getLastMessByConversationId(messages.get(finalI).ConversationsId);
                             if (singleMessage.size()>0){
                                 requireActivity().runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         adopter.insertItems(singleMessage.get(0));
                                     }
                                 });
                             }
                         }
                        }
                    });
                }

                if (messages.size()>0){
                    binding.emptyList.setVisibility(View.GONE);
                }else {
                    binding.emptyList.setVisibility(View.VISIBLE);
                }
            }
        });
        adopter.setClickListener(new HomeMessagesAdopter.ItemClickListener() {
            @Override
            public void onMessageClick(Messages messages) {
               startActivity(new Intent(requireContext(),ChatBBM.class).putExtra("messages",new Gson().toJson(messages)));
            }

            @Override
            public void onProfileClick(UserDetails userDetails) {
                new ShowImage(userDetails.user_profile_pic).show(getChildFragmentManager(),"Image");
            }

            @Override
            public void DownloadProfile(String userId) {
                if (getContext()!=null){
                    Utilities.DownloadProfile(userId,requireContext());
                }
            }
        });
    }

}