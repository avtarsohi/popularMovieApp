package com.sohi.android.poplularmovieapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieObj selectedObject;

    @BindView(R.id.original_Title_Text) TextView mOrignalTitle;
    @BindView(R.id.plot_synopsis_Text) TextView mPlotSynopsis;
    @BindView(R.id.user_Rating_Text) TextView mUserRating;
    @BindView(R.id.release_date_Text) TextView mReleaseDate;
    @BindView(R.id.poster_Image) ImageView  mMoviePosterImage;

    private GoogleApiClient client;


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
            ex.printStackTrace();
        }


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
