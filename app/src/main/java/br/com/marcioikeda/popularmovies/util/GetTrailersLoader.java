package br.com.marcioikeda.popularmovies.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.TrailerList;

/**
 * Created by marcio.ikeda on 23/10/2017.
 */

public class GetTrailersLoader extends AsyncTaskLoader<TrailerList> {

    private URL mUrl;
    private TrailerList mData;

    public GetTrailersLoader(Context context, URL url) {
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
    public TrailerList loadInBackground() {
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
            return gson.fromJson(jsonString, TrailerList.class);
        }
        return null;
    }

    @Override
    public void deliverResult(TrailerList data) {
        mData = data;
        super.deliverResult(data);
    }
}
