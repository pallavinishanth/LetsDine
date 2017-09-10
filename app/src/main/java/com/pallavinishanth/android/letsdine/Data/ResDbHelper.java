package com.pallavinishanth.android.letsdine.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PallaviNishanth on 9/8/17.
 */

public class ResDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "res.db";

    public ResDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVRES_TABLE = " CREATE TABLE IF NOT EXISTS " +
                ResContract.ResEntry.TABLE_NAME +
                "(" +
                ResContract.ResEntry.COLUMN_PLACE_ID + " TEXT NOT NULL," +
                ResContract.ResEntry.COLUMN_RES_NAME + " TEXT NOT NULL PRIMARY KEY," +
                ResContract.ResEntry.COLUMN_RES_VICINITY + " TEXT NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVRES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResContract.ResEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
