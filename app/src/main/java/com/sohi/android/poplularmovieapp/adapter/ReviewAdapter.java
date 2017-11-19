package com.sohi.android.poplularmovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohi.android.poplularmovieapp.NetworkUtils;
import com.sohi.android.poplularmovieapp.R;
import com.sohi.android.poplularmovieapp.model.MovieObj;
import com.sohi.android.poplularmovieapp.model.MovieReview;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

/**
 * Created by siav on 11/12/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private Context mContext;
    private List<MovieReview> movieReviews;
    public ReviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setReviewObjs(List<MovieReview> reviews) {
        this.movieReviews = reviews;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        if(null == this.movieReviews) return 0;
        return this.movieReviews.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder  {
        public TextView textView;
        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.reviewItemText);

        }

        public void bindData(int index){
             textView.setText(movieReviews.get(index).Content);
//            URL url = NetworkUtils.buildUrlForReviewPosterLarge(ReviewObjs.get(index).getPoster_url());
//            Picasso.with(mContext).load(url.toString()).into(imageView);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            imageView.setAdjustViewBounds(true);
        }


    }
}
