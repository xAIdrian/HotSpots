package com.androidtitan.spotscore.main.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.androidtitan.spotscore.common.MvpView;
import com.androidtitan.spotscore.main.data.Score;
import com.androidtitan.spotscore.main.data.foursquare.Venue;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by amohnacs on 5/16/16.
 */
public interface PlayMvp {

    interface Model {

        interface ScoreViewListener {
            void onUserProfileSetFinished();
            void onScoreSavedFinished();
            void onScoreSaveFail();

        }


        Observable<Venue> getVenuesOneByOne(double latitude, double longitude);
        Observable<Venue> getAdditionalVenueInfo(String venueIdentifier);

        void setUserProfile(ScoreViewListener listener);
        void storeUserScore(Score score, ScoreViewListener listener);

        int getUserSaves();

    }

    interface View extends MvpView {

        void updateScore(double average);
        void showFragment(Fragment fragment);

        void onUserProfileSetFinished();
        void onScoreSavedFinish(String message);
    }

    interface Presenter {

        void attachView(View mvpView);
        void detachView();

        void takeActivity(ScoreActivity activity);

        void connectGoogleApiClient();
        void disconnectGoogleApiClient();
        boolean googleApiIsConnected();

        void setupUserProfile();

        void showFragment(Fragment fragment, Bundle arguments);

        LatLng getLastKnownLocation();
        void calculateAndSetScore();

        ArrayList<Venue> getNearbyVenuesList();
        
        void saveUserScore(Score score);

        int getRemainingSaves();
    }
}
