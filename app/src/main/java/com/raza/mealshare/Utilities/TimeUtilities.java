package com.raza.mealshare.Utilities;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtilities {
    public static String GetUpdateTime(Date date) {
        try {
            Calendar today=Calendar.getInstance();
            Calendar startDate=Calendar.getInstance();
            startDate.setTime(date);
            if (today.get(Calendar.DAY_OF_YEAR)==startDate.get(Calendar.DAY_OF_YEAR)){
                return " Today  "+WalkTime().format(startDate.getTime());
            }else if (today.get(Calendar.DAY_OF_YEAR)==startDate.get(Calendar.DAY_OF_YEAR)+1){
                return " Yesterday "+WalkTime().format(startDate.getTime());
            }else {
                return WalkDate().format(startDate.getTime())+WalkTime().format(startDate.getTime());
            }
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat WalkDate(){
        return new SimpleDateFormat("dd MMM", Locale.getDefault());
    }
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat WalkTime(){
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

}
