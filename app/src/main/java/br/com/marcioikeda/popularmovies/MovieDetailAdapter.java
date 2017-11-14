package br.com.marcioikeda.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;

/**
 * Created by marcio.ikeda on 14/11/2017.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Movie mMovieData;
    private String mTrailerData;

    public void setMovieData(Movie data) {
        mMovieData = data;
        notifyDataSetChanged();
    }

    public void setTrailerData(String json) {
        mTrailerData = json;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.content_movie_detail, parent, false);
                viewHolder = new MovieViewHolder0(v1);
                break;
            case 1:
                View v2 = inflater.inflate(R.layout.content_movie_detail_text, parent, false);
                viewHolder = new MovieTextViewHolder(v2);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                MovieViewHolder0 viewHolder0 = (MovieViewHolder0) holder;
                Picasso.with(viewHolder0.moviePostImageView.getContext()).load(MovieAPIUtil
                        .buildImageUri(mMovieData.getPoster_path().substring(1), MovieAPIUtil.IMG_SIZE_185))
                        .into(viewHolder0.moviePostImageView);
                viewHolder0.voteAverageTextView.setText(viewHolder0.voteAverageTextView.getContext().getResources().getString(R.string.vote_average, mMovieData.getVote_average()));
                viewHolder0.releaseYearTextView.setText(mMovieData.getRelease_date().substring(0, 4));
                viewHolder0.voteCountTextView.setText(mMovieData.getVote_count());
                viewHolder0.originalTitleTextView.setText(viewHolder0.originalTitleTextView.getContext().getResources().getString(R.string.original_title, mMovieData.getOriginal_title()));
                viewHolder0.overviewTextView.setText(mMovieData.getOverview());
                break;
            case 1:
                MovieTextViewHolder viewHolder1 = (MovieTextViewHolder) holder;
                viewHolder1.textView.setText(mTrailerData);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class MovieViewHolder0 extends RecyclerView.ViewHolder {

        ImageView moviePostImageView;
        TextView voteAverageTextView;
        TextView releaseYearTextView;
        TextView voteCountTextView;
        TextView originalTitleTextView;
        TextView overviewTextView;

        public MovieViewHolder0(View itemView) {
            super(itemView);
            moviePostImageView = (ImageView) itemView.findViewById(R.id.iv_movie_detail_post);
            voteAverageTextView = (TextView) itemView.findViewById(R.id.tv_vote_average);
            releaseYearTextView = (TextView) itemView.findViewById(R.id.tv_release_year);
            voteCountTextView = (TextView) itemView.findViewById(R.id.tv_vote_count);
            originalTitleTextView = (TextView) itemView.findViewById(R.id.tv_original_title);
            overviewTextView = (TextView) itemView.findViewById(R.id.tv_overview);
        }

    }

    class MovieTextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MovieTextViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_trailer_json);
        }
    }


}
