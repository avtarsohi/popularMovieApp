package com.sohi.android.poplularmovieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sohi.android.poplularmovieapp.adapter.ReviewAdapter;
import com.sohi.android.poplularmovieapp.adapter.TrailerAdapter;
import com.sohi.android.poplularmovieapp.model.MovieObj;
import com.sohi.android.poplularmovieapp.model.MovieReview;
import com.sohi.android.poplularmovieapp.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private MovieObj selectedObject;

    @BindView(R.id.original_Title_Text) TextView mOrignalTitle;
    @BindView(R.id.plot_synopsis_Text) TextView mPlotSynopsis;
    @BindView(R.id.user_Rating_Text) TextView mUserRating;
    @BindView(R.id.release_date_Text) TextView mReleaseDate;
    @BindView(R.id.poster_Image) ImageView  mMoviePosterImage;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.recyclerview_reviews) RecyclerView  mReviewRecyclerView;
    @BindView(R.id.trailerview_reviews) RecyclerView  mTrailerRecyclerView;
    @BindView(R.id.movie_detail_panel) ScrollView mMovieDetailPanel;

    private GoogleApiClient client;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private boolean isReviewLoaded;
    private boolean isTrailerLoaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedMovieObject")) {
            selectedObject = (MovieObj) intent.getExtras().getSerializable("selectedMovieObject");
            displayMovieObject();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void displayMovieObject() {
        try {
            if (selectedObject != null) {
                showLoadingView();
                bindAdapter();
                new FeatchMovieReviewList().execute();
                new FeatchMovieTrailerDetails().execute();

                mOrignalTitle.setText(selectedObject.getOrignal_title());
                mUserRating.setText(String.valueOf(selectedObject.getRating()));
                mPlotSynopsis.setText(selectedObject.getOverview());

                //Convert date in a more user readable form
                SimpleDateFormat formater = new SimpleDateFormat("M/d/yyyy");
                String datestring = formater.format(selectedObject.getRelease_date());
                mReleaseDate.setText(datestring);

                URL url = NetworkUtils.buildUrlForMoviePosterMedium(selectedObject.getPoster_url());

                Picasso.with(this).load(url.toString()).into(mMoviePosterImage);
                mMoviePosterImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                mMoviePosterImage.setAdjustViewBounds(true);
            }
        } catch (Exception ex) {
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            ex.printStackTrace();
        }
    }

    private void showLoadingView() {
        isReviewLoaded = false;
        isTrailerLoaded = false;
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMovieDetailPanel.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void HideLoadingView() {
        if(isReviewLoaded && isTrailerLoaded) {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mMovieDetailPanel.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void bindAdapter() {

        //Review Adapter binding
        LinearLayoutManager review_layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(review_layoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        //Trailer Adapter binding
        LinearLayoutManager trailer_layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(trailer_layoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(MovieDetailActivity.this, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MovieDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onURLClick(MovieTrailer trailer) {
        String url = trailer.getYoutubeURL();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public class FeatchMovieReviewList extends AsyncTask<Void, Void, Void> {

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
                        String.valueOf(selectedObject.getMovie_id()));
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsUrl);
                List<MovieReview> list = OpenMovieJsonUtils.getReviewListFromJsonString(jsonMovieResponse);
                //handle no review case
                if(list.size() == 0)
                {
                    MovieReview emptyReview = new MovieReview();
                    emptyReview.setContent(getString(R.string.no_movie_available_txt));
                    list.add(emptyReview);
                }
                selectedObject.setReviewList(list);
                mReviewAdapter.setReviewObjs(selectedObject.getReviewList());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mReviewAdapter.notifyDataSetChanged();
            isReviewLoaded = true;
            HideLoadingView();
            mReviewRecyclerView.invalidate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class FeatchMovieTrailerDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mRecyclerView.setVisibility(View.INVISIBLE);
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String jsonMovieResponse = "";
            try {
                //fetch movie reviews
                URL movieReviewsUrl = NetworkUtils.buildUrlForMovieTrailerKey(getString(R.string.API_KEY),
                        String.valueOf(selectedObject.getMovie_id()));
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsUrl);
                selectedObject.setTrailerLists(OpenMovieJsonUtils.getTrailerListFromJsonString(jsonMovieResponse));
                mTrailerAdapter.setTrailerObjs(selectedObject.getTrailerLists());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mTrailerAdapter.notifyDataSetChanged();
            isTrailerLoaded = true;
            HideLoadingView();
            mTrailerRecyclerView.invalidate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
