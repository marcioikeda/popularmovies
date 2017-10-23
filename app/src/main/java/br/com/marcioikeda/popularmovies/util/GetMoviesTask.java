package br.com.marcioikeda.popularmovies.util;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.MovieList;

/**
 * Created by marcio.ikeda on 22/08/2017.
 */

public class GetMoviesTask extends AsyncTask<URL, Void, MovieList> {

    IAsyncTaskListener<MovieList> mListener;

    public GetMoviesTask(IAsyncTaskListener<MovieList> listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.onPreExecute();
    }

    @Override
    protected MovieList doInBackground(URL... params) {
        if (params.length == 0) {
            return null;
        }

        URL url = params[0];
        String jsonString;
        try {
            jsonString = MovieAPIUtil.getResponseFromHttpUrl(url);
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
    protected void onPostExecute(MovieList list) {
        mListener.onComplete(list);
    }
}
