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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sohi.android.poplularmovieapp.adapter.MovieAdapter;
import com.sohi.android.poplularmovieapp.data.FavMovieContract;
import com.sohi.android.poplularmovieapp.data.FavMovieDbHelper;
import com.sohi.android.poplularmovieapp.model.MovieObj;
import com.sohi.android.poplularmovieapp.model.MovieReview;
import com.sohi.android.poplularmovieapp.utils.NetworkUtils;
import com.sohi.android.poplularmovieapp.utils.OpenMovieJsonUtils;
import com.sohi.android.poplularmovieapp.utils.SpacesItemDecoration;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String POPLULAR_MOVIE_FILTER = "POPLULAR_MOVIE_FILTER";
    private static final String TOP_RATED_MOVIE_FILTER = "TOP_RATED_MOVIE_FILTER";
    private static final String FAV_MOVIE_FILTER = "FAV_MOVIE_FILTER";
    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String MOVIES_KEY = "movies";
    private String mSelectedFilter = POPLULAR_MOVIE_FILTER;
    private TextView mErrorMessageDisplay;
    private SQLiteDatabase mDb;
    private ProgressBar mLoadingIndicator;
    private android.support.v7.widget.RecyclerView mRecyclerView;
    public MovieAdapter mMovieAdapter;
    private MovieObj selectedMovie;
    private List<MovieObj> mMovieObjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        mRecyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerview_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mMovieAdapter = new MovieAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                mSelectedFilter = savedInstanceState.getString(SORT_SETTING_KEY);
            }
            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mMovieObjs = (ArrayList<MovieObj>) savedInstanceState.getSerializable(MOVIES_KEY);
                setUpDB();
                mMovieAdapter.setMovieObjs(mMovieObjs);
            } else {
                loadData();
            }

        } else {
            loadData();
        }
    }

    private void loadData() {
        setUpDB();
        LoadMovieData(mSelectedFilter);
    }

    private void setUpDB() {
        FavMovieDbHelper dbHelper = new FavMovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void LoadMovieData(String filter) {
        if (isOnline()) {
            showMovieDataView();
            new FeatchMovieTask().execute(filter);
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet_loading_movie_from_local_dB, Toast.LENGTH_LONG).show();
            LoadMoviesFromLocalDB();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_SETTING_KEY, mSelectedFilter);
        if (mMovieObjs != null) {
            outState.putSerializable(MOVIES_KEY, (Serializable) mMovieObjs);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            if (this.mSelectedFilter != FAV_MOVIE_FILTER) {
                mMovieAdapter.setMovieObjs(null);
                LoadMovieData(this.mSelectedFilter);
            }
            return true;
        } else if (id == R.id.action_sort_onPolularMovie) {
            mMovieAdapter.setMovieObjs(null);
            this.mSelectedFilter = POPLULAR_MOVIE_FILTER;
            LoadMovieData(POPLULAR_MOVIE_FILTER);
            return true;
        } else if (id == R.id.action_sort_onHighestRatedMovie) {
            mMovieAdapter.setMovieObjs(null);
            this.mSelectedFilter = TOP_RATED_MOVIE_FILTER;
            LoadMovieData(TOP_RATED_MOVIE_FILTER);
            return true;
        } else if (id == R.id.action_show_onFavMovies) {
            mMovieAdapter.setMovieObjs(null);
            this.mSelectedFilter = FAV_MOVIE_FILTER;
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
            MovieObj movieObject = gson.fromJson(json, new TypeToken<MovieObj>() {
            }.getType());
            movieObjs.add(movieObject);
        }
        mMovieAdapter.setMovieObjs(null);
        mMovieAdapter.setMovieObjs(movieObjs);
        mMovieAdapter.notifyDataSetChanged();
        this.mMovieObjs = movieObjs;

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
        this.selectedMovie = movieObj;
        if (mSelectedFilter != FAV_MOVIE_FILTER) {
            new FeatchMovieDetails().execute();
        } else {
            //all the data available from local dB
            Class movieDetailClass_Instance = MovieDetailActivity.class;
            Intent intentToStartMovieDetailActivity = new Intent(MainActivity.this, movieDetailClass_Instance);
            intentToStartMovieDetailActivity.putExtra("selectedMovieObject", selectedMovie);
            startActivity(intentToStartMovieDetailActivity);
        }
    }

    public class FeatchMovieDetails extends AsyncTask<Void, Void, Void> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... params) {
            String jsonMovieResponse = "";
            try {
                //fetch movie reviews
                URL movieReviewsUrl = NetworkUtils.buildUrlForMoviesReviews(getString(R.string.API_KEY),
                        String.valueOf(selectedMovie.getMovie_id()));
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsUrl);
                List<MovieReview> list = OpenMovieJsonUtils.getReviewListFromJsonString(jsonMovieResponse);
                //handle no review case
                if (list.size() == 0) {
                    MovieReview emptyReview = new MovieReview();
                    emptyReview.setContent(getString(R.string.no_movie_available_txt));
                    list.add(emptyReview);
                }
                selectedMovie.setReviewList(list);

                //fetch movie trailer
                movieReviewsUrl = NetworkUtils.buildUrlForMovieTrailerKey(getString(R.string.API_KEY),
                        String.valueOf(selectedMovie.getMovie_id()));
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsUrl);
                selectedMovie.setTrailerLists(OpenMovieJsonUtils.getTrailerListFromJsonString(jsonMovieResponse));
                selectedMovie.setMovieDetailLoaded(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Class movieDetailClass_Instance = MovieDetailActivity.class;
            Intent intentToStartMovieDetailActivity = new Intent(MainActivity.this, movieDetailClass_Instance);
            intentToStartMovieDetailActivity.putExtra("selectedMovieObject", selectedMovie);
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            startActivity(intentToStartMovieDetailActivity);
        }
    }


    public class FeatchMovieTask extends AsyncTask<String, Void, List<MovieObj>> {
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

                if (filter == POPLULAR_MOVIE_FILTER) {
                    URL movieRequestUrl = NetworkUtils.buildUrlForPopularMovies(getString(R.string.API_KEY));

                    jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                } else if (filter == TOP_RATED_MOVIE_FILTER) {
                    URL movieRequestUrl = NetworkUtils.buildUrlForTopRatedMovies(getString(R.string.API_KEY));

                    jsonMovieResponse = NetworkUtils
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
                mMovieObjs = movieObjs;
                showMovieDataView();
                mMovieAdapter.setMovieObjs(mMovieObjs);
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
