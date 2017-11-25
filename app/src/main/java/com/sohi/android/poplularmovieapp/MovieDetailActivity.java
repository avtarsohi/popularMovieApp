package com.sohi.android.poplularmovieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.sohi.android.poplularmovieapp.adapter.ReviewAdapter;
import com.sohi.android.poplularmovieapp.adapter.TrailerAdapter;
import com.sohi.android.poplularmovieapp.data.FavMovieContract;
import com.sohi.android.poplularmovieapp.data.FavMovieDbHelper;
import com.sohi.android.poplularmovieapp.model.MovieObj;
import com.sohi.android.poplularmovieapp.model.MovieTrailer;
import com.sohi.android.poplularmovieapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    @BindView(R.id.original_Title_Text)
    TextView mOrignalTitle;
    @BindView(R.id.plot_synopsis_Text)
    TextView mPlotSynopsis;
    @BindView(R.id.user_Rating_Text)
    TextView mUserRating;
    @BindView(R.id.release_date_Text)
    TextView mReleaseDate;
    @BindView(R.id.poster_Image)
    ImageView mMoviePosterImage;
    @BindView(R.id.recyclerview_reviews)
    RecyclerView mReviewRecyclerView;
    @BindView(R.id.trailerview_reviews)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.movie_detail_panel)
    ScrollView mMovieDetailPanel;
    @BindView(R.id.addToFavBtn)
    AppCompatButton mFavMovieClickButton;
    @BindView(R.id.favorite_button)
    ImageButton starIcon;


    private MovieObj selectedObject;
    private GoogleApiClient client;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private boolean isReviewLoaded;
    private boolean isTrailerLoaded;
    private SQLiteDatabase mDb;
    private ShareActionProvider mShareActionProvider;
    //First trailer for share
    private MovieTrailer mTrailer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedMovieObject")) {
            selectedObject = (MovieObj) intent.getExtras().getSerializable("selectedMovieObject");
            setUpDB();
            displayMovieObject();
            //scroll to top
            mMovieDetailPanel.scrollTo(0,0);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setUpDB() {
        FavMovieDbHelper dbHelper = new FavMovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void displayMovieObject() {
        try {
            if (selectedObject != null) {
                bindAdapter();

                mOrignalTitle.setText(selectedObject.getOrignal_title());
                mUserRating.setText(String.valueOf(selectedObject.getRating()));
                mPlotSynopsis.setText(selectedObject.getOverview());

                setReleaseDate();
                setPosterImage();

                if (isFavMovie()) {
                    removeFavButton();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPosterImage() {
        URL url = NetworkUtils.buildUrlForMovieDetailsPosterLarge(selectedObject.getPoster_url());

        Picasso.with(this).load(url.toString()).into(mMoviePosterImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                mMovieDetailPanel.scrollTo(0,0);
            }

            @Override
            public void onError() {

            }
        });
        mMoviePosterImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mMoviePosterImage.setAdjustViewBounds(true);
    }

    private void setReleaseDate() {
        //Convert date in a more user readable form
        SimpleDateFormat formater = new SimpleDateFormat("M/d/yyyy");
        String datestring = formater.format(selectedObject.getRelease_date());
        mReleaseDate.setText(datestring);
    }

    private boolean isFavMovie() {
        Cursor cursor = getAllFavMovieId();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_FAVMOVIEID));
                if (id.equals(String.valueOf(selectedObject.getMovie_id())))
                    return true;
            }
        }
        return false;
    }

    private Cursor getAllFavMovieId() {
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

    private void bindAdapter() {

        //Review Adapter binding
        LinearLayoutManager review_layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(review_layoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewAdapter.setReviewObjs(selectedObject.getReviewList());

        //Trailer Adapter binding
        LinearLayoutManager trailer_layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(trailer_layoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(MovieDetailActivity.this, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerAdapter.setTrailerObjs(selectedObject.getTrailerLists());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_trailer:
                if(selectedObject.getTrailerLists() != null &&
                        selectedObject.getTrailerLists().size() > 0) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, selectedObject.getOrignal_title() + " " +
                            selectedObject.getTrailerLists().get(0).getYoutubeURL());
                    startActivity(shareIntent);
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }


    public void onFavClick(View view) {
        if (selectedObject != null) {
            selectedObject.setAddedToFavMovieList(true);
            removeFavButton();
            addToFavMovieDB();
            Toast.makeText(getApplicationContext(), R.string.movie_added_to_DB_message, Toast.LENGTH_LONG).show();
        }
    }

    private void addToFavMovieDB() {
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(FavMovieContract.FavMovieEntry.COLUMN_FAVMOVIEOBJ,
                gson.toJson(selectedObject).getBytes());
        values.put(FavMovieContract.FavMovieEntry.COLUMN_FAVMOVIEID,
                selectedObject.getMovie_id());

        mDb.insert(FavMovieContract.FavMovieEntry.TABLE_NAME, null, values);
    }

    private void removeFavButton() {
        ViewGroup layout = (ViewGroup) mFavMovieClickButton.getParent();
        if (null != layout) {
            layout.removeView(mFavMovieClickButton);
            starIcon.setVisibility(View.VISIBLE);
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
