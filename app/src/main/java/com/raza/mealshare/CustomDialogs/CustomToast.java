package com.raza.mealshare.CustomDialogs;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.raza.mealshare.R;


public class CustomToast {
    private Activity activity;

    public CustomToast(Activity activity, String message) {
        this.activity = activity;
        Log.i("test","Toast Called");
        toastFloatingDark(message);
    }

    private void toastFloatingDark(String message) {
        final Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        View custom_view = activity.getLayoutInflater().inflate(R.layout.snackbar_toast_floating_dark, null);
        TextView messageTextView=custom_view.findViewById(R.id.message);
        messageTextView.setText(message);
        toast.setView(custom_view);
        toast.show();
    }

}
