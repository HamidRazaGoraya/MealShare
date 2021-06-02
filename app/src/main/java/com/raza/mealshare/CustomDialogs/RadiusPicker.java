package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.raza.mealshare.ExtraFiles.Category;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.SettingsModel;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.CategoryAdopter;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.DialogPickRangeBinding;
import com.raza.mealshare.databinding.DialogSelectCategoryBinding;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class RadiusPicker extends DialogFragment {
    private   Buttons buttons;
    private FirebaseRef ref=new FirebaseRef();
    private DialogPickRangeBinding binding;
    public RadiusPicker(Buttons buttons) {
        this.buttons = buttons;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding=DialogPickRangeBinding.inflate(requireActivity().getLayoutInflater());
        binding.seekBar.setProgress(Utilities.GetSearchRadius(requireActivity()));
        binding.textView3.setText(String.valueOf(binding.seekBar.getProgress()/2)+"Km");
       binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if (progress>=2){
                   binding.textView3.setText(String.valueOf(progress/2)+"Km");
               }else {
                   seekBar.setProgress(2);
               }
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });
       binding.Cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });
       binding.Approve.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               buttons.SelectedRadius(binding.seekBar.getProgress());
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
        void SelectedRadius(int radius);
    }


}