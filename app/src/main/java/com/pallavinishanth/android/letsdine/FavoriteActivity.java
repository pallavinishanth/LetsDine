package com.pallavinishanth.android.letsdine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.pallavinishanth.android.letsdine.Data.ResContract;

/**
 * Created by PallaviNishanth on 9/8/17.
 */

public class FavoriteActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = FavoriteActivity.class.getSimpleName();

    private FavoriteAdapter favAdapter;
    private RecyclerView favRecyclerView;
    private RecyclerView.LayoutManager favLayoutManager;

    String[] res_names;
    String[] res_address;
    static String[] PlaceID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorite_layout);

        getSupportLoaderManager().initLoader(0, null, this);

        favRecyclerView = (RecyclerView) findViewById(R.id.fav_recycler_view);
        favRecyclerView.setHasFixedSize(true);
        favLayoutManager = new LinearLayoutManager(FavoriteActivity.this,
                LinearLayoutManager.VERTICAL, false);
        favRecyclerView.setLayoutManager(favLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ResContract.ResEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

//        Log.d(LOG_TAG, "onLoadFinished....");

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            int i = 0;

            res_names = new String[cursor.getCount()];
            res_address = new String[cursor.getCount()];
            PlaceID = new String[cursor.getCount()];

            do {

                res_names[i] = cursor.
                        getString(cursor.getColumnIndex(ResContract.ResEntry.COLUMN_RES_NAME));
                res_address[i] = cursor.
                        getString(cursor.getColumnIndex(ResContract.ResEntry.COLUMN_RES_VICINITY));
                PlaceID[i] = cursor.
                        getString(cursor.getColumnIndex(ResContract.ResEntry.COLUMN_PLACE_ID));
                i++;

            } while (cursor.moveToNext());
            cursor.close();

            favAdapter = new FavoriteAdapter(getBaseContext(), res_names, res_address);
            favRecyclerView.setAdapter(favAdapter);

            favAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View itemView, int position) {

//                Toast.makeText(FavoriteActivity.this, "Fav Restaurant clicked", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(FavoriteActivity.this, DetailActivity.class);
                    i.putExtra(DetailActivity.PLACE_ID, PlaceID[position]);
                    startActivity(i);
                }
            });
        } else {

            Toast.makeText(FavoriteActivity.this, R.string.no_fav, Toast.LENGTH_SHORT).show();
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        favRecyclerView.setAdapter(null);
    }

}
