package com.raza.mealshare.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.raza.mealshare.ExtraFiles.FirebaseRef;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat GetFormatHHMMSS() {
        return new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
    }

    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat GetFileNameFormat() {
        return new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss",Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat AudioFileName() {
        return new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss",Locale.getDefault());
    }
    @NotNull
    public static String GetName() {
        return GetFileNameFormat().format(Calendar.getInstance().getTime());
    }

    @NotNull
    public static String GetTimeHHMMSS() {
        return GetFormatHHMMSS().format(Calendar.getInstance().getTime());
    }


    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat ReviewDateFormat() {
        return new SimpleDateFormat("dd MMM ",Locale.getDefault());
    }

    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat getDayNameFormat() {
        return new SimpleDateFormat("EEE",Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("dd MMM yy",Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat getDayNumberFormat() {
        return new SimpleDateFormat("dd",Locale.getDefault());
    }

    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat getYearFormat() {
        return new SimpleDateFormat("dd MMM ", Locale.getDefault());
    }

    @NotNull
    public static String getYear(@NotNull Calendar calendar) {
        return getYearFormat().format(calendar.getTime());
    }

    @NotNull
    public static String getDayNumber(@NotNull Calendar calendar) {
        return getDayNumberFormat().format(calendar.getTime());
    }

    @NotNull
    public static String getDayNameText(@NotNull Calendar calendar) {
        return getDayNameFormat().format(calendar.getTime());
    }
    @NotNull
    public static String GetProcessingDatePayment(Date date, int number_of_days){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+number_of_days);
        return "Estimate processing date \n"+getYear(calendar);
    }
    @NotNull
    public static String GetReviewTimeString(Date date,int number_of_days) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+number_of_days);
        return "Review By: "+ReviewDateFormat().format(calendar.getTime());
    }
    public static SharedPreferences getSharedPreferences(@NotNull Activity activity){
        return activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }
    @NotNull
    public static StorageReference StorageReference(String path){
        return FirebaseStorage.getInstance().getReference().child(path);
    }
    public static int GetSearchRadius(Activity activity){
        SharedPreferences sharedPreferences=getSharedPreferences(activity);
        FirebaseRef ref=new FirebaseRef();
        if (sharedPreferences.contains(ref.SearchRadius)){
            return sharedPreferences.getInt(ref.SearchRadius,25);
        }
        return 25;
    }
    public static String PostTime(Calendar date){
        Calendar currentTime=Calendar.getInstance();
        if (currentTime.get(Calendar.DAY_OF_YEAR)==date.get(Calendar.DAY_OF_YEAR) && currentTime.get(Calendar.YEAR)==date.get(Calendar.YEAR)){
            return "Today \n"+GetFormatHHMMSS().format(date.getTime());
        }else if (currentTime.get(Calendar.DAY_OF_YEAR)==date.get(Calendar.DAY_OF_YEAR)+1 && currentTime.get(Calendar.YEAR)==date.get(Calendar.YEAR)){
            return "Yesterday \n"+GetFormatHHMMSS().format(date.getTime());
        }else {
            return new SimpleDateFormat("dd MMM").format(date.getTime())+"\n"+new SimpleDateFormat("HH:mm").format(date.getTime());
        }
    }
}
