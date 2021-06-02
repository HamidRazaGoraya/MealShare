package com.raza.mealshare.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CheckForPermissions {
    public static void checkForReadAndWrite(Activity activity, Context ApplicationContext, Results results){
        if ((ContextCompat.checkSelfPermission(ApplicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(ApplicationContext,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            results.HavePermission();
        }else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
            results.Requested();
        }
    }
    public interface Results{
        void HavePermission();
        void Requested();
    }

    public static void CheckForLocationPermission(Activity activity, Context ApplicationContext,int RequestCode, Results results){
        if ((ContextCompat.checkSelfPermission(ApplicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(ApplicationContext,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            results.HavePermission();
        }else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    RequestCode);
            results.Requested();
        }
    }

    public static void CheckForCameraPermission(Activity activity, Context ApplicationContext,int RequestCode, Results results){
        if ((ContextCompat.checkSelfPermission(ApplicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            results.HavePermission();
        }else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                    RequestCode);
            results.Requested();
        }
    }
}
