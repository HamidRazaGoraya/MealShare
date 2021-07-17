package com.raza.mealshare.HomeScreen.Fragments.Messages;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.AllProductsFills.OtherProduct;
import com.raza.mealshare.Database.MessagesFiles.Messages;
import com.raza.mealshare.Database.MessagesFiles.UserDetails;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Messages.Adopter.AdapterChatBBM;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Tools;
import com.raza.mealshare.databinding.ActivityChatBbmBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

public class ChatBBM extends AppCompatActivity {

    private View btn_send;
    private EditText et_content;
    private AdapterChatBBM adapter;
    private RecyclerView recycler_view;
    private FirebaseRef ref=new FirebaseRef();
    private ActionBar actionBar;
    private Messages conversionsMessage;
    private ActivityChatBbmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBbmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        conversionsMessage=new Gson().fromJson(getIntent().getStringExtra("messages"),Messages.class);
        initToolbar();
        iniComponent();
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        AllDataBaseConstant.getInstance(this).allUsersDeo().findUserByUid(conversionsMessage.ConversationsId).observeForever(new Observer<List<UserDetails>>() {
            @Override
            public void onChanged(List<UserDetails> userDetails) {
                try {
                    if (userDetails.size()>0){
                        actionBar.setTitle(userDetails.get(0).user_name);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Tools.setSystemBarColorInt(this, Color.parseColor("#0A7099"));
        AllDataBaseConstant.getInstance(this).allOtherItemsDeo().loadShopByBaseKey(conversionsMessage.productId).observeForever(new Observer<List<OtherProduct>>() {
            @Override
            public void onChanged(List<OtherProduct> otherProducts) {
                try {
                    if (otherProducts.size()>0){
                        actionBar.setSubtitle(otherProducts.get(0).itemName);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void iniComponent() {
        recycler_view = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        adapter = new AdapterChatBBM(this,this,conversionsMessage);
        recycler_view.setAdapter(adapter);
        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        String statement = "SELECT * FROM Messages WHERE ConversationsId = '"+conversionsMessage.ConversationsId+"' ORDER BY time ASC";
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        AllDataBaseConstant.getInstance(ChatBBM.this).allMessagesDeo().getMessagesRaw(query).observeForever(new Observer<List<Messages>>() {
            @Override
            public void onChanged(List<Messages> messages) {
                try {
                    adapter.UpdateAll(messages);
                    recycler_view.scrollToPosition(adapter.getItemCount() - 1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendChat() {
        final String msg = et_content.getText().toString();
        String uniqueID = UUID.randomUUID().toString();
        HashMap<String, Object> SendMessage=new HashMap<>();
        SendMessage.put(ref.product_id,conversionsMessage.getProductId());
        SendMessage.put(ref.sender, FirebaseAuth.getInstance().getCurrentUser().getUid());
        SendMessage.put(ref.receiver,conversionsMessage.getConversationsId());
        SendMessage.put(ref.type,1);
        SendMessage.put(ref.time, FieldValue.serverTimestamp());
        SendMessage.put(ref.content,msg);
        FirebaseFirestore.getInstance().collection(ref.users).document(conversionsMessage.ConversationsId).collection(ref.AllFoodShared).document(conversionsMessage.productId).collection(ref.messages).document(uniqueID).set(SendMessage);
        Messages messages=new Messages();
        messages.setMessageId(uniqueID);
        messages.setProductId(conversionsMessage.getProductId());
        messages.setContent(msg);
        messages.setMessageType(1);
        messages.setReceiverId(conversionsMessage.getConversationsId());
        messages.setSenderId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        messages.setConversationsId(conversionsMessage.getConversationsId());
        messages.setTime(Calendar.getInstance().getTimeInMillis());
        messages.setReadMessages(1);
        adapter.insertItem(messages);
        et_content.setText("");
        recycler_view.scrollToPosition(adapter.getItemCount() - 1);

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setEnabled(false);
            } else {
                btn_send.setEnabled(true);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}