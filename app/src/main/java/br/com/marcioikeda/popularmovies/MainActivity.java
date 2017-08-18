package br.com.marcioikeda.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import br.com.marcioikeda.popularmovies.model.MovieList;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView mRecyclerView;
    String[] movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        MoviesAdapter adapter = new MoviesAdapter(this);

        movieData = new String[]{"q0R4crx2SehcEEQEkYObktdeFy.jpg", "tWqifoYuwLETmmasnGHO7xBjEtt.jpg"};

        adapter.setMoviesData(movieData);
        mRecyclerView.setAdapter(adapter);

    }

    public void loadPopularMovies() {
        new GetMoviesTask().execute(MovieAPIUtil.buildPopularMoviesURL());
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, String.valueOf(position) + " - " + movieData[position], Toast.LENGTH_SHORT).show();
    }

    public class GetMoviesTask extends AsyncTask<URL, Void, MovieList> {

        @Override
        protected MovieList doInBackground(URL... params) {
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
                MovieList result = gson.fromJson(jsonString, MovieList.class);
                return result;
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieList list) {
            if (list != null) {

            }
        }
    }
}
