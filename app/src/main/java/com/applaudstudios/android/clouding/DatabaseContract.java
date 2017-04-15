package com.applaudstudios.android.clouding;

import android.provider.BaseColumns;

/**
 * Created by wjplaud83 on 4/7/17.
 */

public final class DatabaseContract {
    // to prevent someone from accidentally entering into the contract class,
    // and give it to an empty constructor.

    public DatabaseContract() {
    }

    // Inner class that defines the table of contents
    public static abstract class Clouds implements BaseColumns {
        public static final String TABLE_NAME = "clouds";
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String COLUMN_NAME_CLOUD_NAME = "name";
        public static final String COLUMN_NAME_CLOUD_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CLOUD_RATING = "rating";
        public static final String COLUMN_NAME_CLOUD_IMAGEURI = "imageuri";



    }
}
