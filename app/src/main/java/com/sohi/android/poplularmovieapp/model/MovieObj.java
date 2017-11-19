package com.sohi.android.poplularmovieapp.model;

import com.sohi.android.poplularmovieapp.model.MovieReview;
import com.sohi.android.poplularmovieapp.model.MovieTrailer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static android.R.attr.rating;

/**
 * Created by siav on 2/26/17.
 */

public class MovieObj implements Serializable{
    /*original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date*/

    public List<MovieReview> getReviewList() {
        return reviewList;
    }

    public List<MovieTrailer> getTrailerLists() {
        return trailerLists;
    }

    private List<MovieReview> reviewList;

    public void setReviewList(List<MovieReview> reviewList) {
        this.reviewList = reviewList;
    }

    public void setTrailerLists(List<MovieTrailer> trailerLists) {
        this.trailerLists = trailerLists;
    }

    private List<MovieTrailer> trailerLists;

    public String getOrignal_title() {
        return orignal_title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public String getOverview() {
        return overview;
    }

    public float getRating() {
        return rating;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }

    // All setter method
    public void setOrignal_title(String orignal_title) {
        this.orignal_title = orignal_title;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    private String orignal_title;
    private String poster_url;
    private String overview;
    private float rating;
    private Date release_date;
    private int movie_id;

    public int getLoad_id() {
        return load_id;
    }

    public void setLoad_id(int load_id) {
        this.load_id = load_id;
    }

    private int load_id; // this id will be used to sort the movie based on popluarity.
    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    private float popularity;

}
