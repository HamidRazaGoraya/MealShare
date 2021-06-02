package com.raza.mealshare.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public  class PostedItems {
    public PostedItems() {
    }

    @Expose
    @SerializedName("user_location")
    private GeoPoint user_location;
    @Expose
    @SerializedName("userInfo")
    private UserInfo userInfo;
    @Expose
    @SerializedName("data_submission_time")
    private Date data_submission_time;
    @Expose
    @SerializedName("data_status")
    private int data_status;
    @Expose
    @SerializedName("Share")
    private boolean Share;
    @Expose
    @SerializedName("ItemName")
    private String ItemName;
    @Expose
    @SerializedName("ItemDescription")
    private String ItemDescription;
    @Expose
    @SerializedName("Condition")
    private String Condition;
    @Expose
    @SerializedName("Category")
    private Category Category;
    @Expose
    @SerializedName("All_Images")
    private List<All_Images> All_Images;
    @Exclude
    private String DocumentId;

    public GeoPoint getUser_location() {
        return user_location;
    }

    public void setUser_location(GeoPoint user_location) {
        this.user_location = user_location;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getData_submission_time() {
        return data_submission_time;
    }

    public void setData_submission_time(Date data_submission_time) {
        this.data_submission_time = data_submission_time;
    }

    public int getData_status() {
        return data_status;
    }

    public void setData_status(int data_status) {
        this.data_status = data_status;
    }

    public boolean getShare() {
        return Share;
    }

    public void setShare(boolean Share) {
        this.Share = Share;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String ItemDescription) {
        this.ItemDescription = ItemDescription;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String Condition) {
        this.Condition = Condition;
    }

    public Category getCategory() {
        return Category;
    }

    public void setCategory(Category Category) {
        this.Category = Category;
    }

    public List<All_Images> getAll_Images() {
        return All_Images;
    }

    public void setAll_Images(List<All_Images> All_Images) {
        this.All_Images = All_Images;
    }
    public <T extends PostedItems> T withId(final String taskId) {
        this.DocumentId = taskId;
        return (T) this;
    }
}
