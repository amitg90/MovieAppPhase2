package com.example.amit.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FavMovies.db";
    public static final String FAV_TABLE_NAME = "UserFavMovies";
    public static final String FAV_COLUMN_ID = "id";
    public static final String FAV_COLUMN_NAME = "name";
    public static final String PATH_COLUMN_NAME = "path";
    public static final String RELEASE_DATE_COLUMN_NAME = "release_date";
    public static final String VOTE_AVG_COLUMN_NAME = "vote_average";
    public static final String OVERVIEW_COLUMN_NAME = "overview";

    public Database(Context context) {
        super(context, DATABASE_NAME , null, 1);
        Log.e("Database", "Constructor Called");
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.e("Database", "onCreate Called");
        try {
            db.execSQL(
                    "create table UserFavMovies " +
                            "(id integer primary key, name text, path text, release_date text, vote_average text, overview text)"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS UserFavMovies");
        onCreate(db);
    }

    public long insertFav (ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(FAV_TABLE_NAME, null, contentValues);

        return id;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from UserFavMovies where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FAV_TABLE_NAME);
        return numRows;
    }

    public Integer deleteFav(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAV_TABLE_NAME,
                "id = ? ",
                new String[] { Long.toString(id) });
    }

    public void getAllFavMovies() {
        MovieInfo movieInfo;

        MovieDB.movieInfoArrayList.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from UserFavMovies", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            movieInfo = new MovieInfo();
            movieInfo.title = res.getString(res.getColumnIndex(FAV_COLUMN_NAME));
            movieInfo.id = res.getString(res.getColumnIndex(FAV_COLUMN_ID));
            movieInfo.path = res.getString(res.getColumnIndex(PATH_COLUMN_NAME));
            movieInfo.release_date = res.getString(res.getColumnIndex(RELEASE_DATE_COLUMN_NAME));
            movieInfo.vote_average = res.getString(res.getColumnIndex(VOTE_AVG_COLUMN_NAME));
            movieInfo.overview = res.getString(res.getColumnIndex(OVERVIEW_COLUMN_NAME));
            MovieDB.movieInfoArrayList.add(movieInfo);
            Log.e("Database-READ-ALL", movieInfo.title);
            res.moveToNext();
        }
        return;
    }
}
