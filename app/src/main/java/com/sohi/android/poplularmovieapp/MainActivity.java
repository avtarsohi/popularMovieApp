package com.sohi.android.poplularmovieapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.sohi.android.poplularmovieapp.NetworkUtils.getResponseFromHttpUrl;

public class MainActivity extends AppCompatActivity {

    private static final String POPLULAR_MOVIE_FILTER = "POPLULAR_MOVIE_FILTER";
    private static final String TOP_RATED_MOVIE_FILTER = "TOP_RATED_MOVIE_FILTER";
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGridView = (GridView) findViewById(R.id.gridview);
        mImageAdapter = new ImageAdapter(this);
        context = this;

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                //Class destinationClass = DetailActivity.class;
                MovieObj selectedMovie = mImageAdapter.getItem(position);

                Class movieDetailClass_Instance = MovieDetailActivity.class;
                Intent intentToStartMovieDetailActivity = new Intent(context,movieDetailClass_Instance);
                intentToStartMovieDetailActivity.putExtra("selectedMovieObject",selectedMovie);
                startActivity(intentToStartMovieDetailActivity);
            }
        });

       // mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LoadMovieData(POPLULAR_MOVIE_FILTER);
    }

    private void LoadMovieData(String filter)
    {
        if(isOnline()){
            showMovieDataView();
            new FeatchMovieTask().execute(filter);
        }
        else{
            showErrorMessage();
        }
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if(id==R.id.action_refresh){
            mImageAdapter.setMovieObjs(null);
           LoadMovieData(POPLULAR_MOVIE_FILTER);
            return true;
        }
        else if(id ==R.id.action_sort_onPolularMovie) {
           mImageAdapter.setMovieObjs(null);
           LoadMovieData(POPLULAR_MOVIE_FILTER);
           return true;
       }
       else if(id ==R.id.action_sort_onHighestRatedMovie) {
           mImageAdapter.setMovieObjs(null);
           LoadMovieData(TOP_RATED_MOVIE_FILTER);
           return true;
       }
        return super.onOptionsItemSelected(item);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public class FeatchMovieTask extends AsyncTask<String,Void, List<MovieObj>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieObj> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String filter = params[0];


            try {
                String jsonMovieResponse = "";

                if(filter == POPLULAR_MOVIE_FILTER){
                    URL movieRequestUrl = NetworkUtils.buildUrlForPopularMovies(getString(R.string.API_KEY));

                    jsonMovieResponse =  NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                }
                else if(filter == TOP_RATED_MOVIE_FILTER) {
                    URL movieRequestUrl = NetworkUtils.buildUrlForTopRatedMovies(getString(R.string.API_KEY));

                    jsonMovieResponse =  NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                }

                List<MovieObj> simpleJsonMovieData = OpenMovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieObj> movieObjs) {
            super.onPostExecute(movieObjs);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieObjs != null) {
                showMovieDataView();

                mImageAdapter.setMovieObjs(movieObjs);
                mGridView.setAdapter(mImageAdapter);
                mImageAdapter.notifyDataSetChanged();
                //mForecastAdapter.setWeatherData(weatherData);
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mGridView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
