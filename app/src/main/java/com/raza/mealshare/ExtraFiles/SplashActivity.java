package com.raza.mealshare.ExtraFiles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.raza.mealshare.HomeScreen.Home;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainThem);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                try {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, Home.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
