package br.com.marcioikeda.popularmovies.util;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.TrailerList;

/**
 * Created by marcio.ikeda on 23/10/2017.
 */

public class GetTrailersTask extends AsyncTask<URL, Void, TrailerList> {

    IAsyncTaskListener<TrailerList> mListener;

    public GetTrailersTask(IAsyncTaskListener<TrailerList> listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.onPreExecute();
    }

    @Override
    protected TrailerList doInBackground(URL... params) {
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
            return gson.fromJson(jsonString, TrailerList.class);
        }
        return null;
    }

    @Override
    protected void onPostExecute(TrailerList list) {
        mListener.onComplete(list);
    }
}
