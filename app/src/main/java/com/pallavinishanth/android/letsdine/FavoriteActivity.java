package com.pallavinishanth.android.letsdine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pallavinishanth.android.letsdine.Data.ResContract;

/**
 * Created by PallaviNishanth on 9/8/17.
 */

public class FavoriteActivity extends AppCompatActivity {

    private final String LOG_TAG = FavoriteActivity.class.getSimpleName();

    private FavoriteAdapter favAdapter;
    private RecyclerView favRecyclerView;
    private RecyclerView.LayoutManager favLayoutManager;

    String[] res_names;
    String[] res_address;
    static String[] PlaceID;
    TextView fav_textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorite_layout);

        fav_textview = (TextView) findViewById(R.id.fav_heading);

        if(res_names == null && res_address == null)
            fav_textview.setText("No Favorites to show");

        favRecyclerView = (RecyclerView) findViewById(R.id.fav_recycler_view);
        favRecyclerView.setHasFixedSize(true);
        favLayoutManager = new LinearLayoutManager(FavoriteActivity.this,
                LinearLayoutManager.VERTICAL, false);
        favRecyclerView.setLayoutManager(favLayoutManager);

        favAdapter = new FavoriteAdapter(getBaseContext(), res_names, res_address);
        favRecyclerView.setAdapter(favAdapter);

        favAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View itemView, int position) {

//                Toast.makeText(FavoriteActivity.this, "Fav Restaurant clicked", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(FavoriteActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.PLACE_ID, PlaceID[position]);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFavRestaurants();
    }

    public void getFavRestaurants() {

        Log.v(LOG_TAG, "Get favorite Restaurants");

        Cursor favrescursor;

        favrescursor = getContentResolver().
                query(ResContract.ResEntry.CONTENT_URI, null, null, null, null, null);

        if(favrescursor.getCount()!=0) {

            Log.v(LOG_TAG, "Fav movies DB has items" + favrescursor.getCount());

            favrescursor.moveToFirst();
            int i = 0;

            res_names = new String[favrescursor.getCount()];
            res_address = new String[favrescursor.getCount()];
            PlaceID = new String[favrescursor.getCount()];

            do{

                res_names[i] = favrescursor.
                        getString(favrescursor.getColumnIndex(ResContract.ResEntry.COLUMN_RES_NAME));
                res_address[i] = favrescursor.
                        getString(favrescursor.getColumnIndex(ResContract.ResEntry.COLUMN_RES_VICINITY));
                PlaceID[i] = favrescursor.
                        getString(favrescursor.getColumnIndex(ResContract.ResEntry.COLUMN_PLACE_ID));
                i++;

            }while(favrescursor.moveToNext());
            favrescursor.close();

            favAdapter = new FavoriteAdapter(getBaseContext(), res_names, res_address);
            favRecyclerView.setAdapter(favAdapter);
        } else{

            Toast.makeText(FavoriteActivity.this, "No Restaurants in Favorite List", Toast.LENGTH_SHORT).show();
            favrescursor.close();
        }
    }
}
