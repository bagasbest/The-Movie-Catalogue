package com.project.themoviecatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.project.themoviecatalogue.about_me.AboutFragment;
import com.project.themoviecatalogue.catalogue.CatalogueFragment;
import com.project.themoviecatalogue.databinding.ActivityMovieCatalogueBinding;
import com.project.themoviecatalogue.language.LanguageFragment;

public class MovieCatalogueActivity extends AppCompatActivity {

    private ActivityMovieCatalogueBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieCatalogueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        /// untuk memunculkan bottom navigation bar
        ChipNavigationBar navView = findViewById(R.id.nav_view);
        navView.setItemSelected(R.id.movie, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CatalogueFragment()).commit();
        bottomMenu(navView);

    }


    /// fungsi untuk melakukan navigasi: Movie -> Language -> about
    @SuppressLint("NonConstantResourceId")
    private void bottomMenu(ChipNavigationBar navView) {
        navView.setOnItemSelectedListener
                (i -> {
                    Fragment fragment = null;
                    switch (i) {
                        case R.id.movie:
                            fragment = new CatalogueFragment();
                            break;
                        case R.id.language:
                            fragment = new LanguageFragment();
                            break;
                        case R.id.about:
                            fragment = new AboutFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,
                                    fragment).commit();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}