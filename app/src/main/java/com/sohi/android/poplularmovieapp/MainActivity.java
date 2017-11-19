package com.sohi.android.poplularmovieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sohi.android.poplularmovieapp.adapter.MovieAdapter;
import com.sohi.android.poplularmovieapp.data.FavMovieContract;
import com.sohi.android.poplularmovieapp.data.FavMovieDbHelper;
import com.sohi.android.poplularmovieapp.model.MovieObj;
import com.sohi.android.poplularmovieapp.utils.SpacesItemDecoration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String POPLULAR_MOVIE_FILTER = "POPLULAR_MOVIE_FILTER";
    private static final String TOP_RATED_MOVIE_FILTER = "TOP_RATED_MOVIE_FILTER";
    private TextView mErrorMessageDisplay;
    private SQLiteDatabase mDb;
    private ProgressBar mLoadingIndicator;
    private android.support.v7.widget.RecyclerView mRecyclerView;
    public MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (android.support.v7.widget.RecyclerView)findViewById(R.id.recyclerview_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mMovieAdapter = new MovieAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        setUpDB();
        LoadMovieData(POPLULAR_MOVIE_FILTER);
    }

    private void setUpDB() {
        FavMovieDbHelper dbHelper = new FavMovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
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
        mRecyclerView.setVisibility(View.VISIBLE);
        //mGridView.setVisibility(View.VISIBLE);
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
            mMovieAdapter.setMovieObjs(null);
           LoadMovieData(POPLULAR_MOVIE_FILTER);
            return true;
        }
        else if(id ==R.id.action_sort_onPolularMovie) {
           mMovieAdapter.setMovieObjs(null);
           LoadMovieData(POPLULAR_MOVIE_FILTER);
           return true;
       }
       else if(id ==R.id.action_sort_onHighestRatedMovie) {
           mMovieAdapter.setMovieObjs(null);
           LoadMovieData(TOP_RATED_MOVIE_FILTER);
           return true;
       }
       else if(id ==R.id.action_show_onFavMovies) {
           LoadMoviesFromLocalDB();
           return true;
       }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoviesFromLocalDB() {
        Cursor cursor = getAllFavMovieObject();
        List<MovieObj> movieObjs = new ArrayList<>();
        while (cursor.moveToNext()) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_FAVMOVIEOBJ));
            String json = new String(blob);
            Gson gson = new Gson();
            MovieObj movieObject = gson.fromJson(json, new TypeToken<MovieObj>(){}.getType());
            movieObjs.add(movieObject);
        }
        mMovieAdapter.setMovieObjs(null);
        mMovieAdapter.setMovieObjs(movieObjs);
        mMovieAdapter.notifyDataSetChanged();
    }

        private Cursor getAllFavMovieObject() {
        return mDb.query(
                FavMovieContract.FavMovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavMovieContract.FavMovieEntry.COLUMN_TIMESTAMP
        );
    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onMovieClick(MovieObj movieObj) {
        Class movieDetailClass_Instance = MovieDetailActivity.class;
        Intent intentToStartMovieDetailActivity = new Intent(MainActivity.this, movieDetailClass_Instance);
        intentToStartMovieDetailActivity.putExtra("selectedMovieObject",movieObj);
        startActivity(intentToStartMovieDetailActivity);
    }


    public class FeatchMovieTask extends AsyncTask<String,Void, List<MovieObj>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.INVISIBLE);
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
                mMovieAdapter.setMovieObjs(movieObjs);
                mRecyclerView.getLayoutManager().scrollToPosition(0);
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
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
