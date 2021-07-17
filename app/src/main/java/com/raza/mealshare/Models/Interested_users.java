package com.raza.mealshare.Models;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interested_users {
    @Expose
    @SerializedName("user_uid")
    private String user_uid;
    @Expose
    @SerializedName("user_profile_pic")
    private String user_profile_pic;
    @Expose
    @SerializedName("user_name")
    private String user_name;
    @Expose
    @SerializedName("user_location")
    private GeoPoint user_location;

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public GeoPoint getUser_location() {
        return user_location;
    }

    public void setUser_location(GeoPoint user_location) {
        this.user_location = user_location;
    }
}
