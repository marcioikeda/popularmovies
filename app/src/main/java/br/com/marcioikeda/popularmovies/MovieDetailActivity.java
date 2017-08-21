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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.MovieDetail;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_MOVIE = "key_extra_movie";

    private MovieDetail mMovieDetail;
    private Movie mMovie;

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

        //Adjust Height
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
        float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appbar.getLayoutParams();
        lp.height = (int)heightDp;
        appbar.setLayoutParams(lp);

        //Get Movie from intent
        Movie movie = getIntent().getExtras().getParcelable(KEY_EXTRA_MOVIE);
        if (movie != null) {
            loadUIContent(movie);
        }
    }

    private void loadUIContent(Movie movie) {
        //Set UI content
        setTitle(movie.getTitle());
        ImageView movieTopImageView = (ImageView) findViewById(R.id.iv_movie_detail_appbar);
        Picasso.with(this).load(MovieAPIUtil
                .buildImageUri(movie.getBackdrop_path().substring(1), MovieAPIUtil.IMG_SIZE_500))
                .into(movieTopImageView);

        ImageView moviePostImageView = (ImageView) findViewById(R.id.iv_movie_detail_post);
        Picasso.with(this).load(MovieAPIUtil
                .buildImageUri(movie.getPoster_path().substring(1), MovieAPIUtil.IMG_SIZE_185))
                .into(moviePostImageView);

        TextView voteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        voteAverageTextView.setText(movie.getVote_average() + "/10");

        TextView releaseYearTextView = (TextView) findViewById(R.id.tv_release_year);
        releaseYearTextView.setText(movie.getRelease_date().substring(0, 4));

        TextView voteCountTextView = (TextView) findViewById(R.id.tv_vote_count);
        voteCountTextView.setText(movie.getVote_count());

        TextView originalTitleTextView = (TextView) findViewById(R.id.tv_original_title);
        originalTitleTextView.setText(movie.getOriginal_title() + "\n(original title)");

        TextView overviewTextView = (TextView) findViewById(R.id.tv_overview);
        overviewTextView.setText(movie.getOverview());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_EXTRA_MOVIE, mMovie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Movie movie = savedInstanceState.getParcelable(KEY_EXTRA_MOVIE);
        loadUIContent(movie);
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
            } else {
                Toast.makeText(MovieDetailActivity.this, "Error Fetching Movie Detail", Toast.LENGTH_LONG).show();
            }
        }
    }
}
