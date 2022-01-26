package com.project.themoviecatalogue.catalogue.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBmain extends SQLiteOpenHelper {

    /// kelas ini merupakan kelas database, dimana inisiasi database dan operasi database dilakukan disini

    public static final String DBNAME = "catalogue.db";
    public static final String TABLENAME = "movie";
    public static final int VER = 1;


    public DBmain(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }


    //// membuat tabel baru dengan nama movie
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table " + TABLENAME + "(id integer primary key, image blob, title text, description text, genre text, release_date text)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "drop table if exists " + TABLENAME + "";
        sqLiteDatabase.execSQL(query);
    }
}
