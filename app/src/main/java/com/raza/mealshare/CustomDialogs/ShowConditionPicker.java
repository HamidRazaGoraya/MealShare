package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.google.gson.Gson;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.SettingsModel;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.ConditionAdopter;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.DialogSelectCategoryBinding;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ShowConditionPicker extends DialogFragment {
    private   Buttons buttons;
    private FirebaseRef ref=new FirebaseRef();
    private DialogSelectCategoryBinding binding;
    public ShowConditionPicker(Buttons buttons) {
        this.buttons = buttons;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding=DialogSelectCategoryBinding.inflate(requireActivity().getLayoutInflater());
        SettingsModel settingsModel= new Gson().fromJson(Utilities.getSharedPreferences(requireActivity()).getString(ref.Settings,""),SettingsModel.class);
        ConditionAdopter adopter=new ConditionAdopter(requireContext(),settingsModel.getCondition());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycleView.setAdapter(adopter);
        binding.title.setText(R.string.select_condition);
        adopter.setClickListener(new ConditionAdopter.ItemClickListener() {
            @Override
            public void onItemClick(String condition) {
                buttons.SelectedCondition(condition);
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
        void SelectedCondition(String condition);
    }


}