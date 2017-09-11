package com.pallavinishanth.android.letsdine.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PallaviNishanth on 9/8/17.
 */

public final class ResContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.pallavinishanth.android.letsdine";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RES = "favRes";

    public static final class ResEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RES;

        // Table name
        public static final String TABLE_NAME = "favRes";

        public static final String COLUMN_PLACE_ID = "place_id";

        public static final String COLUMN_RES_NAME = "res_name";

        public static final String COLUMN_RES_VICINITY = "res_vicinity";

        public static Uri buildFavResUri(String placeid) {
            return CONTENT_URI.buildUpon().appendEncodedPath(placeid).build();
        }

        public static String getFavResName(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
