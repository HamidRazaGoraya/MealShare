package com.raza.mealshare.Models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.raza.mealshare.Utilities.Utilities;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ImageAndCard {
    private ConstraintLayout cardView;
    private ImageView imageView;

    public ImageAndCard(ConstraintLayout cardView, ImageView imageView) {
        this.cardView = cardView;
        this.imageView = imageView;
        cardView.setVisibility(View.GONE);
    }

    public ConstraintLayout getCardView() {
        return cardView;
    }

    public void setCardView(ConstraintLayout cardView) {
        this.cardView = cardView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public void ShowImage(String path, Context context){
        cardView.setVisibility(View.VISIBLE);
        Glide.with(context).load(Utilities.StorageReference(path)).into(imageView);
    }
}
