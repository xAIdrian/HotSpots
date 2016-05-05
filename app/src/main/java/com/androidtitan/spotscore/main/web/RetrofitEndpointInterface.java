package com.androidtitan.spotscore.main.web;

import com.androidtitan.spotscore.main.data.DetailedVenueResponse;
import com.androidtitan.spotscore.main.data.VenueResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by amohnacs on 5/4/16.
 */
public interface RetrofitEndpointInterface {

    @GET("venues/search")
    Observable<VenueResponse> getVenues(@Query("client_id") String clientId,
                                        @Query("client_secret") String clientSecret,
                                        @Query("v") String version,
                                        @Query("ll") String latLang,
                                        @Query("radius") String radius);


    @GET("venues/{venueId}")
    Observable<DetailedVenueResponse> getDetailedVenue(@Path("venueId") String venueId,
                                                       @Query("client_id") String clientId,
                                                       @Query("client_secret") String clientSecret,
                                                       @Query("v") String version);
}
