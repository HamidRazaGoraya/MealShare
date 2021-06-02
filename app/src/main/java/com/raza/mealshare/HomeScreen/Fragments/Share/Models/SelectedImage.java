package com.raza.mealshare.HomeScreen.Fragments.Share.Models;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.raza.mealshare.Utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SelectedImage {
    private MaterialCardView ImageBack;
    private ImageView selectedImage;
    private File file;
    private Uri uri;
    private String UploadedPath;
    private ConstraintLayout clicker;

    public SelectedImage(MaterialCardView imageBack, ImageView selectedImage, ConstraintLayout clicker) {
        ImageBack = imageBack;
        this.selectedImage = selectedImage;
        this.clicker = clicker;
    }

    public ConstraintLayout getClicker() {
        return clicker;
    }

    public void setClicker(ConstraintLayout clicker) {
        this.clicker = clicker;
    }

    public boolean isAdded(){
        return ImageBack.getVisibility()== View.VISIBLE;
    }
    public boolean Uploaded(){
        return UploadedPath!=null;
    }
    public void AddImage(@NotNull File file, Context context){
        if (file.exists()){
            this.file=file;
            ImageBack.setVisibility(View.VISIBLE);
            Glide.with(context).load(file.getAbsolutePath()).into(selectedImage);
        }
    }
    public void AddImage(@NotNull Uri uri, Context context){
        this.uri=uri;
            ImageBack.setVisibility(View.VISIBLE);
            Glide.with(context).load(uri).into(selectedImage);

    }
    public void AddImage(String path,Context context){
        UploadedPath=path;
        ImageBack.setVisibility(View.VISIBLE);
        Glide.with(context).load(Utilities.StorageReference(path)).into(selectedImage);
    }

    public MaterialCardView getImageBack() {
        return ImageBack;
    }

    public void setImageBack(MaterialCardView imageBack) {
        ImageBack = imageBack;
    }

    public ImageView getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(ImageView selectedImage) {
        this.selectedImage = selectedImage;
    }



    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUploadedPath() {
        return UploadedPath;
    }

    public void setUploadedPath(String uploadedPath) {
        UploadedPath = uploadedPath;
    }

    public Uri getUri() {
        if (uri!=null){
            return uri;
        }else {
            return Uri.fromFile(file);
        }
    }
}
