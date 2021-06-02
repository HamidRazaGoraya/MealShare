package com.raza.mealshare.ExtraFiles;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class NotificationActionReceiver extends BroadcastReceiver {
    public static final String ACTION_CLEAR = "com.daily.data.ACTION_CLEAR";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (ACTION_CLEAR.equals(Objects.requireNonNull(intent.getAction()))) {
            assert manager != null;
            manager.cancel(notificationId);
        }


    }
}