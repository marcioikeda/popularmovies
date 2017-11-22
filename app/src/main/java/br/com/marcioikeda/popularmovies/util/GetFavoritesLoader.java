package br.com.marcioikeda.popularmovies.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.marcioikeda.popularmovies.data.MovieContract;
import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.MovieList;

import static android.R.id.list;

/**
 * Created by marcio.ikeda on 22/11/2017.
 */

public class GetFavoritesLoader extends AsyncTaskLoader<MovieList> {

    MovieList mData;
    Context mContext;

    public GetFavoritesLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    public MovieList loadInBackground() {
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null, null, null, null);
        ArrayList<Movie> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
            movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
            movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP)));
            movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
            movie.setVote_average(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
            movie.setVote_count(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));
            movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
            list.add(movie);
        }
        MovieList movieList = new MovieList();
        Movie[] movieArray = new Movie[list.size()];
        movieList.setResults(list.toArray(movieArray));

        return movieList;
    }

    @Override
    public void deliverResult(MovieList data) {
        mData = data;
        super.deliverResult(data);
    }
}

