package com.sohi.android.poplularmovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.sohi.android.poplularmovieapp.NetworkUtils;
import com.sohi.android.poplularmovieapp.R;
import com.sohi.android.poplularmovieapp.model.MovieObj;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by siav on 11/12/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private Context mContext;
    private List<MovieObj> movieObjs;
    private final MovieAdapterOnClickHandler mClickHanlder;
    public MovieAdapter(Context mContext, MovieAdapterOnClickHandler mClickHanlder) {
        this.mContext = mContext;
        this.mClickHanlder = mClickHanlder;
    }

    public void setMovieObjs(List<MovieObj> movieObjs) {
        this.movieObjs = movieObjs;
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.image_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        if(null == this.movieObjs) return 0;
        return this.movieObjs.size();
    }

    public interface MovieAdapterOnClickHandler {
        void onMovieClick(MovieObj movieObj);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView1);
            imageView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bindData(int index){
            URL url = NetworkUtils.buildUrlForMoviePosterLarge(movieObjs.get(index).getPoster_url());
            Picasso.with(mContext).load(url.toString()).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieObj movieObj = movieObjs.get(adapterPosition);
            mClickHanlder.onMovieClick(movieObj);
        }
    }
}
