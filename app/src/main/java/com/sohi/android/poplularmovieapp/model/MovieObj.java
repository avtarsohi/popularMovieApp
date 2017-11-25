package com.sohi.android.poplularmovieapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by siav on 2/26/17.
 */

public class MovieObj implements Serializable {

    private List<MovieReview> reviewList;
    private List<MovieTrailer> trailerLists;
    private String orignal_title;
    private String poster_url;
    private String overview;
    private float rating;
    private Date release_date;
    private int movie_id;
    private boolean isMovieDetailLoaded;
    private boolean isAddedToFavMovieList;
    private int load_id; // this id will be used to sort the movie based on popluarity.
    private float popularity;

    public List<MovieReview> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<MovieReview> reviewList) {
        this.reviewList = reviewList;
    }

    public List<MovieTrailer> getTrailerLists() {
        return trailerLists;
    }

    public void setTrailerLists(List<MovieTrailer> trailerLists) {
        this.trailerLists = trailerLists;
    }

    public String getOrignal_title() {
        return orignal_title;
    }

    // All setter method
    public void setOrignal_title(String orignal_title) {
        this.orignal_title = orignal_title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public boolean isAddedToFavMovieList() {
        return isAddedToFavMovieList;
    }

    public void setAddedToFavMovieList(boolean addedToFavMovieList) {
        isAddedToFavMovieList = addedToFavMovieList;
    }

    public boolean isMovieDetailLoaded() {
        return isMovieDetailLoaded;
    }

    public void setMovieDetailLoaded(boolean movieDetailLoaded) {
        isMovieDetailLoaded = movieDetailLoaded;
    }

    public int getLoad_id() {
        return load_id;
    }

    public void setLoad_id(int load_id) {
        this.load_id = load_id;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

}
