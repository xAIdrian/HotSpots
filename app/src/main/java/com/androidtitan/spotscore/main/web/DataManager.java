package com.androidtitan.spotscore.main.web;

import com.androidtitan.spotscore.main.data.Venue;

import rx.Observable;

/**
 * Created by amohnacs on 5/3/16.
 */
public interface DataManager {

    interface ScoreViewListener {
        void onUsernameFinished(String userName);
    }

    void setNavDrawerUserName(String userId, ScoreViewListener listener);

    Observable<Venue> getVenuesOneByOne(double latitude, double longitude);
    Observable<Venue> getAdditionalVenueInfo(String venueIdentifier);
}
