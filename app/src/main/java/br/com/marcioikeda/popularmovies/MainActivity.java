package br.com.marcioikeda.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.MovieList;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

import static android.view.View.GONE;
import static br.com.marcioikeda.popularmovies.MovieDetailActivity.KEY_EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener, IAsyncTaskListener<MovieList>{

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private ProgressBar mProgressBar;
    private Movie[] mMovieData;

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

        loadPopularMovies();
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

    private void loadPopularMovies() {
        new GetMoviesTask(this).execute(MovieAPIUtil.buildPopularMoviesURL());
    }

    private void loadTopRatedMovies() {
        new GetMoviesTask(this).execute(MovieAPIUtil.buildTopRatedMoviesURL());
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
            loadPopularMovies();
            return true;
        } else if (item.getItemId() == R.id.action_toprated) {
            loadTopRatedMovies();
            return true;
        }
        return false;
    }

    @Override
    public void onListItemClick(int position) {
        //Toast.makeText(this, String.valueOf(position) + " - " + mMovieData[position], Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_EXTRA_MOVIE, mMovieData[position]);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onPreExecute() {
        mRecyclerView.setVisibility(GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onComplete(MovieList result) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(GONE);
        if (result != null) {
            loadMoviesIntoUI(result);
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.error_network_getmovies),
                    Toast.LENGTH_LONG).show();
        }
    }
}
