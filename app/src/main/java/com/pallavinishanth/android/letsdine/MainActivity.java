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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            location = savedInstanceState.getParcelable(KEY_LOCATION);

        }

        setContentView(R.layout.activity_main);

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
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
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

    }

    @Override
    public void onError(Status status) {

        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Location selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
            outState.putParcelable(KEY_LOCATION, location);
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
}
