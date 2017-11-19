package com.sohi.android.poplularmovieapp.data;

import android.provider.BaseColumns;

/**
 * Created by siav on 11/19/17.
 */

public class FavMovieContract {
    public static final class FavMovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favMovieList";
        public static final String COLUMN_FAVMOVIEOBJ = "favmovieobj";
        public static final String COLUMN_TIMESTAMP = "timestamp";
//        public static final String COLUMN_PARTY_SIZE = "partySize";
//        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
