package com.raza.mealshare.HomeScreen.Fragments.Share.Results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.raza.mealshare.LogIn.IOnBackPressed;
import com.raza.mealshare.R;

public class RequestAndShareResults extends AppCompatActivity {
    public static String Type="type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_and_share_results);
        loadFragment(new AllProducts(),"Allproducts");
    }

    public void loadFragment(Fragment fragment, String tag){
        if (fragment != null) {
            fragment.setArguments(getIntent().getExtras());
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
}