package com.raza.mealshare.HomeScreen.Fragments.Messages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raza.mealshare.R;
import com.raza.mealshare.databinding.FragmentMessagesBinding;

import org.jetbrains.annotations.NotNull;

public class Messages extends Fragment {
    private FragmentMessagesBinding binding;
    public Messages() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentMessagesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}