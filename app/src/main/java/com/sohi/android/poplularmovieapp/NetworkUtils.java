package com.sohi.android.poplularmovieapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.R.attr.apiKey;

/**
 * Created by siav on 2/26/17.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_API_BASE_URL="http://api.themoviedb.org/3/";
    private static final String POPLULAR_MOVIE_API_URL="http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String TOP_RATED_MOVIE_API_URL="http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String MOVIE_DETAIL_URL="http://api.themoviedb.org/3/movie/";

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";
    /* The number of days we want our API to return */
    private static final int numDays = 14;

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

    public static URL buildUrlForPopularMovies(String api_Key) {
        Uri builtUri = Uri.parse(POPLULAR_MOVIE_API_URL + api_Key).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlForTopRatedMovies(String api_Key) {
        Uri builtUri = Uri.parse(TOP_RATED_MOVIE_API_URL + api_Key).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlForMoviePosterLarge(String posterId) {
        Uri builtUri = Uri.parse(AppConstants.MovieDB_Base_Url_For_Image + AppConstants.MovieDB_Image_Size_w500 + posterId).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlForMoviePosterMedium(String posterId){
        Uri builtUri = Uri.parse(AppConstants.MovieDB_Base_Url_For_Image + AppConstants.MovieDB_Image_Size_w154 + posterId).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlForMoviePosterSmallOne(String posterId){
        Uri builtUri = Uri.parse(AppConstants.MovieDB_Base_Url_For_Image + AppConstants.MovieDB_Image_Size_w92 + posterId).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
