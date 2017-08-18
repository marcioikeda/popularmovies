package br.com.marcioikeda.popularmovies.util;

import android.app.Application;

import br.com.marcioikeda.popularmovies.R;

/**
 * Created by marcio.ikeda on 18/08/2017.
 */

public class PopMoviesApplication extends Application {

    public static String API_KEY;

    @Override
    public void onCreate() {
        super.onCreate();
        API_KEY = getResources().getString(R.string.api_key);
    }
}
