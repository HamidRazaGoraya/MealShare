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
public interface AllMessagesDeo {
    @Query("SELECT * FROM Messages ORDER BY time DESC")
    LiveData<List<Messages>> AllMessages();

    @Query("SELECT * FROM Messages WHERE MessageId In (:baseKey) Limit 1")
    List<Messages> loadShopByBaseKey(String baseKey);

    @Query("SELECT * FROM Messages WHERE ConversationsId In (:conversationId) ORDER BY time DESC Limit 1")
    List<Messages> getLastMessByConversationId(String conversationId);

    @Query("DELETE FROM Messages")
    void deleteAll();
    @Insert
    void insert(Messages allPaths);
    @Update
    void update(Messages allPaths);
    @Query("SELECT * FROM Messages ORDER BY _id DESC")
    LiveData<List<Messages>> getLiveAll();
    @RawQuery(observedEntities = Messages.class)
    LiveData<List<Messages>> getMessagesRaw(SupportSQLiteQuery query);



}
