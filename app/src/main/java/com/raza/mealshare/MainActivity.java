package com.raza.mealshare;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.RadiusFiles.AllRadius;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.SettingsModel;
import com.raza.mealshare.HomeScreen.Home;
import com.raza.mealshare.LogIn.IOnBackPressed;
import com.raza.mealshare.LogIn.SignIn;
import com.raza.mealshare.Utilities.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

public class MainActivity extends AppCompatActivity {
    private FirebaseRef ref=new FirebaseRef();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AllRadius radius=AllDataBaseConstant.getInstance(MainActivity.this).allRadius();
                List<RadiusAndCategory> radiusAndCategories=radius.getAllOnce();
                if (radiusAndCategories.size()==0){
                    radius.insert(new RadiusAndCategory("ALL",1,250));
                }
            }
        });
        loadFragment(new SignIn(),"SignIn");

    }



    public void loadFragment(Fragment fragment, String tag){
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment,tag);
            ft.commit();
        }
    }
    @Override public void onBackPressed() {
        Log.i("test","test1");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            Log.i("test","test2");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseFirestore.getInstance().collection(ref.ApplicationControl).document(ref.basic_controls).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (value.exists()){
                        SettingsModel settingsModel=value.toObject(SettingsModel.class);
                        if (settingsModel!=null){
                            Utilities.getSharedPreferences(MainActivity.this).edit().putString(ref.Settings,new Gson().toJson(settingsModel)).apply();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
