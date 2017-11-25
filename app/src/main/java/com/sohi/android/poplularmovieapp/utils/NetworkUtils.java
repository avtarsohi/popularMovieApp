package com.sohi.android.poplularmovieapp.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sohi.android.poplularmovieapp.model.AppConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by siav on 2/26/17.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static URL buildUrlForPopularMovies(String api_Key) {
        Uri builtUri = Uri.parse(AppConstants.POPLULAR_MOVIE_API_URL + api_Key).buildUpon().build();

        return getUrl(builtUri);
    }

    public static URL buildUrlForMoviesReviews(String api_Key, String id) {
        Uri builtUri = Uri.parse(AppConstants.MOVIE_REVIEWS_LIST_URL.replace(AppConstants.MOVIE_ID, id) + api_Key).buildUpon().build();

        return getUrl(builtUri);
    }

    public static URL buildUrlForMoviesYoutubeTrailer(String videoKey) {
        Uri builtUri = Uri.parse(AppConstants.MOVIE_TRAILER_LIST_URL + videoKey).buildUpon().build();

        return getUrl(builtUri);
    }

    public static URL buildUrlForMovieTrailerKey(String api_Key, String id) {
        String url = AppConstants.MOVIE_TRAILER_KEY_LIST_URL.replace(AppConstants.MOVIE_ID, id) + api_Key;
        Uri builtUri = Uri.parse(url).buildUpon().build();

        return getUrl(builtUri);
    }

    public static URL buildUrlForTopRatedMovies(String api_Key) {
        Uri builtUri = Uri.parse(AppConstants.TOP_RATED_MOVIE_API_URL + api_Key).buildUpon().build();

        return getUrl(builtUri);
    }

    @Nullable
    private static URL getUrl(Uri builtUri) {
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

        return getUrl(builtUri);
    }

    public static URL buildUrlForMovieDetailsPosterLarge(String posterId) {
        Uri builtUri = Uri.parse(AppConstants.MovieDB_Base_Url_For_Image + AppConstants.MovieDB_Image_Size_W342 + posterId).buildUpon().build();

        return getUrl(builtUri);
    }

    public static URL buildUrlForMoviePosterMedium(String posterId) {
        Uri builtUri = Uri.parse(AppConstants.MovieDB_Base_Url_For_Image + AppConstants.MovieDB_Image_Size_w154 + posterId).buildUpon().build();

        return getUrl(builtUri);
    }

    public static URL buildUrlForMoviePosterSmallOne(String posterId) {
        Uri builtUri = Uri.parse(AppConstants.MovieDB_Base_Url_For_Image + AppConstants.MovieDB_Image_Size_w92 + posterId).buildUpon().build();

        return getUrl(builtUri);
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
