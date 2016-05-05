package com.androidtitan.spotscore.main.web;

import com.androidtitan.spotscore.main.data.Venue;
import com.androidtitan.spotscore.main.data.VenueResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by amohnacs on 5/3/16.
 */
public interface DataManager {

    //keep our return listener up here

    Observable<Venue> getVenuesOneByOne(double latitude, double longitude);
    Observable<Venue> getDetailedVenue(String venueIdentifier);
}
