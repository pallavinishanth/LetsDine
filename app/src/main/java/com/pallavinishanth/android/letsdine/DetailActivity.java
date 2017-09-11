package com.pallavinishanth.android.letsdine;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pallavinishanth.android.letsdine.Data.ResContract;
import com.pallavinishanth.android.letsdine.Network.DetailPhotos;
import com.pallavinishanth.android.letsdine.Network.DetailResult;
import com.pallavinishanth.android.letsdine.Network.ResDetailJSON;
import com.pallavinishanth.android.letsdine.Network.ResRetrofitAPI;
import com.pallavinishanth.android.letsdine.Network.Reviews;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by PallaviNishanth on 7/23/17.
 */

public class DetailActivity extends AppCompatActivity {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    private static final int GOOGLE_API_CLIENT_ID = 0;

    public static final String EXTRA_NAME = "res_detail";
    public static final String PLACE_ID = "place_id";
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private ResPhotoAdapter resPhotoAdapter;
    private RecyclerView photoRecyclerView;
    private RecyclerView.LayoutManager photoLayoutManager;

    private ResReviewsAdapter resReviewAdapter;
    private RecyclerView reviewRecyclerView;
    private RecyclerView.LayoutManager reviewLayoutManager;

    private ResDetailAdapter resDetailAdapter;
    private RecyclerView detailRecyclerView;

    private static ArrayList<DetailPhotos> photoslist = new ArrayList<DetailPhotos>();
    private static ArrayList<Reviews> reviewslist = new ArrayList<Reviews>();
    private String placeID;
    final String RES_DETAIL_API = "https://maps.googleapis.com/maps/";
    private static DetailResult detail_result = new DetailResult();

    private static String res_name;

    TextView hours_view;
    ImageView res_Photo;
    TextView website_view;
    TextView address;
    ImageView backdrop;
    TextView photoHeading;
    TextView reviewHeading;
    ImageView mapImage;
    TextView phoneview;
    ImageView phoneicon;
    FloatingActionButton fav_fab;

    boolean markedFav, delete_result, insert_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "OnCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_detail_view);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        placeID = getIntent().getStringExtra(PLACE_ID);

        Log.v(LOG_TAG, "Place ID " + placeID);

        website_view = (TextView) findViewById(R.id.website_url);
        hours_view = (TextView) findViewById(R.id.hours);
        address = (TextView) findViewById(R.id.address);
        backdrop = (ImageView) findViewById(R.id.res_backdrop);
        photoHeading = (TextView) findViewById(R.id.photos_heading);
        reviewHeading = (TextView) findViewById(R.id.Reviews_heading);
        mapImage = (ImageView) findViewById(R.id.map_image);
        phoneview = (TextView) findViewById(R.id.phoneNum);
        phoneicon = (ImageView) findViewById(R.id.phoneIcon);
        fav_fab = (FloatingActionButton) findViewById(R.id.fav_fab);


        photoRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        photoRecyclerView.setHasFixedSize(true);
        photoLayoutManager = new LinearLayoutManager(DetailActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(photoLayoutManager);

        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        reviewRecyclerView.setHasFixedSize(true);
        reviewLayoutManager = new LinearLayoutManager(DetailActivity.this,
                LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        if(savedInstanceState !=null){

            markedFav = savedInstanceState.getBoolean("M_marked");

            detail_result = savedInstanceState.getParcelable("DETAIL_RESULT");

            if(detail_result.getWebsite()!=null){

                Log.v(LOG_TAG, "Place website " + detail_result.getWebsite());
                website_view.setText(detail_result.getWebsite());
                website_view.setContentDescription(detail_result.getWebsite());

                website_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(detail_result.getWebsite()));
                        startActivity(browserIntent);
                    }
                });

            }else{
                website_view.setText("URL not found");
                website_view.setContentDescription("URL not found");
            }

            if(detail_result.getWebsite()!=null){

                Log.v(LOG_TAG, "Place website " + detail_result.getWebsite());
                website_view.setText(detail_result.getWebsite());
                website_view.setContentDescription(detail_result.getWebsite());

                website_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(detail_result.getWebsite()));
                        startActivity(browserIntent);
                    }
                });

            }else{
                website_view.setText("URL not found");
                website_view.setContentDescription("URL not found");
            }

            if(detail_result.getPhoneNum()!=null){

                Log.v(LOG_TAG, detail_result.getPhoneNum());
                phoneview.setText(detail_result.getPhoneNum());
                phoneview.setContentDescription(detail_result.getPhoneNum());

                phoneicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+ detail_result.getPhoneNum()));
                        startActivity(callIntent);
                    }
                });
            }else{

                phoneview.setText("Phone Number Not Found");
                phoneview.setContentDescription("Phone Number Not Found");
            }

            if(detail_result.getDetailOpeningHours()!=null){

                for(int i=0; i<7; i++){
                    hours_view.append(detail_result.getDetailOpeningHours().getweekhours()[i]);
                    hours_view.setContentDescription(detail_result.getDetailOpeningHours().getweekhours()[0]
                            + detail_result.getDetailOpeningHours().getweekhours()[1]
                            + detail_result.getDetailOpeningHours().getweekhours()[2]
                            + detail_result.getDetailOpeningHours().getweekhours()[3]
                            + detail_result.getDetailOpeningHours().getweekhours()[4]
                            + detail_result.getDetailOpeningHours().getweekhours()[5]
                            + detail_result.getDetailOpeningHours().getweekhours()[6]);
                    hours_view.append("\n");
                    Log.v(LOG_TAG, "hours " + (detail_result.getDetailOpeningHours().getweekhours()[i]));
                }

            }else{
                hours_view.setText("Hours not found");
                hours_view.setContentDescription("Hours not found");
            }

            if(detail_result.getAddress()!=null){

                Log.v(LOG_TAG, detail_result.getAddress());
                address.setText(detail_result.getAddress());
                address.setContentDescription(detail_result.getAddress());
            }else{
                address.setText("Address not found");
                address.setContentDescription("Address not found");
            }

            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

            if(detail_result.getname() != null){

                collapsingToolbar.setTitle(detail_result.getname() );
                collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

            }else{
                collapsingToolbar.setTitle("Restaurant name not found");
            }


            if(!detail_result.getPhotos().isEmpty()){

                Log.v(LOG_TAG, detail_result.getPhotos().get(0).getPhotoReference());
                Log.v(LOG_TAG, String.format("size = %d", detail_result.getPhotos().size()));
                photoslist = detail_result.getPhotos();

                Glide.with(getBaseContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxheight=380&photoreference="
                        +photoslist.get(0).getPhotoReference()
                        +"&key=" + BuildConfig.GOOGLE_PLACES_API_KEY).centerCrop().into(backdrop);

                resPhotoAdapter = new ResPhotoAdapter(getBaseContext(), photoslist);
                photoRecyclerView.setAdapter(resPhotoAdapter);
            }else{

//                    Glide.with(getBaseContext()).load(detail_result.geticon()).centerCrop().into(backdrop);
                photoHeading.setText("PHOTOS NOT FOUND");
                photoHeading.setContentDescription("PHOTOS NOT FOUND");

            }

            if(!detail_result.getReviews().isEmpty()){
                Log.v(LOG_TAG, detail_result.getReviews().get(0).getAuthor_name());
                Log.v(LOG_TAG, detail_result.getReviews().get(0).getText());
                Log.v(LOG_TAG, String.format("Review Rating = %d", detail_result.getReviews().get(0).getRating()));
                reviewslist = detail_result.getReviews();

                resReviewAdapter = new ResReviewsAdapter(getBaseContext(), reviewslist);
                reviewRecyclerView.setAdapter(resReviewAdapter);

            }else{
                reviewHeading.setText("REVIEWS NOT FOUND");
                reviewHeading.setContentDescription("REVIEWS NOT FOUND");
            }

            mapImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                        Toast.makeText(DetailActivity.this, "Maps clicked",
//                                Toast.LENGTH_SHORT).show();

                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+ detail_result.getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

        }else{

            retrofit_detail_response(placeID);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d(LOG_TAG, "Before rotating" + detail_result.getname());

        outState.putParcelable("DETAIL_RESULT", detail_result);
        outState.putBoolean("M_marked", markedFav);

        super.onSaveInstanceState(outState);

    }

    public Boolean ResMarkedFav(String res_name){

        Log.d(LOG_TAG, "ResMarkedFav "+ res_name);

        Cursor cursor;

        cursor = getContentResolver().query(ResContract.ResEntry.CONTENT_URI,
                new String[] {ResContract.ResEntry.COLUMN_RES_NAME},
                ResContract.ResEntry.COLUMN_RES_NAME + "=" + "'" +res_name + "'",
                null, null, null);

        if(cursor.getCount()==0)
            return false;
        else
            return true;
    }

    public boolean insertFavMovieData(String place_id, String name, String vicinity){


        ContentValues mvalues = new ContentValues();

        mvalues.put(ResContract.ResEntry.COLUMN_PLACE_ID, place_id);
        mvalues.put(ResContract.ResEntry.COLUMN_RES_NAME, name);
        mvalues.put(ResContract.ResEntry.COLUMN_RES_VICINITY, vicinity);

        // Finally, insert movie data into the database.
        Uri insertedUri = getContentResolver().insert(
                ResContract.ResEntry.CONTENT_URI,
                mvalues);

        Long result_id = ContentUris.parseId(insertedUri);

        if(result_id>0 && result_id!=-1){
            Log.v(LOG_TAG, "Insertion Successfuly "+ name);
            return true;

        }else {

            Log.v(LOG_TAG, "Insertion failed "+ name);
            return false;
        }

    }

    public boolean DeleteFavResData(String res_name){

        int no_rows_deleted = getContentResolver()
                .delete(ResContract.ResEntry.buildFavResUri(res_name),
                        ResContract.ResEntry.COLUMN_RES_NAME + "=" + "'" +res_name + "'", null);

        Log.v(LOG_TAG, "Deleted Movie Data "+ res_name + "rows " + no_rows_deleted);

        if(no_rows_deleted > 0){

            return true;

        }else {

            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DetailActivity.this.finishAfterTransition();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrofit_detail_response(String place_ID){

        Retrofit resDetailRetrofit = new Retrofit.Builder()
                .baseUrl(RES_DETAIL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ResRetrofitAPI retrofitDetailAPI = resDetailRetrofit.create(ResRetrofitAPI.class);

        Call<ResDetailJSON> call = retrofitDetailAPI.getRestaurantDetails(place_ID,
                BuildConfig.GOOGLE_PLACES_API_KEY);

        call.enqueue(new Callback<ResDetailJSON>() {
            @Override
            public void onResponse(Response<ResDetailJSON> response, Retrofit retrofit) {

                Log.v(LOG_TAG, "Restaurant Detail Response is " + response.body().getStatus());

                detail_result = response.body().getResults();

                if(detail_result.getWebsite()!=null){

                    Log.v(LOG_TAG, "Place website " + detail_result.getWebsite());
                    website_view.setText(detail_result.getWebsite());
                    website_view.setContentDescription(detail_result.getWebsite());

                    website_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(detail_result.getWebsite()));
                            startActivity(browserIntent);
                        }
                    });

                }else{
                    website_view.setText("URL not found");
                    website_view.setContentDescription("URL not found");
                }

                if(detail_result.getPhoneNum()!=null){

                    Log.v(LOG_TAG, detail_result.getPhoneNum());
                    phoneview.setText(detail_result.getPhoneNum());
                    phoneview.setContentDescription(detail_result.getPhoneNum());

                    phoneicon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

        //                            Toast.makeText(DetailActivity.this, "Phone Icon clicked",
        //                                    Toast.LENGTH_SHORT).show();

                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:"+ detail_result.getPhoneNum()));
                            startActivity(callIntent);
                        }
                    });
                }else{

                    phoneview.setText("Phone Number Not Found");
                    phoneview.setContentDescription("Phone Number Not Found");
                }

                if(detail_result.getDetailOpeningHours()!=null){

                    for(int i=0; i<7; i++){
                        hours_view.append(detail_result.getDetailOpeningHours().getweekhours()[i]);
                        hours_view.setContentDescription(detail_result.getDetailOpeningHours().getweekhours()[0]
                                + detail_result.getDetailOpeningHours().getweekhours()[1]
                                + detail_result.getDetailOpeningHours().getweekhours()[2]
                                + detail_result.getDetailOpeningHours().getweekhours()[3]
                                + detail_result.getDetailOpeningHours().getweekhours()[4]
                                + detail_result.getDetailOpeningHours().getweekhours()[5]
                                + detail_result.getDetailOpeningHours().getweekhours()[6]);
                        hours_view.append("\n");
                        Log.v(LOG_TAG, "hours " + (detail_result.getDetailOpeningHours().getweekhours()[i]));
                    }

                }else{
                    hours_view.setText("Hours not found");
                    hours_view.setContentDescription("Hours not found");
                }

                if(detail_result.getAddress()!=null){

                    Log.v(LOG_TAG, detail_result.getAddress());
                    address.setText(detail_result.getAddress());
                    address.setContentDescription(detail_result.getAddress());
                }else{
                    address.setText("Address not found");
                    address.setContentDescription("Address not found");
                }

                CollapsingToolbarLayout collapsingToolbar =
                        (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

                if(detail_result.getname() != null){

                    collapsingToolbar.setTitle(detail_result.getname() );
                    collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                    collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

                }else{
                    collapsingToolbar.setTitle("Restaurant name not found");
                }


                if(!detail_result.getPhotos().isEmpty()){

                    Log.v(LOG_TAG, detail_result.getPhotos().get(0).getPhotoReference());
                    Log.v(LOG_TAG, String.format("size = %d", detail_result.getPhotos().size()));
                    photoslist = detail_result.getPhotos();

                    Glide.with(getBaseContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxheight=380&photoreference="
                            +photoslist.get(0).getPhotoReference()
                            +"&key=" + BuildConfig.GOOGLE_PLACES_API_KEY).centerCrop().into(backdrop);

                    resPhotoAdapter = new ResPhotoAdapter(getBaseContext(), photoslist);
                    photoRecyclerView.setAdapter(resPhotoAdapter);
                }else{

//                    Glide.with(getBaseContext()).load(detail_result.geticon()).centerCrop().into(backdrop);
                    photoHeading.setText("PHOTOS NOT FOUND");
                    photoHeading.setContentDescription("PHOTOS NOT FOUND");

                }

                if(!detail_result.getReviews().isEmpty()){
                    Log.v(LOG_TAG, detail_result.getReviews().get(0).getAuthor_name());
                    Log.v(LOG_TAG, detail_result.getReviews().get(0).getText());
                    Log.v(LOG_TAG, String.format("Review Rating = %d", detail_result.getReviews().get(0).getRating()));
                    reviewslist = detail_result.getReviews();

                    resReviewAdapter = new ResReviewsAdapter(getBaseContext(), reviewslist);
                    reviewRecyclerView.setAdapter(resReviewAdapter);

                }else{
                    reviewHeading.setText("REVIEWS NOT FOUND");
                    reviewHeading.setContentDescription("REVIEWS NOT FOUND");
                }

                mapImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        Toast.makeText(DetailActivity.this, "Maps clicked",
//                                Toast.LENGTH_SHORT).show();

                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+ detail_result.getAddress());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });

                markedFav = ResMarkedFav(detail_result.getname());

                if(markedFav){

                    fav_fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                }else{

                    fav_fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }

                fav_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        Toast.makeText(DetailActivity.this, "Fav Heart clicked", Toast.LENGTH_SHORT).show();

                        if(markedFav){

                            delete_result = DeleteFavResData(detail_result.getname());

                            if(delete_result){
                                markedFav = false;
                                fav_fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                                Toast.makeText(DetailActivity.this,
                                        "Deleted from Favorites", Toast.LENGTH_SHORT).show();

                            }else{

                                fav_fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                                Toast.makeText(DetailActivity.this,
                                        "Deletion Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            insert_result = insertFavMovieData(placeID, detail_result.getname(),
                                    detail_result.getAddress());

                            if(insert_result){

                                markedFav = true;
                                fav_fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                                Toast.makeText(DetailActivity.this,
                                        "Added to Favorites", Toast.LENGTH_SHORT).show();

                            }else{

                                fav_fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                                Toast.makeText(DetailActivity.this,
                                        "Adding to Fav Failed", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v(LOG_TAG, "On Failure " + t.toString());
            }
        });
    }
}