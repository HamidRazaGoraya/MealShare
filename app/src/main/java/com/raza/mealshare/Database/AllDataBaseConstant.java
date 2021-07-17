package com.raza.mealshare.Database;

import android.content.Context;
import android.util.Log;


import com.raza.mealshare.Database.AllProductsFills.AllFavoritItemsDeo;
import com.raza.mealshare.Database.AllProductsFills.MyFavoruitProduct;
import com.raza.mealshare.Database.MessagesFiles.AllMessagesDeo;
import com.raza.mealshare.Database.AllProductsFills.AllMyItemsDeo;
import com.raza.mealshare.Database.AllProductsFills.AllOtherItemsDeo;
import com.raza.mealshare.Database.AllProductsFills.MyProduct;
import com.raza.mealshare.Database.AllProductsFills.OtherProduct;
import com.raza.mealshare.Database.MessagesFiles.AllUsersDeo;
import com.raza.mealshare.Database.MessagesFiles.Messages;
import com.raza.mealshare.Database.MessagesFiles.UserDetails;
import com.raza.mealshare.Database.RadiusFiles.AllRadius;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {MyProduct.class, OtherProduct.class, Messages.class, UserDetails.class, MyFavoruitProduct.class, RadiusAndCategory.class}, version = 1)
public abstract class AllDataBaseConstant extends RoomDatabase {
   private static final String DB_NAME = "userProductFiles7";
   private static  AllDataBaseConstant instance;
   public static  AllDataBaseConstant getInstance(Context context) {
        if (instance == null) {
            Log.i("NewInstance","New");
            instance = Room.databaseBuilder(context.getApplicationContext(),AllDataBaseConstant.class,DB_NAME).build();
        }
       Log.i("NewInstance","Old");
        return instance;
    }
   public abstract AllMyItemsDeo allMyItemsDeo();
   public abstract AllOtherItemsDeo allOtherItemsDeo();
   public abstract AllMessagesDeo allMessagesDeo();
   public abstract AllUsersDeo allUsersDeo();
   public abstract AllFavoritItemsDeo allFavoritItemsDeo();
   public abstract AllRadius allRadius();
}

