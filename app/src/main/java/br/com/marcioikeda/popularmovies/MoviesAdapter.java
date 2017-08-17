package br.com.marcioikeda.popularmovies;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by marcio.ikeda on 17/08/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final ListItemClickListener mOnClickListener;

    private String[] mMoviesData;

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    public MoviesAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void setMoviesData(String[] data) {
        mMoviesData = data;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.item_movie_list;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(holder.movieView.getContext()).load(mMoviesData[position]).into(holder.movieView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView movieView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieView = (ImageView) itemView.findViewById(R.id.iv_movie_post);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
