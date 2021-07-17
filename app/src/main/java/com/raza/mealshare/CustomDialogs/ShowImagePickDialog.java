package com.raza.mealshare.CustomDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.raza.mealshare.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ShowImagePickDialog extends DialogFragment {
    private   Buttons buttons;
    private   String header;

    public ShowImagePickDialog(Buttons buttons, String header) {
        this.buttons = buttons;
        this.header = header;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_image, null);
        TextView title=view.findViewById(R.id.title);
        MaterialCardView cameraPick=view.findViewById(R.id.cameraPick);
        MaterialCardView galleryPick=view.findViewById(R.id.galleryPick);
        SetText("Camera",cameraPick);
        SetText("Gallery",galleryPick);
        cameraPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.SelectCamera();
                Objects.requireNonNull(ShowImagePickDialog.this.getDialog()).dismiss();

            }
        });
        galleryPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.SelectGallery();
                Objects.requireNonNull(ShowImagePickDialog.this.getDialog()).dismiss();
            }
        });
        title.setText(R.string.pick_from);
        title.setVisibility(View.VISIBLE);
        title.setBackgroundColor(requireActivity().getResources().getColor(R.color.themColorGreen));
        builder.setView(view);
        AlertDialog dialog=builder.create();
        if (dialog!= null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }
    public interface Buttons{
        void SelectGallery();
        void SelectCamera();
    }
    private void SetText(String text, @NotNull MaterialCardView cardView){
        TextView textView=cardView.findViewById(R.id.title);
        textView.setText(text);
    }

}