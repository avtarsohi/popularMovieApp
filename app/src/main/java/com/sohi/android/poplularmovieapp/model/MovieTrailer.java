package com.sohi.android.poplularmovieapp.model;

import java.io.Serializable;

/**
 * Created by siav on 11/18/17.
 */

public class MovieTrailer implements Serializable {
    private int counter;
    private String Id;
    private String Key;
    private String DisplayText;
    private String YoutubeURL;

    public MovieTrailer(int counter) {
        this.counter = counter + 1;
        this.YoutubeURL = "https://www.youtube.com/watch?v=";
        this.DisplayText = "Trailer ";
    }

    public String getDisplayText() {
        return DisplayText + counter;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getYoutubeURL() {
        return this.YoutubeURL + this.getKey();
    }
}