package com.sohi.android.poplularmovieapp.model;

import java.io.Serializable;

/**
 * Created by siav on 11/18/17.
 */

public class MovieReview implements Serializable {
    public String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String Content;
}