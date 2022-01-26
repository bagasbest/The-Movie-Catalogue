package com.project.themoviecatalogue.catalogue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.themoviecatalogue.R;

import java.util.ArrayList;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.ViewHolder> {

    /// kelas ini merupakan kelas pembantu untuk menampilkan data film dalam bentuk list

    Context context;
    int singleData;
    ArrayList<CatalogueModel> modelArrayList;
    SQLiteDatabase sqLiteDatabase;

    /// generate constructor


    public CatalogueAdapter(Context context, int singleData, ArrayList<CatalogueModel> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.singleData = singleData;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_catalogue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(modelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description, genre;
        ConstraintLayout clickCatalogue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            genre = itemView.findViewById(R.id.genre);
            clickCatalogue = itemView.findViewById(R.id.clickCatalogue);
        }

        //// method untuk menampilkan data dalam bentuk list pada halaman Catalogue Fragment
        @SuppressLint("SetTextI18n")
        public void bind(CatalogueModel model) {
            byte[]imageByte = model.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            Glide.with(itemView.getContext())
                    .load(bitmap)
                    .dontTransform()
                    .into(image);

            title.setText(model.getTitle());
            description.setText(model.getDescription());
            genre.setText("Genre: " + model.getGenre());

            clickCatalogue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), CatalogueDetailActivity.class);
                    intent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, model);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
