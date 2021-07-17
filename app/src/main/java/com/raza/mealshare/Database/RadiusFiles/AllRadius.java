package com.raza.mealshare.Database.RadiusFiles;

import com.raza.mealshare.Database.MessagesFiles.UserDetails;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AllRadius {
    @Query("SELECT * FROM RadiusAndCategory ")
    LiveData<List<RadiusAndCategory>> getLiveAll();
    @Query("SELECT * FROM RadiusAndCategory")
    List<RadiusAndCategory> getAllOnce();
    @Query("DELETE FROM RadiusAndCategory")
    void deleteAll();
    @Insert
    void insert(RadiusAndCategory allPaths);
    @Update
    void update(RadiusAndCategory allPaths);
}
