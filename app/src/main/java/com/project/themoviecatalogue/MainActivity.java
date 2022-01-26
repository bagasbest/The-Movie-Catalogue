package com.project.themoviecatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.project.themoviecatalogue.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// untuk menampilkan logo
        Glide.with(this)
                .load(R.drawable.logo)
                .into(binding.logo);


        /// untuk memberikan efek delay selama 3.5 detik di splash screen
        /// kemudian ke halaman movie catalogue
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
           startActivity(new Intent(MainActivity.this, MovieCatalogueActivity.class));
           finish();
        }, 3500);

    }

    /// method untuk menghapus activity ini, supaya tidak terjadi kebocoran memori
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}