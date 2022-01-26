package com.project.themoviecatalogue.catalogue;

import static com.project.themoviecatalogue.catalogue.database.DBmain.TABLENAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.themoviecatalogue.MovieCatalogueActivity;
import com.project.themoviecatalogue.R;
import com.project.themoviecatalogue.catalogue.database.DBmain;
import com.project.themoviecatalogue.databinding.ActivityCatalogueDetailBinding;

public class CatalogueDetailActivity extends AppCompatActivity {

    /// halaman ini berfungsi untuk menampilkan detail movie

    public static final String EXTRA_CATALOGUE = "catalogue";
    private ActivityCatalogueDetailBinding binding;
    private CatalogueModel model;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogueDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //// mula mula set terlebih dahulu atribut yang akan digunakan
        model = getIntent().getParcelableExtra(EXTRA_CATALOGUE);
        byte[]imageByte = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        Glide.with(this)
                .load(bitmap)
                .dontTransform()
                .into(binding.image);
        binding.title.setText(model.getTitle());
        binding.genre.setText("Genre: " + model.getGenre());
        binding.release.setText("Release Date: "+ model.getRelease_date());
        binding.description.setText(model.getDescription());


        /// klik untuk kembali ke halaman sebelumnya
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /// klik untuk mengedit film
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogueDetailActivity.this ,CatalogueEditActivity.class);
                intent.putExtra(CatalogueEditActivity.EXTRA_CATALOGUE, model);
                startActivity(intent);
            }
        });


        /// klik untuk menghapus film
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDeleteMovie();
            }
        });
    }

    /// konfirmasi menghapus film
    private void showConfirmationDeleteMovie() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.confirm_delete_desc)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    deleteMovie();
                })
                .setNegativeButton("NO", null)
                .show();
    }


    /// proses menghapus film
    private void deleteMovie() {
        DBmain dBmain = new DBmain(this);
        SQLiteDatabase sqLiteDatabase = dBmain.getReadableDatabase();
        long recDelete = sqLiteDatabase.delete(TABLENAME, "id="+model.getId(),null);
        if(recDelete != -1) {
            showSuccessDeleteDialog();
        }
    }


    /// berhasil menghapus film
    private void showSuccessDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_movie)
                .setMessage(R.string.delete_movie_success)
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(CatalogueDetailActivity.this, MovieCatalogueActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogInterface.dismiss();
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}