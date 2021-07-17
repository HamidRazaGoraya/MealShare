package com.raza.mealshare.Database.AllProductsFills;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface AllOtherItemsDeo {

    @Query("SELECT * FROM OtherProduct ORDER BY data_submission_time DESC")
    List<OtherProduct> AllProducts();
    @Query("SELECT * FROM OtherProduct WHERE baseKey In (:baseKey) Limit 1")
    LiveData<List<OtherProduct>> loadShopByBaseKey(String baseKey);
    @Query("DELETE FROM OtherProduct")
    void deleteAll();
    @Insert
    void insert(OtherProduct allPaths);
    @Update
    void update(OtherProduct allPaths);
    @Query("SELECT * FROM OtherProduct ORDER BY _id DESC")
    LiveData<List<OtherProduct>> getLiveAll();
    @Query("SELECT * FROM OtherProduct ORDER BY itemName ASC")
    LiveData<List<OtherProduct>> LiveAllProducts();
    @Query("SELECT * FROM OtherProduct WHERE Share In (:shared) ORDER BY data_submission_time DESC")
    LiveData<List<OtherProduct>> LiveAllShared(int shared);
    @RawQuery(observedEntities = OtherProduct.class)
    LiveData<List<OtherProduct>> getProduct(SupportSQLiteQuery query);
}
