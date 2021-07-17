package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.RadiusFiles.AllRadius;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.databinding.DialogRangePickerFilterBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class GetCategoryWithFIlter extends DialogFragment {
    private String[] allCategory;
    private List<Category> categoryArrayList;
    private  int startValue=0;
    private boolean Switched=false;
    private RadiusAndCategory andCategory;

    public GetCategoryWithFIlter(@NotNull List<Category> categoryArrayList, RadiusAndCategory andCategory) {
        this.categoryArrayList = categoryArrayList;
        this.andCategory=andCategory;
        Log.i("TeamModel",String.valueOf(categoryArrayList.size()));
        allCategory =new String[categoryArrayList.size()];
        for (int i = 0; i< categoryArrayList.size(); i++){
            allCategory[i]= categoryArrayList.get(i).getName();
        }
        if(andCategory.Category.equals("ALL")){
            Switched=true;
        }else {
            Switched=false;
        }
        for (int i = 0; i< allCategory.length; i++){
            if (andCategory.Category.equals(allCategory[i])){
                startValue=i+1;
            }
        }
    }



    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        DialogRangePickerFilterBinding binding=DialogRangePickerFilterBinding.inflate(inflater);
        FillValues(binding);
        binding.pickerRange.setMinValue(1);
        binding.pickerRange.setMaxValue(allCategory.length);
        binding.pickerRange.setDisplayedValues(allCategory);
        binding.pickerRange.setValue(startValue);
        binding.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(GetCategoryWithFIlter.this.getDialog()).cancel();
            }
        });
        binding.Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (binding.allFaculties.isChecked()){
                        andCategory.setCategory("ALL");
                        andCategory.setRadius(binding.seekBar.getProgress()*5);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                AllDataBaseConstant.getInstance(requireContext()).allRadius().deleteAll();
                                AllDataBaseConstant.getInstance(requireContext()).allRadius().insert(andCategory);
                            }
                        });
                        dismiss();
                        return;
                    }
                    for (int i = 0; i< categoryArrayList.size(); i++){
                        if (allCategory[binding.pickerRange.getValue()-1].equals(categoryArrayList.get(i).getName())){
                            andCategory.setCategory(categoryArrayList.get(i).getName());
                            andCategory.setRadius(binding.seekBar.getProgress()*5);
                            andCategory.setCategoryId(categoryArrayList.get(i).getId());
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    AllDataBaseConstant.getInstance(requireContext()).allRadius().deleteAll();
                                    AllDataBaseConstant.getInstance(requireContext()).allRadius().insert(andCategory);
                                }
                            });
                            Objects.requireNonNull(GetCategoryWithFIlter.this.getDialog()).cancel();
                            return;
                        }
                    }
                }catch (Exception e){
                   e.printStackTrace();
                }
            }
        });
        binding.seekBar.setProgress(andCategory.radius/5);
        binding.textView3.setText(String.valueOf(binding.seekBar.getProgress()*5)+"Km");
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress>=2){
                    binding.textView3.setText(String.valueOf(progress*5)+"Km");
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
        binding.allFaculties.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.pickerRange.setEnabled(false);
                    binding.pickerRange.setAlpha((float) 0.5);
                }else {
                    binding.pickerRange.setEnabled(true);
                    binding.pickerRange.setAlpha(1);
                }
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

    public void FillValues(DialogRangePickerFilterBinding binding){
        if (Switched){
            binding.allFaculties.setChecked(true);
            binding.pickerRange.setEnabled(false);
            binding.pickerRange.setAlpha((float) 0.5);
        }else {
            binding.allFaculties.setChecked(false);
            binding.pickerRange.setEnabled(true);
            binding.pickerRange.setAlpha(1);
        }
    }
}