package com.project.themoviecatalogue.catalogue;

import android.os.Parcel;
import android.os.Parcelable;


/// kelas model digunakan untuk sebagai kelas penampung data data dari database sqlite, kelas ini digunakan pada CatalogueFragment untuk menampilkan data movie

public class CatalogueModel implements Parcelable {
    private int id;
    private byte[] image;
    private String title;
    private String description;
    private String genre;
    private String release_date;

    /// constructor
    public CatalogueModel(int id, byte[] image, String title, String description, String genre, String release_date) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.release_date = release_date;
    }

    protected CatalogueModel(Parcel in) {
        id = in.readInt();
        image = in.createByteArray();
        title = in.readString();
        description = in.readString();
        genre = in.readString();
        release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(image);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(genre);
        dest.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CatalogueModel> CREATOR = new Creator<CatalogueModel>() {
        @Override
        public CatalogueModel createFromParcel(Parcel in) {
            return new CatalogueModel(in);
        }

        @Override
        public CatalogueModel[] newArray(int size) {
            return new CatalogueModel[size];
        }
    };

    /// getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
