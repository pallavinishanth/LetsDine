package com.pallavinishanth.android.letsdine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.pallavinishanth.android.letsdine.Network.Photos;
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

import static com.pallavinishanth.android.letsdine.R.layout.activity_main;

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
    public static double latitude;
    public static double longitude;
    public LocationManager locationManager;
    Criteria criteria;

    public double default_latitude = 42.359799;
    public double default_longitude = -71.054460;

    String state;
    static boolean current_loc = true;

    static String loc;

    static int i = 0;

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
    private CoordinatorLayout mCLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d(LOG_TAG, "onCreate");
        setContentView(activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        progress_bar = (ProgressBar) findViewById(R.id.progressBar);
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        LocTextView = (TextView) findViewById(R.id.location);

        getLocation();

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

        // Retrieve location from saved instance state.
        if (savedInstanceState != null) {
            LocTextView.setText(savedInstanceState.getString(KEY_LOCATION));
            resJSONdata = savedInstanceState.getParcelableArrayList("RES_LIST");
            progressBarIsShowing = savedInstanceState.getBoolean("progressBarIsShowing");
//            Log.d(LOG_TAG, "After rotating" + LocTextView.getText().toString());
//            Log.d(LOG_TAG, "After rotating" + resJSONdata.get(1).getName().toString());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
//                Log.i(LOG_TAG, "Place: " + place.getName());
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.i(LOG_TAG, status.getStatusMessage());
                this.onError(status);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
//                Log.i(LOG_TAG, "User Canceled the operation ");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

//        Log.d(LOG_TAG, "onRequestPermissionsResult");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        getLatLong();
    }

    public void getLocation() {

//        Log.d(LOG_TAG, "getLocation");

        //Location Manager is used to figure out which location provider needs to be used.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Best location provider is decided by the criteria
        criteria = new Criteria();
        //location manager will take the best location from the criteria
        locationManager.getBestProvider(criteria, true);

        //once you know the name of the LocationProvider, you can call getLastKnownPosition()
        // to find out where you were recently.

        // Checking the permissions
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getLatLong();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    public void getLatLong() {

        try {
            if (mLocationPermissionGranted) {

//                Log.d(LOG_TAG, "mLocationPermissionGranted true");

                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

                if (location != null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

//                    Log.d(LOG_TAG, "latitude : " + latitude + "longitude : " + longitude);
                } else {

                    latitude = default_latitude;
                    longitude = default_longitude;
                }

                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;

                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        cityname = addresses.get(0).getLocality().toString();
                        //state = addresses.get(0).getAdminArea().toString();
//
//                        Log.d(LOG_TAG, "After back button pressed");
                        LocTextView.setText(cityname);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }

                if (current_loc == true && resJSONdata.isEmpty()) {

//                    Log.d(LOG_TAG, "calling retrofit...");
//                    Log.d(LOG_TAG, "latitude : " + latitude + "longitude : " + longitude);
                    progress_bar.setVisibility(ProgressBar.VISIBLE);
                    progressBarIsShowing = true;
                    retrofit_response(latitude + "," + longitude);
                }

                resRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                resRecyclerView.setHasFixedSize(true);

                resLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                resRecyclerView.setLayoutManager(resLayoutManager);

                resDataAdapter = new ResDataAdapter(getBaseContext(), resJSONdata);
                resRecyclerView.setAdapter(resDataAdapter);

                resDataAdapter.setOnItemClickListener(new ResDataAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View itemView, int position) {

//                Toast.makeText(MainActivity.this, "Res card clicked", Toast.LENGTH_SHORT).show();

                        Results res_results_card = resJSONdata.get(position);

                        ArrayList<Photos> res_photos = resJSONdata.get(position).getPhotos();

                        Intent i = new Intent(MainActivity.this, DetailActivity.class);
                        i.putExtra(DetailActivity.PLACE_ID, res_results_card.getPlaceId());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Bundle bundle = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(MainActivity.this)
                                    .toBundle();

                            startActivity(i, bundle);
                        } else {
                            startActivity(i);
                        }

                    }
                });

            } else {

                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;

                try {
                    addresses = gcd.getFromLocation(default_latitude, default_longitude, 1);
                    if (addresses.size() > 0) {
                        cityname = addresses.get(0).getLocality().toString();
                        //state = addresses.get(0).getAdminArea().toString();

//                        Log.d(LOG_TAG, "After back button pressed");
                        LocTextView.setText(cityname);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }

                if (current_loc == true && resJSONdata.isEmpty()) {

//                    Log.d(LOG_TAG, "calling retrofit...");
//                    Log.d(LOG_TAG, "latitude : " + default_longitude + "longitude : " + default_longitude);
                    progress_bar.setVisibility(ProgressBar.VISIBLE);
                    progressBarIsShowing = true;
                    retrofit_response(default_latitude + "," + default_longitude);
                }

                resRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                resRecyclerView.setHasFixedSize(true);

                resLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                resRecyclerView.setLayoutManager(resLayoutManager);

                resDataAdapter = new ResDataAdapter(getBaseContext(), resJSONdata);
                resRecyclerView.setAdapter(resDataAdapter);

                resDataAdapter.setOnItemClickListener(new ResDataAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View itemView, int position) {

//                Toast.makeText(MainActivity.this, "Res card clicked", Toast.LENGTH_SHORT).show();

                        Results res_results_card = resJSONdata.get(position);

                        ArrayList<Photos> res_photos = resJSONdata.get(position).getPhotos();

                        Intent i = new Intent(MainActivity.this, DetailActivity.class);
                        i.putExtra(DetailActivity.PLACE_ID, res_results_card.getPlaceId());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Bundle bundle = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(MainActivity.this)
                                    .toBundle();

                            startActivity(i, bundle);
                        } else {
                            startActivity(i);
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    @Override
    public void onPlaceSelected(Place place) {

//        Log.i(LOG_TAG, "Place Selected: " + place.getName());

        loc = place.getName().toString();

        LocTextView.setText(place.getName());
        LatLng Sel_location = place.getLatLng();

        current_loc = false;
        progress_bar.setVisibility(ProgressBar.VISIBLE);
        progressBarIsShowing = true;
        retrofit_response(Sel_location.latitude + "," + Sel_location.longitude);
    }

    @Override
    public void onError(Status status) {

        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, R.string.loc_sel_failed + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();

    }

    /**
     * Saves the location when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

//        Log.d(LOG_TAG, "Before rotating" + LocTextView.getText().toString());

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
//
//        Log.d(LOG_TAG, "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.d(LOG_TAG, "on resume");

        if (current_loc == false)
            LocTextView.setText(loc);
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
//                Toast.makeText(getApplicationContext(), "On select of favorite", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void retrofit_response(String location) {

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

//                Log.v(LOG_TAG, "Restaurant search Response is " + response.body().getStatus());

                resJSONdata = response.body().getResults();
                res_data_count = resJSONdata.size();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("DataSize", res_data_count);

                for (int i = 0; i < res_data_count; i++) {
                    editor.putString("PlaceID" + "_" + i, resJSONdata.get(i).getPlaceId());

                }

                for (int i = 0; i < res_data_count; i++) {

                    if (resJSONdata.get(i).getName() != null) {
                        editor.putString("RESName" + "_" + i, resJSONdata.get(i).getName());
                    } else {
                        editor.putString("RESName" + "_" + i, "name not found");
                    }

                }
                for (int i = 0; i < res_data_count; i++) {

                    if (resJSONdata.get(i).getVicinity() != null) {
                        editor.putString("RESVicinity" + "_" + i, resJSONdata.get(i).getVicinity());
                    } else {
                        editor.putString("RESVicinity" + "_" + i, "Address not found");
                    }
                }
                for (int i = 0; i < res_data_count; i++) {

                    if (resJSONdata.get(i).getOpeningHours() != null) {
                        editor.putBoolean("RESHours" + "_" + i, resJSONdata.get(i).getOpeningHours().getOpenNow());
                    }

                }

                editor.apply();

                for (Results result : resJSONdata) {

//                    Log.v(LOG_TAG, "Nearby Restaurant Name is " + result.getName());

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