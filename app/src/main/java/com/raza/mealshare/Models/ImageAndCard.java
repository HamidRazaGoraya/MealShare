package com.raza.mealshare.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.raza.mealshare.Utilities.Utilities;

import androidx.annotation.Nullable;
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
    public void ShowImage(String path, Context context,Failed failed){
        cardView.setVisibility(View.VISIBLE);
        Glide.with(context).load(Utilities.StorageReference(path)).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                failed.reload();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }
    public interface Failed{
        void reload();
    }
}
