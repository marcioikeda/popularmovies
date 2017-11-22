package br.com.marcioikeda.popularmovies;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.marcioikeda.popularmovies.data.MovieDbUtil;
import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.ReviewList;
import br.com.marcioikeda.popularmovies.model.TrailerList;
import br.com.marcioikeda.popularmovies.util.GetReviewsLoader;
import br.com.marcioikeda.popularmovies.util.GetTrailersLoader;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

import static android.R.attr.data;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_MOVIE = "key_extra_movie";

    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    private static final String KEY_MOVIE_ID = "key_movie_id";

    private RecyclerView mRecyclerView;

    Movie mMovie;

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
                if (MovieDbUtil.isFavorite(MovieDetailActivity.this, mMovie.getId())) {
                    MovieDbUtil.deleteFavorite(MovieDetailActivity.this, mMovie.getId());
                    Snackbar.make(view, getString(R.string.favorites_remove), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    MovieDbUtil.insertFavorite(MovieDetailActivity.this, mMovie);
                    Snackbar.make(view, getString(R.string.favorites_add), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content_movie_detail);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        MovieDetailAdapter adapter = new MovieDetailAdapter();
        mRecyclerView.setAdapter(adapter);

        //Adjust Height
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
        float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appbar.getLayoutParams();
        lp.height = (int)heightDp;
        appbar.setLayoutParams(lp);

        //Get Movie from intent
        mMovie = getIntent().getExtras().getParcelable(KEY_EXTRA_MOVIE);
        if (mMovie != null) {
            loadUIContent(mMovie);
            Bundle args = new Bundle();
            args.putString(KEY_MOVIE_ID, mMovie.getId());
            getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, args, trailerLoaderListener);
            getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, args, reviewLoaderListener);
        }

    }

    private void loadUIContent(Movie movie) {
        //Set UI content
        setTitle(movie.getTitle());
        ImageView movieTopImageView = (ImageView) findViewById(R.id.iv_movie_detail_appbar);
        Picasso.with(this).load(MovieAPIUtil
                .buildImageUri(movie.getBackdrop_path().substring(1), MovieAPIUtil.IMG_SIZE_500))
                .into(movieTopImageView);
        ((MovieDetailAdapter) mRecyclerView.getAdapter()).setMovieData(movie);
    }

    private LoaderManager.LoaderCallbacks<TrailerList> trailerLoaderListener = new LoaderManager.LoaderCallbacks<TrailerList>() {

        @Override
        public Loader<TrailerList> onCreateLoader(int id, Bundle args) {
            if (id == TRAILER_LOADER_ID) {
                if (args != null && args.getString(KEY_MOVIE_ID) != null) {
                    return new GetTrailersLoader(getBaseContext(), MovieAPIUtil.buildTrailerURL(args.getString(KEY_MOVIE_ID)));
                }
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<TrailerList> loader, TrailerList data) {
            if (data == null) {
                //TODO tratar erro
            } else if (data.getResults() == null || data.getResults().size() == 0) {
                //TODO tratar sem trailer
            } else {
                ((MovieDetailAdapter) mRecyclerView.getAdapter()).setTrailerData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<TrailerList> loader) {
            //TODO invalidate Trailer data
        }
    };

    private LoaderManager.LoaderCallbacks<ReviewList> reviewLoaderListener = new LoaderManager.LoaderCallbacks<ReviewList>() {

        @Override
        public Loader<ReviewList> onCreateLoader(int id, Bundle args) {
            if (id == REVIEW_LOADER_ID) {
                if (args != null && args.getString(KEY_MOVIE_ID) != null) {
                    return new GetReviewsLoader(getBaseContext(), MovieAPIUtil.buildReviewURL(args.getString(KEY_MOVIE_ID)));
                }
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ReviewList> loader, ReviewList data) {
            if (data == null) {
                //TODO tratar erro
            } else if (data.getResults() == null || data.getResults().size() == 0) {
                //TODO tratar sem review
            } else {
                ((MovieDetailAdapter) mRecyclerView.getAdapter()).setReviewData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ReviewList> loader) {
            //TODO invalidate Review data
        }
    };
}
