package br.com.marcioikeda.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.MovieList;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

import static android.view.View.GONE;
import static br.com.marcioikeda.popularmovies.MovieDetailActivity.KEY_EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener{

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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        loadPopularMovies();
    }

    private void loadPopularMovies() {
        new GetMoviesTask().execute(MovieAPIUtil.buildPopularMoviesURL());
    }

    private void loadTopRatedMovies() {
        new GetMoviesTask().execute(MovieAPIUtil.buildTopRatedMoviesURL());
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

    private class GetMoviesTask extends AsyncTask<URL, Void, MovieList> {

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieList doInBackground(URL... params) {
            if (params.length == 0) {
                return null;
            }

            URL url = params[0];
            String jsonString;
            try {
                jsonString = MovieAPIUtil.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if (jsonString != null) {
                Gson gson = new Gson();
                return gson.fromJson(jsonString, MovieList.class);
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieList list) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(GONE);
            if (list != null) {
                loadMoviesIntoUI(list);
            } else {
                Toast.makeText(MainActivity.this, "Error fetching movies", Toast.LENGTH_LONG).show();
            }
        }
    }
}
