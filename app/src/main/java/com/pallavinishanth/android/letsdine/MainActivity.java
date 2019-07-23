package com.pallavinishanth.android.letsdine;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener, PlaceSelectionListener {

    ProgressBar _progressBar;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    boolean progressBarIsShowing;
    private CoordinatorLayout mCLayout;
    LocationRequest mLocationRequest;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private boolean mLocationPermissionGranted;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public static double latitude;
    public static double longitude;
    TextView LocTextView;
    String cityName;
    ImageView currLoc;
    private Toolbar mToolbar;
    private RecyclerView resRecyclerView;
    private RecyclerView.LayoutManager resLayoutManager;
    private ResDataAdapter resDataAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    static boolean current_loc = false;
    static boolean loc_changed = false;

    static String loc;

    final String RES_DATA_API = "https://maps.googleapis.com/maps/";
    private int RADIUS = 10000;
    static int res_data_count;
    private static ArrayList<Results> resJSONdata = new ArrayList<>();

    // Keys for storing activity state.
    private static final String KEY_LOCATION = "location";

    public double default_latitude = 42.359799;
    public double default_longitude = -71.054460;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);
        //actionBar.setDisplayUseLogoEnabled(true);

        LocTextView = findViewById(R.id.location);
       // currLoc = findViewById(R.id.currentloc_icon);

        _progressBar = findViewById(R.id.progressBar);
        //mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mCLayout = findViewById(R.id.coordinator_layout);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (savedInstanceState != null) {

//            Log.v(LOG_TAG, "After rotation... savedInstanceState != null");

            LocTextView.setText(savedInstanceState.getString(KEY_LOCATION));
            resJSONdata = savedInstanceState.getParcelableArrayList("RES_LIST");
            progressBarIsShowing = savedInstanceState.getBoolean("progressBarIsShowing");
//            Log.d(LOG_TAG, "After rotating" + LocTextView.getText().toString());
//            Log.d(LOG_TAG, "After rotating" + resJSONdata.get(1).getName().toString());

            resRecyclerView = findViewById(R.id.recycler_view);
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
        }else{
//            Log.v(LOG_TAG, "savedInstanceState ===== null");

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            } else
                Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();

        }

//        currLoc.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
////                Log.v(LOG_TAG, "current location clicked..........");
//                Toast.makeText(MainActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
//                current_loc = true;
//
//                if (mGoogleApiClient != null)
//                    mGoogleApiClient.connect();
//
////                getLocation();
//            }
//        });

        LocTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loc_changed = true;
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


    }

    /*Ending the updates for the location service*/
    @Override
    protected void onStop() {
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {

//        Log.v(LOG_TAG, "settingRequest..........");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
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
                break;
        }
    }


    @Override
    public void onPlaceSelected(Place place) {

            loc_changed = true;
//            Log.i(LOG_TAG, "Place Selected: " + place.getName());

            loc = place.getName().toString();

            LocTextView.setText(place.getName());
            LatLng Sel_location = place.getLatLng();
            _progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBarIsShowing = true;
            retrofit_response(Sel_location.latitude + "," + Sel_location.longitude);

    }

    @Override
    public void onError(Status status) {

//        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, R.string.loc_sel_failed + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    Log.v(LOG_TAG, "Permissionsgranted");
                    mLocationPermissionGranted = true;
                    getLatLong();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Log.v(LOG_TAG, "Permissionsdenied");
                    mLocationPermissionGranted = false;
                    getLatLong();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getLocation() {

//        Log.v(LOG_TAG, "getLocation.......");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            Log.v(LOG_TAG, "requestPermissions");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {

            mLocationPermissionGranted = true;

//            Log.v(LOG_TAG, "defaultPermissionsgranted");
            getLatLong();
        }
    }


    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {

//        Log.v(LOG_TAG, "onLocationChanged###############" + "current_loc" + current_loc + "loc_changed"+ loc_changed);
        mLastLocation = location;
        _progressBar.setVisibility(View.INVISIBLE);
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();

        if(current_loc==true || resJSONdata.isEmpty()){

//            Log.v(LOG_TAG, "latitude...." + latitude + "longitude...." + longitude);

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;

            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality().toString();

                    LocTextView.setText(cityName);
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
            _progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBarIsShowing = true;
            retrofit_response(latitude + "," + longitude);

            resRecyclerView = findViewById(R.id.recycler_view);
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
        current_loc = false;
    }

    public void getLatLong() {

//        Log.v(LOG_TAG, "getLatLong..........");

        try{
            if (mLocationPermissionGranted) {
                /*Getting the location after acquiring location service*/
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

                if (mLastLocation != null) {
                    _progressBar.setVisibility(View.INVISIBLE);
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                    if (current_loc==true || resJSONdata.isEmpty()) {
//                        Log.v(LOG_TAG, "latitude" + latitude + "longitude" + longitude);

                        Geocoder gcd = new Geocoder(this, Locale.getDefault());
                        List<Address> addresses;

                        try {
                            addresses = gcd.getFromLocation(latitude, longitude, 1);
                            if (addresses.size() > 0) {
                                cityName = addresses.get(0).getLocality().toString();
                                LocTextView.setText(cityName);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                        _progressBar.setVisibility(ProgressBar.VISIBLE);
                        progressBarIsShowing = true;
                        retrofit_response(latitude + "," + longitude);

                        resRecyclerView = findViewById(R.id.recycler_view);
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
                } else {
                /*if there is no last known location. Which means the device has no data for the location currently.
                * So we will get the current location.
                * For this we'll implement Location Listener and override onLocationChanged*/
                    Log.i("Current Location", "No data for location found");

                    if (!mGoogleApiClient.isConnected())
                        mGoogleApiClient.connect();

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);
                }
            }else{

//                Log.v(LOG_TAG, "if permissions denied by user...");

                if (current_loc==true || resJSONdata.isEmpty()) {
                    Geocoder gcd = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses;

                    try {
                        addresses = gcd.getFromLocation(default_latitude, default_longitude, 1);
                        if (addresses.size() > 0) {
                            cityName = addresses.get(0).getLocality().toString();
                            //state = addresses.get(0).getAdminArea().toString();

//                        Log.d(LOG_TAG, "After back button pressed");
                            LocTextView.setText(cityName);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                    _progressBar.setVisibility(ProgressBar.VISIBLE);
                    progressBarIsShowing = true;
                    retrofit_response(default_latitude + "," + default_longitude);

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
            }

        }catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
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

                _progressBar.setVisibility(ProgressBar.INVISIBLE);

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