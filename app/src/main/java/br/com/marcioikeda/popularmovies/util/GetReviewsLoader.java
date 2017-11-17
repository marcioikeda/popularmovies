package br.com.marcioikeda.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.ReviewList;

/**
 * Created by marcio.ikeda on 23/10/2017.
 */

public class GetReviewsLoader extends AsyncTaskLoader<ReviewList> {

    private URL mUrl;
    private ReviewList mData;

    public GetReviewsLoader(Context context, URL url) {
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
    public ReviewList loadInBackground() {
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
            return gson.fromJson(jsonString, ReviewList.class);
        }
        return null;
    }

    @Override
    public void deliverResult(ReviewList data) {
        mData = data;
        super.deliverResult(data);
    }
}
