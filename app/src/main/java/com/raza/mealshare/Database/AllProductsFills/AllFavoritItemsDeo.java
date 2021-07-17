package com.raza.mealshare.Database.AllProductsFills;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AllFavoritItemsDeo {

    @Query("SELECT * FROM MyFavoruitProduct ORDER BY data_submission_time DESC")
    List<MyFavoruitProduct> AllProducts();
    @Query("SELECT * FROM MyFavoruitProduct WHERE baseKey In (:baseKey) Limit 1")
    LiveData<List<MyFavoruitProduct>> CheckIfThisFavor(String baseKey);
    @Query("SELECT * FROM MyFavoruitProduct WHERE baseKey In (:baseKey) Limit 1")
    List<MyFavoruitProduct> CheckOnce(String baseKey);
    @Query("DELETE FROM MyFavoruitProduct")
    void deleteAll();
    @Query("DELETE FROM MyFavoruitProduct WHERE baseKey In (:baseKey)")
    void deleteOne(String baseKey);
    @Insert
    void insert(MyFavoruitProduct allPaths);
    @Update
    void update(MyFavoruitProduct allPaths);
    @Query("SELECT * FROM MyFavoruitProduct ORDER BY _id DESC")
    LiveData<List<MyFavoruitProduct>> getLiveAll();
    @Query("SELECT * FROM MyFavoruitProduct ORDER BY itemName ASC")
    LiveData<List<MyFavoruitProduct>> LiveAllProducts();
    @Query("SELECT * FROM MyFavoruitProduct WHERE Share In (:shared) ORDER BY data_submission_time DESC")
    LiveData<List<MyFavoruitProduct>> LiveAllShared(int shared);
}
