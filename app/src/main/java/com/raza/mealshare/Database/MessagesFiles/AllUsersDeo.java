package com.raza.mealshare.Database.MessagesFiles;

import com.raza.mealshare.Database.AllProductsFills.MyProduct;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface AllUsersDeo {
    @Query("SELECT * FROM UserDetails WHERE user_uid In (:userUid) Limit 1")
    LiveData<List<UserDetails>> findUserByUid(String userUid);
    @Query("SELECT * FROM UserDetails WHERE user_uid In (:userUid) Limit 1")
    List<UserDetails> checkUser(String userUid);
    @Query("DELETE FROM UserDetails")
    void deleteAll();
    @Insert
    void insert(UserDetails allPaths);
    @Update
    void update(UserDetails allPaths);
}
