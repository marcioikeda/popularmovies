package br.com.marcioikeda.popularmovies.util;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by marcio.ikeda on 18/08/2017.
 */

public class MovieAPIUtil {

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p";
    private static final String API_KEY_QUERY = "api_key";

    private static final String MOVIE_PATH = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String TOPRATED_PATH = "top_rated";

    public static final String IMG_SIZE_185 = "w185";
    public static final String IMG_SIZE_500 = "w500";

    public static URL buildPopularMoviesURL() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(MOVIE_PATH)
                        .appendPath(POPULAR_PATH)
                        .appendQueryParameter(API_KEY_QUERY, PopMoviesApplication.API_KEY)
                        .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTopRatedMoviesURL() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(TOPRATED_PATH)
                .appendQueryParameter(API_KEY_QUERY, PopMoviesApplication.API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildImageUri(String imagePath, String size) {
        return Uri.parse(IMAGE_URL).buildUpon()
                        .appendPath(size)
                        .appendPath(imagePath)
                        .build();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
