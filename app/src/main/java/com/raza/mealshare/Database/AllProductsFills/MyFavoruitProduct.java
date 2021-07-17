package com.raza.mealshare.Database.AllProductsFills;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.raza.mealshare.Models.All_Images;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.Models.Interested_users;
import com.raza.mealshare.Models.PostedItems;
import com.raza.mealshare.Models.UserInfo;
import com.raza.mealshare.Utilities.Utilities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MyFavoruitProduct")
public class MyFavoruitProduct {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String userLocation;
    public String userInfo;
    public Long data_submission_time;
    public int data_status;
    public int Share;
    public String itemName;
    public String ItemDescription;
    public String ItemDescriptionLong;
    public String Condition;
    public int CategoryId;
    public String Category;
    public String AllImages;
    public String baseKey;
    public int Distance=0;
    public String interested_users;
    public int Favorites=0;
    public MyFavoruitProduct() {
    }

    public GeoPoint getUserLocation() {
        return new GeoPoint(Double.parseDouble(userLocation.split(",")[0]),Double.parseDouble(userLocation.split(",")[1]));
    }

    public UserInfo getUserInfo() {
        return new Gson().fromJson(userInfo,UserInfo.class);
    }

    public boolean getFavorites() {
        return Favorites == 1;
    }

    public void setFavorites(boolean newFavorites) {
        if (newFavorites){
            Favorites=1;
        }else {
            Favorites=0;
        }
    }

    public Date getData_submission_time() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(data_submission_time);
        return calendar.getTime();
    }

    public int getData_status() {
        return data_status;
    }

    public boolean getShare() {
        return Share==1;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getCondition() {
        return Condition;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public Category getCategory() {
        return new Gson().fromJson(Category,Category.class);
    }

    public MyFavoruitProduct(@NotNull PostedItems itemDetails, GeoPoint myLocation) {
        this.ItemDescriptionLong=itemDetails.getItemDescriptionLong();
        this.userLocation=String.valueOf(itemDetails.getUser_location().getLatitude()+","+itemDetails.getUser_location().getLongitude());
        this.userInfo=new Gson().toJson(itemDetails.getUserInfo());
        if (itemDetails.getData_submission_time()!=null){
            this.data_submission_time=itemDetails.getData_submission_time().getTime();
        }else {
            this.data_submission_time=Calendar.getInstance().getTimeInMillis();
        }
        this.data_status=itemDetails.getData_status();
        if (itemDetails.getShare()){
            this.Share=1;
        }else {
            this.Share=0;
        }
        this.itemName=itemDetails.getItemName();
        this.CategoryId=itemDetails.getCategory().getId();
        this.Category=new Gson().toJson(itemDetails.getCategory());
        this.ItemDescription=itemDetails.getItemDescription();
        this.Condition=itemDetails.getCondition();

        JSONArray jsonArray=new JSONArray();
        if (itemDetails.getAll_Images()!=null){
            for (int i=0;i<itemDetails.getAll_Images().size();i++){
                jsonArray.put(new Gson().toJson(itemDetails.getAll_Images().get(i)));
            }
        }

        this.AllImages =jsonArray.toString();
        this.baseKey=itemDetails.getDocumentId();
        if (myLocation!=null){
            Distance= Utilities.getDistance(itemDetails.getUser_location(),myLocation);
        }
        JSONArray interestedUsers = new JSONArray();
        if (itemDetails.getInterested_users()!=null){
            for (int i=0;i<itemDetails.getInterested_users().size();i++){
                interestedUsers.put(new Gson().toJson(itemDetails.getInterested_users().get(i)));
            }
        }
        this.interested_users=interestedUsers.toString();
    }


    public List<All_Images> getAllImages() {
        try {
            JSONArray jsonArray = new JSONArray(AllImages);
            ArrayList<All_Images> allImages=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                allImages.add(new Gson().fromJson(jsonArray.getString(i), All_Images.class));
            }
            return allImages;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Interested_users> getAllInterested() {
        try {
            JSONArray jsonArray = new JSONArray(interested_users);
            ArrayList<Interested_users> allImages=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                allImages.add(new Gson().fromJson(jsonArray.getString(i), Interested_users.class));
            }
            return allImages;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



}
