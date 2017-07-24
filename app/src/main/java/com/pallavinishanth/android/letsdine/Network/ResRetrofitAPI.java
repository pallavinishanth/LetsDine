package com.pallavinishanth.android.letsdine.Network;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public interface ResRetrofitAPI {

    /*
     * Retrofit methods that return near by restaurants and each restaurant details
     */
    @GET("api/place/search/json?type=restaurant")
    Call<ResSearchJSON> getNearbyRestaurants(@Query("location") String location,
                                        @Query("radius") int radius, @Query("key") String API_KEY);

}
