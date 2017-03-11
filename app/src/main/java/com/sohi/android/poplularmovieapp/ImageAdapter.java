package com.sohi.android.poplularmovieapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public void setMovieObjs(List<MovieObj> movieObjs) {
        this.movieObjs = movieObjs;
    }

    private List<MovieObj> movieObjs;

    public ImageAdapter(Context c) {
        mContext = c;
        movieObjs = new ArrayList<MovieObj>();
    }

    @Override
    public int getCount() {
        return movieObjs.size();
    }

    @Override
    public MovieObj getItem(int position) {
        return movieObjs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movieObjs.get(position).getMovie_id();
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
      /*  ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }*/

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, create a new ImageView
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        URL url = NetworkUtils.buildUrlForMoviePosterLarge(movieObjs.get(position).getPoster_url());
        Picasso.with(mContext).load(url.toString()).into(imageView);


        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    public List<MovieObj> getMovieObjs() {
        return movieObjs;
    }
}