package com.raza.mealshare.Database.RadiusFiles;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.raza.mealshare.Models.MessageModel;

import java.util.Calendar;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RadiusAndCategory")
public class RadiusAndCategory {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String Category;
    public int CategoryId;
    public int radius;

    public RadiusAndCategory() {
    }

    public RadiusAndCategory(String category, int categoryId, int radius) {
        Category = category;
        CategoryId = categoryId;
        this.radius = radius;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
