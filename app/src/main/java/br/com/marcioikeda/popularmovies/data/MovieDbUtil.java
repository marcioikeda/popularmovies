package br.com.marcioikeda.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import br.com.marcioikeda.popularmovies.model.Movie;

import static android.R.attr.id;

/**
 * Created by marcio.ikeda on 22/11/2017.
 */

public class MovieDbUtil {

    public static boolean isFavorite(Context context, String movieId) {
        Cursor cursor =  context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                                                null,
                                                                MovieContract.MovieEntry.COLUMN_ID + "=?",
                                                                new String[]{movieId},
                                                                null);
        return cursor.getCount() > 0 ;
    }

    public static Uri insertFavorite(Context context, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPoster_path());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdrop_path());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());

        return context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    public static int deleteFavorite(Context context, String movieId) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();
        return context.getContentResolver().delete(uri, null, null);
    }
}
