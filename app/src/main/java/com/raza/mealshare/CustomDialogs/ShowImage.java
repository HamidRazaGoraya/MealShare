package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.DialogImageCenterBinding;
import com.raza.mealshare.databinding.SignInToViewBinding;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ShowImage extends DialogFragment {

    private FirebaseRef ref=new FirebaseRef();
    private DialogImageCenterBinding binding;
    private String linkText;
    public ShowImage(String link) {
        this.linkText=link;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding=DialogImageCenterBinding.inflate(requireActivity().getLayoutInflater());
       binding.dissmiss.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });
       Glide.with(requireContext()).load(Utilities.StorageReference(linkText)).into(binding.BackImage);
        builder.setView(binding.getRoot());
        AlertDialog dialog=builder.create();
        if (dialog!= null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }



}