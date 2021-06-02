package com.raza.mealshare.Utilities;

import android.view.View;


import com.raza.mealshare.Registration.Models.LoginModel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfoShortCuts {
    public static String currentVersion="1.1";
    public static String GetGender(String value){
        if (value==null){
            return "";
        }
        switch (value){
            case "M":
                return "(男)";
            case "F":
                return "(女)";
            default:
                return value;
        }
    }
    public static String GetProfileGender(String value){
        if (value==null){
            return "";
        }
        switch (value){
            case "M":
                return "男";
            case "F":
                return "女";
            default:
                return value;
        }
    }
    @NotNull
    public static String teamPlayers(int numberOfPlayers, View hidePlayers){
        try {
            hidePlayers.setVisibility(View.VISIBLE);
            return String.valueOf(numberOfPlayers);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
