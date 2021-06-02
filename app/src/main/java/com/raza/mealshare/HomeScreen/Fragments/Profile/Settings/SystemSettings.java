package com.raza.mealshare.HomeScreen.Fragments.Profile.Settings;

import android.os.Bundle;
import android.view.View;

import com.raza.mealshare.HomeScreen.Fragments.Profile.Adopter.ProfileButtonsAdopter;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileButtonsModel;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SystemSettings extends AppCompatActivity {
private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        loadingDialog=new LoadingDialog(this,R.style.DialogLoadingTheme);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        SetUpRecycleView();
    }

    private void SetUpRecycleView() {
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final RecyclerView recyclerView=findViewById(R.id.allButtons);
        recyclerView.setLayoutManager(new LinearLayoutManager(SystemSettings.this));
        final ProfileButtonsAdopter profileButtonsAdopter=new ProfileButtonsAdopter(SystemSettings.this,getAllTitles());
        recyclerView.setAdapter(profileButtonsAdopter);
        profileButtonsAdopter.setClickListener(new ProfileButtonsAdopter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String id) {

            }

            @Override
            public void ChangeNotification(String value) {

            }
        });
    }
    private void  dismissLoading(){
        try {
            loadingDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void  showLoading(){
        try {
            loadingDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @NotNull
    private ArrayList<ProfileButtonsModel> getAllTitles() {
        ArrayList<ProfileButtonsModel> arrayList=new ArrayList<>();
        arrayList.add(new ProfileButtonsModel("修改密碼","1"));
        arrayList.add(new ProfileButtonsModel("通知設定","2"));
        arrayList.add(new ProfileButtonsModel("免責聲明","3"));
        arrayList.add(new ProfileButtonsModel("私隱","4"));
        arrayList.add(new ProfileButtonsModel("投訴","5"));
        arrayList.add(new ProfileButtonsModel("版本資訊","6"));
        arrayList.add(new ProfileButtonsModel("帳戶註銷","7"));
        return arrayList;
    }
}