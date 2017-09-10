package com.pallavinishanth.android.letsdine.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by PallaviNishanth on 9/8/17.
 */

public class ResProvider extends ContentProvider {

    static final int RESTAURANTS = 100;

    static final int RES_WITH_NAME = 101;

    private static final SQLiteQueryBuilder sMovieQBuilder;

    static{

        sMovieQBuilder = new SQLiteQueryBuilder();
        sMovieQBuilder.setTables(ResContract.ResEntry.TABLE_NAME);
    }

    // The URI Matcher used by this content Provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private ResDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ResContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ResContract.PATH_RES, RESTAURANTS);
        matcher.addURI(authority, ResContract.PATH_RES + "/#", RES_WITH_NAME);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case RESTAURANTS:
                return ResContract.ResEntry.CONTENT_TYPE;

            case RES_WITH_NAME:
                return ResContract.ResEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case RESTAURANTS:

                //db.beginTransaction();
                long _id = db.insert(ResContract.ResEntry.TABLE_NAME, null, contentValues);

                if(_id >0){

                    returnUri = ResContract.ResEntry.buildFavResUri(String.valueOf(_id));
                }else

                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        //db.close();
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ResDbHelper(getContext());
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch(match){

            case RES_WITH_NAME:

                String name = ResContract.ResEntry.getFavResName(uri);
                selection = ResContract.ResEntry.COLUMN_RES_NAME + "=" + name;
                rowsDeleted = db.delete(ResContract.ResEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case RESTAURANTS:

                retCursor = sMovieQBuilder.query(mOpenHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);

                break;
            case RES_WITH_NAME:

                retCursor = sMovieQBuilder.query(mOpenHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }
}
