package br.com.marcioikeda.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.MovieList;
import br.com.marcioikeda.popularmovies.util.GetMoviesLoader;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

import static android.view.View.GONE;
import static br.com.marcioikeda.popularmovies.MovieDetailActivity.KEY_EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<MovieList>{

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private ProgressBar mProgressBar;
    private Movie[] mMovieData;

    private static int POPULAR_LOADER_ID = 1;
    private static int TOPRATED_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_bar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        SharedPreferences sf = getPreferences(Context.MODE_PRIVATE);
        String filterValue = sf.getString(getString(R.string.sf_filter_movies_key), getString(R.string.sf_filter_default));
        if (filterValue.equalsIgnoreCase(getString(R.string.sf_filter_popular))) {
            loadPopularMovies(false);
        } else if (filterValue.equalsIgnoreCase(getString(R.string.sf_filter_toprated))) {
            loadTopRatedMovies(false);
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void loadPopularMovies(boolean forceReload) {
        SharedPreferences sf = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(getString(R.string.sf_filter_movies_key), getString(R.string.sf_filter_popular));
        editor.commit();
        mRecyclerView.setVisibility(GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (forceReload) {
            getSupportLoaderManager().restartLoader(POPULAR_LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().initLoader(POPULAR_LOADER_ID, null, this);
        }
    }

    private void loadTopRatedMovies(boolean forceReload) {
        SharedPreferences sf = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(getString(R.string.sf_filter_movies_key), getString(R.string.sf_filter_toprated));
        editor.commit();
        mRecyclerView.setVisibility(GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (forceReload) {
            getSupportLoaderManager().restartLoader(TOPRATED_LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().initLoader(TOPRATED_LOADER_ID, null, this);
        }
    }

    private void loadMoviesIntoUI(MovieList list) {
        if (list != null) {
            mMovieData = list.getResults();
            mAdapter.setMoviesData(mMovieData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_popular) {
            loadPopularMovies(true);
            return true;
        } else if (item.getItemId() == R.id.action_toprated) {
            loadTopRatedMovies(true);
            return true;
        }
        return false;
    }

    @Override
    public void onListItemClick(ImageView imageView, int position) {
        //Toast.makeText(this, String.valueOf(position) + " - " + mMovieData[position], Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_EXTRA_MOVIE, mMovieData[position]);
        intent.putExtras(extras);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, imageView, getString(R.string.transition_movie_post));
        startActivity(intent, options.toBundle());
    }

    @Override
    public Loader<MovieList> onCreateLoader(int id, Bundle args) {
        if (id == POPULAR_LOADER_ID) {
            return new GetMoviesLoader(this, MovieAPIUtil.buildPopularMoviesURL());
        } else if (id == TOPRATED_LOADER_ID) {
            return new GetMoviesLoader(this, MovieAPIUtil.buildTopRatedMoviesURL());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<MovieList> loader, MovieList data) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(GONE);
        if (data != null) {
            loadMoviesIntoUI(data);
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.error_network_getmovies),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieList> loader) {
        mAdapter.setMoviesData(null);
    }
}
