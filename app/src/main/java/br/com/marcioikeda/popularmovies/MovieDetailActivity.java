package br.com.marcioikeda.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.MovieDetail;
import br.com.marcioikeda.popularmovies.model.MovieList;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

import static android.R.id.list;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_MOVIE = "key_extra_movie";

    private MovieDetail mMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Get Movie from intent
        Movie movie = getIntent().getExtras().getParcelable(KEY_EXTRA_MOVIE);
        Toast.makeText(this, "Got movie: " + movie.getTitle(), Toast.LENGTH_SHORT).show();

        //Adjust Height
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
        float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appbar.getLayoutParams();
        lp.height = (int)heightDp*2;
        appbar.setLayoutParams(lp);

        //Set UI content
        setTitle(movie.getTitle());
        ImageView movieImageView = (ImageView) findViewById(R.id.iv_movie_detail_post);
        Picasso.with(this).load(MovieAPIUtil
                .buildImageUri(movie.getPoster_path().substring(1), MovieAPIUtil.IMG_SIZE_500))
                .into(movieImageView);
    }

    void loadMovieIntoUI(MovieDetail movie) {

    }

    public class GetMovieDetailTask extends AsyncTask<URL, Void, MovieDetail> {

        @Override
        protected MovieDetail doInBackground(URL... params) {
            if (params.length == 0) {
                return null;
            }

            URL url = params[0];
            String jsonString = null;
            try {
                jsonString = MovieAPIUtil.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if (jsonString != null) {
                Gson gson = new Gson();
                MovieDetail result = gson.fromJson(jsonString, MovieDetail.class);
                return result;
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDetail result) {
            if (result != null) {
                mMovieDetail = result;
                loadMovieIntoUI(result);
            } else {
                Toast.makeText(MovieDetailActivity.this, "Error Fetching Movie Detail", Toast.LENGTH_LONG).show();
            }
        }
    }
}
