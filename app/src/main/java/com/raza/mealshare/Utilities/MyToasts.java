package com.raza.mealshare.Utilities;

import android.content.Context;
import android.widget.Toast;

public class MyToasts {
    public static void requiredFields(Context context){
        Toast.makeText(context, "Required  ", Toast.LENGTH_SHORT).show();
    }
    public static void requiredFields(Context context,int mini){
        Toast.makeText(context, "Required  minimum "+mini+" characters", Toast.LENGTH_SHORT).show();
    }
    public static void passwordNotSame(Context context){
        Toast.makeText(context, "密碼不一致, 請重新輸入", Toast.LENGTH_SHORT).show();
    }
    public static void emailNotVelied(Context context){
        Toast.makeText(context, "Please enter a veiled email", Toast.LENGTH_SHORT).show();
    }
    public static void passwordToShort(Context context){
        Toast.makeText(context, "密碼必須至少6位", Toast.LENGTH_SHORT).show();
    }
    public static void starTimeAfterEndTime(Context context){
        Toast.makeText(context, "結束時間必須大於開始時間", Toast.LENGTH_SHORT).show();
    }
    public static void noTeam(Context context){
        Toast.makeText(context, "請先創建球隊, 再發起球賽", Toast.LENGTH_SHORT).show();
    }
    public static void ReLogIn(Context context){
        Toast.makeText(context, "請重新登錄", Toast.LENGTH_SHORT).show();
    }
    public static void NewVersion(Context context){
        Toast.makeText(context, "新版本已發佈,  請下載更新", Toast.LENGTH_SHORT).show();
    }
    public static void TimeOut(Context context){
        Toast.makeText(context, "網絡緩慢， 請再試一次", Toast.LENGTH_SHORT).show();
    }

    public static void NoInternet(Context context){
        Toast.makeText(context, "網絡問題，請再試一次", Toast.LENGTH_SHORT).show();
    }
    public static void ShouldNotBeSame(Context context){
        Toast.makeText(context, "首選和次選不能相同", Toast.LENGTH_SHORT).show();
    }
    public static void NotificationSettings(Context context){
        Toast.makeText(context, "通知設定完成", Toast.LENGTH_SHORT).show();
    }
}
