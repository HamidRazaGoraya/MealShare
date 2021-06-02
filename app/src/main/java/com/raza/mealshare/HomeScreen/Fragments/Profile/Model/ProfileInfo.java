package com.raza.mealshare.HomeScreen.Fragments.Profile.Model;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public  class ProfileInfo {
    public ProfileInfo() {
    }

    @Expose
    @SerializedName("user_profile_pic")
    private String user_profile_pic;
    @Expose
    @SerializedName("user_name")
    private String user_name;
    @Expose
    @SerializedName("user_joining_time")
    private Date user_joining_time;
    @Expose
    @SerializedName("user_email")
    private String user_email;
    @Expose
    @SerializedName("user_level")
    private int user_level;
    @Expose
    @SerializedName("user_points")
    private int user_points;
    @Expose
    @SerializedName("user_address")
    private String user_address;
    @Expose
    @SerializedName("user_location")
    private GeoPoint user_location;
    @Expose
    @SerializedName("user_gender")
    private String user_gender;
    @Expose
    @SerializedName("user_date_birth")
    private Date user_date_birth;

    public Date getUser_date_birth() {
        return user_date_birth;
    }

    public void setUser_date_birth(Date user_date_birth) {
        this.user_date_birth = user_date_birth;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getUser_points() {
        return user_points;
    }

    public void setUser_points(int user_points) {
        this.user_points = user_points;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public GeoPoint getUser_location() {
        return user_location;
    }

    public void setUser_location(GeoPoint user_location) {
        this.user_location = user_location;
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

    public Date getUser_joining_time() {
        return user_joining_time;
    }

    public void setUser_joining_time(Date user_joining_time) {
        this.user_joining_time = user_joining_time;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
