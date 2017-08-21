package br.com.marcioikeda.popularmovies;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_MOVIE = "key_extra_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Will be used later
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

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
        voteAverageTextView.setText(getResources().getString(R.string.vote_average, movie.getVote_average()));

        TextView releaseYearTextView = (TextView) findViewById(R.id.tv_release_year);
        releaseYearTextView.setText(movie.getRelease_date().substring(0, 4));

        TextView voteCountTextView = (TextView) findViewById(R.id.tv_vote_count);
        voteCountTextView.setText(movie.getVote_count());

        TextView originalTitleTextView = (TextView) findViewById(R.id.tv_original_title);
        originalTitleTextView.setText(getResources().getString(R.string.original_title, movie.getOriginal_title()));

        TextView overviewTextView = (TextView) findViewById(R.id.tv_overview);
        overviewTextView.setText(movie.getOverview());
    }

}
