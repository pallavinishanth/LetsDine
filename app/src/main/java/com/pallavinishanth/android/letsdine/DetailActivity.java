package com.pallavinishanth.android.letsdine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.pallavinishanth.android.letsdine.Network.Results;

/**
 * Created by PallaviNishanth on 7/23/17.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "res_detail";
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    TextView hours_view;
    ImageView res_Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "OnCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_detail_view);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        hours_view = (TextView) findViewById(R.id.hours);

        Intent i = getIntent();

        if(i !=null && i.hasExtra(EXTRA_NAME)){

            final Results res = i.getParcelableExtra(EXTRA_NAME);
            //hours_view.setText(res.getOpeningHours().getOpenNow().toString());
            res_Photo = (ImageView) findViewById(R.id.res_backdrop);


            Log.v(LOG_TAG, "Restaurant details" + res.getName());
        }

    }
}
