package com.pallavinishanth.android.letsdine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.pallavinishanth.android.letsdine.Network.ResRetrofitAPI;
import com.pallavinishanth.android.letsdine.Network.ResSearchJSON;
import com.pallavinishanth.android.letsdine.Network.Results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements PlaceSelectionListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // Keys for storing activity state.
    private static final String KEY_LOCATION = "location";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;

    TextView LocTextView;
    String cityname;
    private Location location;
    String state;
    static boolean current_loc = true;

    final String RES_DATA_API = "https://maps.googleapis.com/maps/";
    private int RADIUS = 10000;
    private static ArrayList<Results> resJSONdata = new ArrayList<>();

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView resRecyclerView;
    private RecyclerView.LayoutManager resLayoutManager;
    private ResDataAdapter resDataAdapter;
    static int res_data_count;
    ProgressBar progress_bar;
    boolean progressBarIsShowing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        progress_bar = (ProgressBar)findViewById(R.id.progressBar);

        LocTextView = (TextView)findViewById(R.id.location);

        //Location Manager is used to figure out which location provider needs to be used.
        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        //Best location provider is decided by the criteria
        Criteria criteria=new Criteria();
        //location manager will take the best location from the criteria
        locationManager.getBestProvider(criteria, true);

        //once you know the name of the LocationProvider, you can call getLastKnownPosition()
        // to find out where you were recently.

        // Checking the permissions
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
        android.Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if(mLocationPermissionGranted) {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        }

        Geocoder gcd=new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses=gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(addresses.size()>0)
                {
                    cityname = addresses.get(0).getLocality().toString();
                    //state = addresses.get(0).getAdminArea().toString();
                    LocTextView.setText(cityname);
                }

            } catch (IOException e) {
                e.printStackTrace();

            }

        LocTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    AutocompleteFilter locationFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();

                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN).setFilter(locationFilter)
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        if(current_loc==true) {
            progress_bar.setVisibility(ProgressBar.VISIBLE);
            progressBarIsShowing = true;
            retrofit_response(location.getLatitude() + "," + location.getLongitude());
        }

        // Retrieve location from saved instance state.
        if (savedInstanceState != null) {
            LocTextView.setText(savedInstanceState.getString(KEY_LOCATION));
            resJSONdata = savedInstanceState.getParcelableArrayList("RES_LIST");
            progressBarIsShowing = savedInstanceState.getBoolean("progressBarIsShowing");
            Log.d(LOG_TAG, "After rotating" + LocTextView.getText().toString());
            Log.d(LOG_TAG, "After rotating" + resJSONdata.get(1).getName().toString());
        }

        resRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        resRecyclerView.setHasFixedSize(true);

        resLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        resRecyclerView.setLayoutManager(resLayoutManager);

        resDataAdapter = new ResDataAdapter(getBaseContext(), resJSONdata);
        resRecyclerView.setAdapter(resDataAdapter);

        resDataAdapter.setOnItemClickListener(new ResDataAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View itemView, int position) {

                Toast.makeText(MainActivity.this, "Res card clicked", Toast.LENGTH_SHORT).show();

                Results res_results_card = resJSONdata.get(position);

                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_NAME, res_results_card);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(LOG_TAG, "Place: " + place.getName());
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(LOG_TAG, status.getStatusMessage());
                this.onError(status);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(LOG_TAG, "User Canceled the operation " );
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {

        Log.i(LOG_TAG, "Place Selected: " + place.getName());

        LocTextView.setText(place.getName());
        LatLng Sel_location = place.getLatLng();

        current_loc = false;
        progress_bar.setVisibility(ProgressBar.VISIBLE);
        progressBarIsShowing = true;
        retrofit_response(Sel_location.latitude +"," +Sel_location.longitude);
    }

    @Override
    public void onError(Status status) {

        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Location selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();

    }

    /**
     * Saves the location when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d(LOG_TAG, "Before rotating" + LocTextView.getText().toString());

        outState.putString(KEY_LOCATION, LocTextView.getText().toString());
        outState.putParcelableArrayList("RES_LIST", resJSONdata);
        if (progressBarIsShowing) {
            outState.putBoolean("progressBarIsShowing", progressBarIsShowing);
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(getApplicationContext(), "On select of favorite", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void retrofit_response(String location){

        Retrofit resRetrofit = new Retrofit.Builder()
                .baseUrl(RES_DATA_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ResRetrofitAPI retrofitAPI = resRetrofit.create(ResRetrofitAPI.class);

        Call<ResSearchJSON> call = retrofitAPI.getNearbyRestaurants(location, RADIUS,
                BuildConfig.GOOGLE_PLACES_API_KEY);

        call.enqueue(new Callback<ResSearchJSON>() {
            @Override
            public void onResponse(Response<ResSearchJSON> response, Retrofit retrofit) {

                Log.v(LOG_TAG, "Restaurant search Response is " + response.body().getStatus());

                resJSONdata = response.body().getResults();
                res_data_count = resJSONdata.size();

                for(Results result: resJSONdata){
                    Log.v(LOG_TAG, "Nearby Restaurant Name is " + result.getName());
                    Log.v(LOG_TAG, "Nearby Restaurant price level " + result.getPriceLevel());

                }

                progress_bar.setVisibility(ProgressBar.INVISIBLE);

                resDataAdapter = new ResDataAdapter(getBaseContext(), resJSONdata);
                resRecyclerView.setAdapter(resDataAdapter);

            }

            @Override
            public void onFailure(Throwable t) {
                Log.v(LOG_TAG, "On Failure" + t.toString());
            }
        });

    }
}
