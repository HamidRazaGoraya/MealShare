package com.raza.mealshare.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.AllProductsFills.TrackAllItems;
import com.raza.mealshare.Database.MessagesFiles.AllUsersDeo;
import com.raza.mealshare.Database.MessagesFiles.UserDetails;
import com.raza.mealshare.Database.RadiusFiles.AllRadius;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.HomeScreen.Fragments.Profile.Model.ProfileInfo;
import com.raza.mealshare.Location.PickUpLocation;
import com.raza.mealshare.Models.UserInfo;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

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
        if (TextUtils.isEmpty(path)){
            return FirebaseStorage.getInstance().getReference("Asd");
        }
        return FirebaseStorage.getInstance().getReference().child(path);
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

    public static boolean DoesLocationAvailable(Activity context){
        return getSharedPreferences(context).contains("Location");
    }
    public static void SaveLocation(Activity context, LatLng location){
        getSharedPreferences(context).edit().putString("Location",String.valueOf(location.latitude)+","+String.valueOf(location.longitude)).apply();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AllRadius radius= AllDataBaseConstant.getInstance(context).allRadius();
                List<RadiusAndCategory> radiusAndCategories=radius.getAllOnce();
                if (radiusAndCategories.size()==0){
                    radius.insert(new RadiusAndCategory("ALL",1,6000));
                }
            }
        });
        if (Utilities.DoesUserProfileAvailable(context)|| Utilities.DoesLocationAvailable(context)){
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                TrackAllItems.GetAllMyItems(context,FirebaseAuth.getInstance().getCurrentUser());
            }
            TrackAllItems.GetAllOtherItems(context);
        }
    }
    public static GeoPoint GetLatLog(Activity activity){
        if (DoesUserProfileAvailable(activity)){
            if (UserProfile(activity).getUser_location()!=null){
                return UserProfile(activity).getUser_location();
            }else if (DoesLocationAvailable(activity)){
                String location=getSharedPreferences(activity).getString("Location","");
                return new GeoPoint(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
            }else {
                return null;
            }
        }else {
            if (DoesLocationAvailable(activity)){
                String location=getSharedPreferences(activity).getString("Location","");
                return new GeoPoint(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
            }else {
                return null;
            }

        }
    }

    public static ProfileInfo UserProfile(Activity activity){
        return new Gson().fromJson(getSharedPreferences(activity).getString("ProfileInfo",""),ProfileInfo.class);
    }
    public static boolean DoesUserProfileAvailable(Activity context){
        return getSharedPreferences(context).contains("ProfileInfo");
    }
    public static void SaveProfile(Activity activity,ProfileInfo profileInfo){
         getSharedPreferences(activity).edit().putString("ProfileInfo",new Gson().toJson(profileInfo)).apply();
    }

    public static int getDistance(@NonNull GeoPoint itemLocation, GeoPoint userLocation) {
        int inMeter=((int) GeoPointToLocation(itemLocation).distanceTo(GeoPointToLocation(userLocation)));

        int inKM=0;
        if (inMeter!=0){
             inKM=inMeter/1000;
        }
        Log.i("Distance","meter :" +inMeter+"   "+inKM);
        return inKM;
    }
    private static Location GeoPointToLocation(GeoPoint geoPoint){
        Location location=  new Location("GPS");
        location.setLatitude(geoPoint.getLatitude());
        location.setLongitude(geoPoint.getLongitude());
        return location;
    }
    public static String QuerryString(RadiusAndCategory andCategory,int type){
        String myString="SELECT * FROM OtherProduct WHERE ";
        if (!andCategory.Category.equals("ALL")){
             myString=myString+" CategoryId = "+andCategory.getCategoryId()+" AND";
        }
        myString=myString+" Distance < "+andCategory.getRadius()+" AND Share = "+type+" ORDER BY data_submission_time DESC";
        Log.i("Querry",myString);
        return myString;
    }

    public static void DownloadProfile(String userId,Context context){
        FirebaseRef ref=new FirebaseRef();
        FirebaseFirestore.getInstance().collection(ref.users).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    try {
                        UserInfo userInfo=task.getResult().toObject(UserInfo.class).withId(task.getResult().getId());
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                AllUsersDeo allUsersDeo= AllDataBaseConstant.getInstance(context).allUsersDeo();
                                if (allUsersDeo.checkUser(userInfo.getUser_uid()).size()>0){
                                    allUsersDeo.update(new UserDetails(userInfo));
                                }else {
                                    allUsersDeo.insert(new UserDetails(userInfo));
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
