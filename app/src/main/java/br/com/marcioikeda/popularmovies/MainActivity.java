package br.com.marcioikeda.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, String.valueOf(position) + " - " + movieData[position], Toast.LENGTH_SHORT).show();
    }
}
