package com.project.themoviecatalogue.catalogue;

import static com.project.themoviecatalogue.catalogue.database.DBmain.TABLENAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.themoviecatalogue.MovieCatalogueActivity;
import com.project.themoviecatalogue.R;
import com.project.themoviecatalogue.catalogue.database.DBmain;
import com.project.themoviecatalogue.databinding.ActivityCatalogueAddBinding;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CatalogueAddActivity extends AppCompatActivity {

    /// halaman ini untuk menambahkan film baru

    private ActivityCatalogueAddBinding binding;
    private String dp;
    private static final int REQUEST_FROM_GALLERY = 1001;
    private DBmain dBmain;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogueAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// inisiasi database
        dBmain = new DBmain(this);
        /// kembali ke halaman Movie Catalogue
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /// klik pilih tanggal release
        binding.release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReleaseDate();
            }
        });



        // KLIK TAMBAH GAMBAR
        binding.imageHint.setOnClickListener(view -> ImagePicker.with(CatalogueAddActivity.this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY));


        /// klik simpan film
        binding.save.setOnClickListener(view -> formValidation());


    }

    /// method untuk memunculkan kalendar dan memilih tanggal release film
    private void setReleaseDate() {
        // munculkan kalendar dan user bisa memilih tanggal pada kalendar
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {

            /// setelah tanggal dipilih, tutup kalendar dan ganti teks "Input Release Date" dengan tanggal yang di pilih user
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String getDateNow = sdf.format(new Date(Long.parseLong(selection.toString())));
            binding.release.setText(getDateNow);
        });
    }


    /// method untuk memvalidasi inputan pengguna
    /// jika semua kolom sudah terisi, maka sistem bisa menambahkan film baru
    /// dan menyimpan data film ke database sqlite
    private void formValidation() {
        String title = binding.title.getText().toString().trim();
        String description = binding.description.getText().toString();
        String genre = binding.genre.getText().toString().trim();
        String release = binding.release.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.empty_title, Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()) {
            Toast.makeText(this,  R.string.empty_desc, Toast.LENGTH_SHORT).show();
        } else if (genre.isEmpty()) {
            Toast.makeText(this,  R.string.empty_genre, Toast.LENGTH_SHORT).show();
        } else if (release.equals( R.string.input_release_date)) {
            Toast.makeText(this, R.string.empty_date_release, Toast.LENGTH_SHORT).show();
        } else if (dp == null) {
            Toast.makeText(this, R.string.empty_dp, Toast.LENGTH_SHORT).show();
        } else {
            ContentValues cv = new ContentValues();
            cv.put("image", ImageViewToByte(binding.image));
            cv.put("title", title);
            cv.put("description", description);
            cv.put("genre", genre);
            cv.put("release_date", release);

            sqLiteDatabase = dBmain.getWritableDatabase();
            Long recinsert = sqLiteDatabase.insert(TABLENAME, null, cv);
            if (recinsert != null) {
                showSuccessDialog();
            }
        }
    }

    /// munculkan dialog ketika sukses upload film baru
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.upload_new_movie)
                .setMessage(R.string.upload_new_movie_desc)
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(CatalogueAddActivity.this, MovieCatalogueActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogInterface.dismiss();
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    /// konversi gambar ke bentuk byte, karena sqlite tidak bisa menyimpan gambar
    private byte[] ImageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    /// method untuk mengambil gambar dari galeri
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadImage(data.getData());
            }
        }
    }

    /// method untuk meng set  gambar yang dipilih dari galeri
    private void uploadImage(Uri data) {
        dp = data.toString();
        Glide.with(this)
                .load(data)
                .into(binding.image);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}