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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.project.themoviecatalogue.MovieCatalogueActivity;
import com.project.themoviecatalogue.R;
import com.project.themoviecatalogue.catalogue.database.DBmain;
import com.project.themoviecatalogue.databinding.ActivityCatalogueEditBinding;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CatalogueEditActivity extends AppCompatActivity {

    /// ini merupakan halaman untuk mengedit film
    /// variabel dibawah di tuliskan agar tidak terjadi error ketika kelas di load
    public static final String EXTRA_CATALOGUE = "catalogue";
    private CatalogueModel model;
    private ActivityCatalogueEditBinding binding;
    private String dp;
    private static final int REQUEST_FROM_GALLERY = 1001;
    private DBmain dBmain;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogueEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// inisiasi database sqlite
        dBmain = new DBmain(this);
        model = getIntent().getParcelableExtra(EXTRA_CATALOGUE);

        /// tampilkan atribut dari film yang ingin di edit
        byte[]imageByte = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        Glide.with(this)
                .load(bitmap)
                .dontTransform()
                .into(binding.image);

        binding.title.setText(model.getTitle());
        binding.description.setText(model.getDescription());
        binding.genre.setText(model.getGenre());
        binding.release.setText(model.getRelease_date());




        /// kembali ke halaman Movie Catalogue
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /// klik ubah tanggal release
        binding.release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReleaseDate();
            }
        });

        // KLIK Untuk ubah GAMBAR
        binding.imageHint.setOnClickListener(view -> ImagePicker.with(CatalogueEditActivity.this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY));

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


    /// method untuk memvalidasi inputan pengguna, apakah semua kolom sudah terisi atau belum
    /// jika sudah terisi, otomatis bisa update film
    private void formValidation() {
        String title = binding.title.getText().toString().trim();
        String description = binding.description.getText().toString();
        String genre = binding.genre.getText().toString().trim();
        String release = binding.release.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.empty_title, Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()) {
            Toast.makeText(this, R.string.empty_desc, Toast.LENGTH_SHORT).show();
        } else if (genre.isEmpty()) {
            Toast.makeText(this, R.string.empty_genre, Toast.LENGTH_SHORT).show();
        } else if (release.equals(R.string.input_date_release)) {
            Toast.makeText(this, R.string.empty_date_release, Toast.LENGTH_SHORT).show();
        } else {
            /// proses update film
            ContentValues cv = new ContentValues();
            /// ada 2 konsisi untuk update film
            /// dimana user mengupdate gambar atau user tidak mengupdate gambar
            if(dp != null) {
                cv.put("image", ImageViewToByte(binding.image));
                cv.put("title", title);
                cv.put("description", description);
                cv.put("genre", genre);
                cv.put("release_date", release);

                sqLiteDatabase = dBmain.getWritableDatabase();
                /// ini proses inti untuk update film
                long recEdit = sqLiteDatabase.update(TABLENAME, cv, "id="+model.getId(), null);

                if (recEdit != -1) {
                    showSuccessDialog();
                }
            } else {
                cv.put("title", title);
                cv.put("description", description);
                cv.put("genre", genre);
                cv.put("release_date", release);

                sqLiteDatabase = dBmain.getWritableDatabase();
                long recEdit = sqLiteDatabase.update(TABLENAME, cv, "id="+model.getId(), null);

                if (recEdit != -1) {
                    showSuccessDialog();
                }
            }
        }
    }



    /// method munculkan dialog ketika sukses upload film
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.update_movie)
                .setMessage(R.string.update_movie_desc)
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(CatalogueEditActivity.this, MovieCatalogueActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogInterface.dismiss();
                    startActivity(intent);
                    finish();
                })
                .show();
    }


    /// konversi gambar ke dalam byte, karena sqlite tidak bisa menyimpan gambar
    private byte[] ImageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }


    /// method untuk mendapatkan gambar dari galeri
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

    /// method untuk meng set image yang dipilih dari galeri
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