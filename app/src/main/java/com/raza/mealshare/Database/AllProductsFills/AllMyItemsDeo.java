package com.raza.mealshare.Database.AllProductsFills;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AllMyItemsDeo {
    @Query("SELECT * FROM MyProduct ORDER BY data_submission_time DESC")
    List<MyProduct> AllProducts();

    @Query("SELECT * FROM MyProduct WHERE baseKey In (:baseKey) Limit 1")
    List<MyProduct> loadShopByBaseKey(String baseKey);

    @Query("DELETE FROM MyProduct")
    void deleteAll();
    @Insert
    void insert(MyProduct allPaths);
    @Update
    void update(MyProduct allPaths);
    @Query("SELECT * FROM MyProduct ORDER BY _id DESC")
    LiveData<List<MyProduct>> getLiveAll();

    @Query("SELECT * FROM MyProduct ORDER BY itemName ASC")
    LiveData<List<MyProduct>> LiveAllProducts();

    @Query("SELECT * FROM MyProduct WHERE Share In (:shared) ORDER BY data_submission_time DESC")
    LiveData<List<MyProduct>> LiveAllShared(int shared);


}
