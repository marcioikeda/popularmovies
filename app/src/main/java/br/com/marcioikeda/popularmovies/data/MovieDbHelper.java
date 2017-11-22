package br.com.marcioikeda.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcio.ikeda on 21/11/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "movie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                + " (" + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieContract.MovieEntry.COLUMN_ID + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_POSTER + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_BACKDROP + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
