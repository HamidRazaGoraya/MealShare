package com.raza.mealshare.Database.MessagesFiles;

import com.raza.mealshare.Models.MessageModel;
import com.raza.mealshare.Models.UserInfo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserDetails")
public class UserDetails {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String user_uid;
    public String user_name;
    public String user_email;
    public String user_profile_pic;

    public UserDetails() {
    }

    public UserDetails(UserInfo userInfo) {
        user_uid=userInfo.getUser_uid();
        user_name=userInfo.getUser_name();
        user_email=userInfo.getUser_email();
        user_profile_pic=userInfo.getUser_profile_pic();
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }
}
