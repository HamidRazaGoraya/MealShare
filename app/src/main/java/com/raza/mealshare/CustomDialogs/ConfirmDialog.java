package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.raza.mealshare.R;

import java.util.Objects;

public class ConfirmDialog {
    public ConfirmDialog() {
    }

    public void ShowConfirmDialogWihdraw(final Context context, final DialogButtons dialogButtons){
        try {
            final Dialog DialogYesNo = new Dialog(context);
            DialogYesNo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            DialogYesNo.setContentView(R.layout.custom_yes_no_dialog);
            Objects.requireNonNull(DialogYesNo.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DialogYesNo.setCancelable(false);
            DialogYesNo.findViewById(R.id.Approve).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   dialogButtons.OnApprove();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnReject();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void ShowConfirmDeleteItem(final Context context, final DialogButtons dialogButtons){
        try {
            final Dialog DialogYesNo = new Dialog(context);
            DialogYesNo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            DialogYesNo.setContentView(R.layout.custom_yes_no_dialog);
            Objects.requireNonNull(DialogYesNo.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DialogYesNo.setCancelable(false);
            TextView title=DialogYesNo.findViewById(R.id.title);
            title.setText(R.string.sure_delete_this_item);
            Button approve=DialogYesNo.findViewById(R.id.Approve);
            approve.setText(R.string.yes);
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnApprove();
                    DialogYesNo.dismiss();
                }
            });
            Button reject=DialogYesNo.findViewById(R.id.Cancel);
            reject.setText(context.getString(R.string.cancel));
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnReject();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void ShowConfirmExitTeam(final Context context, final DialogButtons dialogButtons){
        try {
            final Dialog DialogYesNo = new Dialog(context);
            DialogYesNo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            DialogYesNo.setContentView(R.layout.custom_yes_no_dialog);
            Objects.requireNonNull(DialogYesNo.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DialogYesNo.setCancelable(false);
            TextView title=DialogYesNo.findViewById(R.id.title);
            title.setText("確定要離隊");
            Button approve=DialogYesNo.findViewById(R.id.Approve);
            approve.setText("是");
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnApprove();
                    DialogYesNo.dismiss();
                }
            });
            Button reject=DialogYesNo.findViewById(R.id.Cancel);
            reject.setText("否");
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnReject();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ShowConfirmRemovePlayer(final Context context, final DialogButtons dialogButtons){
        try {
            final Dialog DialogYesNo = new Dialog(context);
            DialogYesNo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            DialogYesNo.setContentView(R.layout.custom_yes_no_dialog);
            Objects.requireNonNull(DialogYesNo.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DialogYesNo.setCancelable(false);
            TextView title=DialogYesNo.findViewById(R.id.title);
            title.setText("確定要移除隊員");
            Button approve=DialogYesNo.findViewById(R.id.Approve);
            approve.setText("是");
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnApprove();
                    DialogYesNo.dismiss();
                }
            });
            Button reject=DialogYesNo.findViewById(R.id.Cancel);
            reject.setText("否");
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnReject();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ShowConfirmDialogLogOut(final Context context, final DialogButtons dialogButtons){
        try {
            final Dialog DialogYesNo = new Dialog(context);
            DialogYesNo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            DialogYesNo.setContentView(R.layout.custom_yes_no_dialog);
            Objects.requireNonNull(DialogYesNo.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DialogYesNo.setCancelable(false);
            TextView title=DialogYesNo.findViewById(R.id.title);
            title.setText(R.string.sure_logout);
            Button approve=DialogYesNo.findViewById(R.id.Approve);
            approve.setText(context.getString(R.string.yes));
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnApprove();
                    DialogYesNo.dismiss();
                }
            });
            Button reject=DialogYesNo.findViewById(R.id.Cancel);
            reject.setText(context.getString(R.string.cancel));
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnReject();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ShowConfirmDialogDeleteAccount(final Context context, final DialogButtons dialogButtons){
        try {
            final Dialog DialogYesNo = new Dialog(context);
            DialogYesNo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            DialogYesNo.setContentView(R.layout.custom_yes_no_dialog);
            Objects.requireNonNull(DialogYesNo.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DialogYesNo.setCancelable(false);
            TextView title=DialogYesNo.findViewById(R.id.title);
            title.setText("確定要註銷");
            Button approve=DialogYesNo.findViewById(R.id.Approve);
            approve.setText("是");
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnApprove();
                    DialogYesNo.dismiss();
                }
            });
            Button reject=DialogYesNo.findViewById(R.id.Cancel);
            reject.setText("否");
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButtons.OnReject();
                    DialogYesNo.dismiss();
                }
            });
            DialogYesNo.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
