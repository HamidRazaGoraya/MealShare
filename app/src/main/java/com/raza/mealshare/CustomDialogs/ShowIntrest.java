package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.databinding.ShowIntrestDialogBinding;
import com.raza.mealshare.databinding.SignInToViewBinding;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ShowIntrest extends DialogFragment {
    private   Buttons buttons;
    private FirebaseRef ref=new FirebaseRef();
    private ShowIntrestDialogBinding binding;
    public ShowIntrest(Buttons buttons) {
        this.buttons = buttons;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding=ShowIntrestDialogBinding.inflate(requireActivity().getLayoutInflater());

       binding.Cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });
       binding.Approve.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               buttons.SendMessage();
               dismiss();
           }
       });
        builder.setView(binding.getRoot());
        AlertDialog dialog=builder.create();
        if (dialog!= null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }
    public interface Buttons{
        void SendMessage();
    }


}