package com.androidtitan.spotscore.main.play.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.androidtitan.spotscore.main.data.Venue;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.play.ui.ScoreView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amohnacs on 5/2/16.
 */
public interface ScorePresenter {

    void attachView(ScoreView mvpView);
    void detachView();

    void takeActivity(ScoreActivity activity);

    void connectGoogleApiClient();
    void disconnectGoogleApiClient();
    boolean googleApiIsConnected();

    void showFragment(Fragment fragment, Bundle arguments);

    LatLng getLastKnownLocation();
    void calculateAndSetScore();

    ArrayList<Venue> getNearbyVenuesList();

    void setNavHeaderImageView(ImageView mNavDrawerHeaderImage);
    void setNavDrawerUserName(String mUserId);
}
