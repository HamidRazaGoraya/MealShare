package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;

import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.DialogPickRangeBinding;
import com.raza.mealshare.databinding.SignInToViewBinding;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MustSignIn extends DialogFragment {
    private   Buttons buttons;
    private FirebaseRef ref=new FirebaseRef();
    private SignInToViewBinding binding;
    public MustSignIn(Buttons buttons) {
        this.buttons = buttons;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding=SignInToViewBinding.inflate(requireActivity().getLayoutInflater());
       binding.Cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });
       binding.Approve.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               buttons.SelectedSign();
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
        void SelectedSign();
    }


}