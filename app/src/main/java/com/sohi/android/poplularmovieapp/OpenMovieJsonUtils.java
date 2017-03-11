package com.sohi.android.poplularmovieapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.format;

/**
 * Created by siav on 2/27/17.
 */

public class OpenMovieJsonUtils {
    public static List<MovieObj> getSimpleMovieStringsFromJson(Context context, String inputJsonStr)
            throws JSONException {

        /* Movie information. */
        final String OWM_LIST = "results";

        List<MovieObj> movieList= new ArrayList<MovieObj>();

        final String OWM_Movie = "Movie";
        final String OWM_MESSAGE_CODE = "results";

        /* String array to hold each day's Movie String */
        String[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(inputJsonStr);

        JSONArray MovieArray = movieJson.getJSONArray(OWM_LIST);

        parsedMovieData = new String[MovieArray.length()];

        long localDate = System.currentTimeMillis();

        for (int i = 0; i < MovieArray.length(); i++) {
            MovieObj movie = new MovieObj();

            /* Get the JSON object representing the movie */
            JSONObject movieObj = MovieArray.getJSONObject(i);
            movie.setPoster_url(movieObj.getString("poster_path"));
            movie.setMovie_id(movieObj.getInt("id"));

            try
            {
                DateFormat format = new SimpleDateFormat("yyyy-dd-mm", Locale.ENGLISH);
                movie.setRelease_date(format.parse(movieObj.getString("release_date")));
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            movie.setOrignal_title(movieObj.getString("original_title"));
            movie.setPoster_url(movieObj.getString("poster_path"));
            movie.setRating(Float.valueOf(movieObj.getString("vote_average")));
            movie.setPopularity(Float.valueOf(movieObj.getString("popularity")));
            movie.setOverview(movieObj.getString("overview"));

            movieList.add(movie);
        }

        return movieList;
    }

}
