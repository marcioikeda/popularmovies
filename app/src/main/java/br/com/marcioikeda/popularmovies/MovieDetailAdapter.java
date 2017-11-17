package br.com.marcioikeda.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.marcioikeda.popularmovies.model.Movie;
import br.com.marcioikeda.popularmovies.model.Review;
import br.com.marcioikeda.popularmovies.model.ReviewList;
import br.com.marcioikeda.popularmovies.model.Trailer;
import br.com.marcioikeda.popularmovies.model.TrailerList;
import br.com.marcioikeda.popularmovies.util.MovieAPIUtil;
import br.com.marcioikeda.popularmovies.util.Util;

/**
 * Created by marcio.ikeda on 14/11/2017.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "PopularMovies";

    private Movie mMovieData;
    private TrailerList mTrailerData;
    private ReviewList mReviewData;

    private List<Object> items;

    private final int MOVIE = 0;
    private final int TRAILER = 1;
    private final int REVIEW = 2;
    private final int TITLE = 3;

    public void setMovieData(Movie data) {
        mMovieData = data;
        syncItems();
    }

    public void setTrailerData(TrailerList list) {
        mTrailerData = list;
        syncItems();
    }

    public void setReviewData(ReviewList list) {
        mReviewData = list;
        syncItems();
    }

    private void syncItems() {
        items = new ArrayList<>();
        if (mMovieData != null) {
            items.add(mMovieData);
        }
        if (mTrailerData != null && mTrailerData.getResults() != null && mTrailerData.getResults().size() > 0) {
            items.addAll(mTrailerData.getResults());
        }
        if (mReviewData != null && mReviewData.getResults() != null && mReviewData.getResults().size() > 0) {
            items.addAll(mReviewData.getResults());
        }
        Log.d(TAG, "syncItems");
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Movie) {
            return MOVIE;
        } else if (items.get(position) instanceof Trailer) {
            return TRAILER;
        } else if (items.get(position) instanceof Review) {
            return REVIEW;
        }
        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {
            case MOVIE:
                View v1 = inflater.inflate(R.layout.content_movie_detail, parent, false);
                viewHolder = new MovieViewHolder0(v1);
                Log.d(TAG, "onCreateViewHolder Movie");
                break;
            case TITLE:
                View v2 = inflater.inflate(R.layout.content_movie_detail_text, parent, false);
                viewHolder = new MovieTextViewHolder(v2);
                Log.d(TAG, "onCreateViewHolder Title");
                break;
            case TRAILER:
                View v3 = inflater.inflate(R.layout.content_movie_detail_trailer, parent, false);
                viewHolder = new TrailerViewHolder(v3);
                Log.d(TAG, "onCreateViewHolder Trailer");
                break;
            case REVIEW:
                View v4 = inflater.inflate(R.layout.content_movie_detail_review, parent, false);
                viewHolder = new ReviewViewHolder(v4);
                Log.d(TAG, "onCreateViewHolder Review");
                break;
            default:
                View v5 = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolder(v5) {};
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) {
            Log.d(TAG, "holder null");
            return;
        }
        switch (holder.getItemViewType()) {
            case MOVIE:
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
            case TITLE:
                MovieTextViewHolder viewHolder1 = (MovieTextViewHolder) holder;
                if (mTrailerData != null) {
                    viewHolder1.textView.setText(mTrailerData.toString());
                }
                break;
            case TRAILER:
                TrailerViewHolder viewHolder2 = (TrailerViewHolder) holder;
                String trailerName = ((Trailer) items.get(position)).getName();
                viewHolder2.textView.setText(trailerName);
                break;
            case REVIEW:
                ReviewViewHolder viewHolder3 = (ReviewViewHolder) holder;
                String author = ((Review) items.get(position)).getAuthor();
                viewHolder3.authorTextView.setText(author);
                String content = ((Review) items.get(position)).getContent();
                viewHolder3.contentTextView.setText(content);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MovieViewHolder0 extends ViewHolder {

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

    class MovieTextViewHolder extends ViewHolder {
        TextView textView;

        public MovieTextViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_trailer_json);
        }
    }

    class TrailerViewHolder extends ViewHolder implements View.OnClickListener{
        TextView textView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = ((Trailer) items.get(getAdapterPosition()));
            if (trailer.getSite().equalsIgnoreCase("Youtube")) {
                Util.watchYoutubeVideo(v.getContext(), trailer.getKey());
            }
        }
    }

    class ReviewViewHolder extends ViewHolder {
        TextView authorTextView;
        TextView contentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }

}
