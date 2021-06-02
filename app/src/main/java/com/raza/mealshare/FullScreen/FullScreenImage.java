package com.raza.mealshare.FullScreen;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raza.mealshare.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FullScreenImage extends DialogFragment {
private ProgressBar progressBar;


    public FullScreenImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.dialog_image_center, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(requireActivity()).load(getArguments().getString("image")).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(requireContext(), "Not Loaded error", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into((ImageView) view.findViewById(R.id.BackImage));
        view.findViewById(R.id.dissmiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}