package com.project.themoviecatalogue.about_me;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.themoviecatalogue.R;
import com.project.themoviecatalogue.databinding.FragmentAboutBinding;


public class AboutFragment extends Fragment {
    private FragmentAboutBinding binding;

    /// halaman tentang aplikasi

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}