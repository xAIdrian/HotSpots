package com.androidtitan.spotscore.main.play.presenter;

import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.play.ui.ScoreView;
import com.google.android.gms.maps.model.LatLng;

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

    LatLng getLastKnownLocation();
}
