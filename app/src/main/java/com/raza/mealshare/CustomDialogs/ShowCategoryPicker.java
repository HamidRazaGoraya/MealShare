package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.google.gson.Gson;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.ExtraFiles.SettingsModel;
import com.raza.mealshare.HomeScreen.Fragments.Share.Adopter.CategoryAdopter;
import com.raza.mealshare.Models.Category;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.DialogSelectCategoryBinding;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ShowCategoryPicker extends DialogFragment {
    private   Buttons buttons;
    private FirebaseRef ref=new FirebaseRef();
    private DialogSelectCategoryBinding binding;
    public ShowCategoryPicker(Buttons buttons) {
        this.buttons = buttons;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding=DialogSelectCategoryBinding.inflate(requireActivity().getLayoutInflater());
        SettingsModel settingsModel= new Gson().fromJson(Utilities.getSharedPreferences(requireActivity()).getString(ref.Settings,""),SettingsModel.class);
        CategoryAdopter adopter=new CategoryAdopter(requireContext(),settingsModel.getCategory());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycleView.setAdapter(adopter);
        adopter.setClickListener(new CategoryAdopter.ItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                buttons.SelectedCategory(category);
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
        void SelectedCategory(Category category);
    }


}