package br.com.marcioikeda.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.MovieList;

/**
 * Created by marcio.ikeda on 22/08/2017.
 */

public class GetMoviesLoader extends AsyncTaskLoader<MovieList> {

    URL mUrl;
    MovieList mData;

    public GetMoviesLoader(Context context, URL url) {
        super(context);
        mUrl = url;
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
        if (mUrl == null) {
            return null;
        }

        String jsonString;
        try {
            jsonString = MovieAPIUtil.getResponseFromHttpUrl(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (jsonString != null) {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, MovieList.class);
        }
        return null;
    }

    @Override
    public void deliverResult(MovieList data) {
        mData = data;
        super.deliverResult(data);
    }
}
