package com.sohi.android.poplularmovieapp.model;

/**
 * Created by siav on 3/1/17.
 */

public class AppConstants {
    public final static String MovieDB_Base_Url_For_Image = "https://image.tmdb.org/t/p/";
    public final static String MovieDB_Image_Size_w500 = "w500/";
    public final static String MovieDB_Image_Size_W342 = "w342/";
    public final static String MovieDB_Image_Size_w92 = "w92/";
    public final static String MovieDB_Image_Size_w154 = "w154/";

    public static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POPLULAR_MOVIE_API_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String MOVIE_TRAILER_KEY_LIST_URL = "http://api.themoviedb.org/3/movie/MOVIE_ID/videos?api_key=";
    public static final String MOVIE_TRAILER_LIST_URL = "https://www.youtube.com/watch?v=";
    public static final String MOVIE_REVIEWS_LIST_URL = "http://api.themoviedb.org/3/movie/MOVIE_ID/reviews?api_key=";
    public static final String MOVIE_REVIEW_LIST_URL = "http://api.themoviedb.org/3/movie/MOVIE_ID/reviews?api_key=";
    public static final String VIDEO_PARAMETER = "&append_to_response=videos";
    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String TOP_RATED_MOVIE_API_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    public static final String MOVIE_DETAIL_URL = "https://api.themoviedb.org/3/movie/";

    /* The format we want our API to return */
    public static final String format = "json";
    /* The units we want our API to return */
    public static final String units = "metric";
    /* The number of days we want our API to return */
    public static final int numDays = 14;

    public final static String QUERY_PARAM = "q";
    public final static String LAT_PARAM = "lat";
    public final static String LON_PARAM = "lon";
    public final static String FORMAT_PARAM = "mode";
    public final static String UNITS_PARAM = "units";
    public final static String DAYS_PARAM = "cnt";
}
