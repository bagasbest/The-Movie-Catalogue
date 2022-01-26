package com.project.themoviecatalogue.catalogue;

import static com.project.themoviecatalogue.catalogue.database.DBmain.TABLENAME;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.themoviecatalogue.R;
import com.project.themoviecatalogue.catalogue.database.DBmain;
import com.project.themoviecatalogue.databinding.FragmentCatalogueBinding;

import java.util.ArrayList;


public class CatalogueFragment extends Fragment {

    private FragmentCatalogueBinding binding;
    private DBmain dBmain;
    private SQLiteDatabase sqLiteDatabase;
    private CatalogueAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        /// fungsi untuk memberikan daftar movie katalogue pada halaman catalogue fragment
        populateCatalogueMovie();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvCatalogue.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatalogueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /// method untuk mendapatkan data yang tersimpan dalam sqlite, kemudian menampilkannya menggunakan recycler view adapter
    /// atribut yang digunakan adalah judul film, deskripsi, genre, tanggal rilis, dan cover
    private void populateCatalogueMovie() {
        dBmain = new DBmain(getActivity());

        sqLiteDatabase = dBmain.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLENAME + "", null);
        ArrayList<CatalogueModel> model = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            byte[]image = cursor.getBlob(1);
            String title = cursor.getString(2);
            String description = cursor.getString(3);
            String genre = cursor.getString(4);
            String release_date = cursor.getString(5);
            model.add(new CatalogueModel(id, image, title, description, genre, release_date));
        }
        cursor.close();
        if(model.size() == 0) {
            binding.noData.setVisibility(View.VISIBLE);
        }
        adapter = new CatalogueAdapter(getActivity(), R.layout.item_catalogue, model, sqLiteDatabase);
        binding.rvCatalogue.setAdapter(adapter);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /// tombol untuk menambahkan film baru
        /// jika user klik tombol ini akan ke halaman CatalogueAddActivity
        binding.addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatalogueAddActivity.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}