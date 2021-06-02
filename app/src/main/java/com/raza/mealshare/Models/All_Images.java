package com.raza.mealshare.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class All_Images {
    @Expose
    @SerializedName("data_thumbnail_storage_path")
    private String data_thumbnail_storage_path;
    @Expose
    @SerializedName("data_fullimage_storage_path")
    private String data_fullimage_storage_path;

    public String getData_thumbnail_storage_path() {
        return data_thumbnail_storage_path;
    }

    public void setData_thumbnail_storage_path(String data_thumbnail_storage_path) {
        this.data_thumbnail_storage_path = data_thumbnail_storage_path;
    }

    public String getData_fullimage_storage_path() {
        return data_fullimage_storage_path;
    }

    public void setData_fullimage_storage_path(String data_fullimage_storage_path) {
        this.data_fullimage_storage_path = data_fullimage_storage_path;
    }
}
