package com.sohi.android.poplularmovieapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohi.android.poplularmovieapp.R;
import com.sohi.android.poplularmovieapp.model.MovieTrailer;

import java.util.List;

/**
 * Created by siav on 11/12/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private final TrailerAdapterOnClickHandler mClickHanlder;
    private Context mContext;
    private List<MovieTrailer> movieTrailers;

    public TrailerAdapter(Context mContext, TrailerAdapterOnClickHandler mClickHanlder) {
        this.mContext = mContext;
        this.mClickHanlder = mClickHanlder;
    }

    public void setTrailerObjs(List<MovieTrailer> Trailers) {
        this.movieTrailers = Trailers;
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        if (null == this.movieTrailers) return 0;
        return this.movieTrailers.size();
    }

    public interface TrailerAdapterOnClickHandler {
        void onURLClick(MovieTrailer trailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView imageView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.trailerItemText);
            imageView = (ImageView) itemView.findViewById(R.id.play_trailer);
            imageView.setImageResource(android.R.drawable.ic_media_play);

            //android.R.drawable.ic_media_play
            textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            textView.setOnClickListener(this);
            itemView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        public void bindData(int index) {
            textView.setText(movieTrailers.get(index).getDisplayText());
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieTrailer movieObj = movieTrailers.get(adapterPosition);
            mClickHanlder.onURLClick(movieObj);
        }
    }
}
