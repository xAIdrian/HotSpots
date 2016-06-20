package com.androidtitan.spotscore.main.play.presenter;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.androidtitan.spotscore.common.BasePresenter;
import com.androidtitan.spotscore.main.data.Score;
import com.androidtitan.spotscore.main.data.foursquare.Venue;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by amohnacs on 5/2/16.
 */
public class ScorePresenter extends BasePresenter<PlayMvp.View> implements PlayMvp.Presenter,
        PlayMvp.Model.ScoreViewListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private final String TAG = getClass().getSimpleName();

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    //@Inject
    PlayMvp.Model mDataManager;

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private ScoreActivity mActivity;

    private LocationRequest mLocationRequest;

    private Location mLastLocation;
    //private LatLng latLng;
    public ArrayList<Venue> mVenueList;

    private boolean mRequestingLocationUpdates = true;

    private double calcAverage;
    private int calcCount;

    @Inject
    public ScorePresenter(Context context, PlayMvp.Model dataManager) {
        mContext = context;
        this.mDataManager = dataManager;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setupLocationRequest();

    }

    @Override
    public void attachView(PlayMvp.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        //if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void takeActivity(ScoreActivity activity) {
        mActivity = activity;
    }

    @Override
    public void connectGoogleApiClient() {
        Log.d(TAG, "connectGoogleApiClient()");
        mGoogleApiClient.connect();
    }

    @Override
    public void disconnectGoogleApiClient() {
        Log.d(TAG, "disconnectGoogleApiClient()");
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean googleApiIsConnected() {

        if (mGoogleApiClient.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void showFragment(Fragment fragment, Bundle args) {

        if (args != null) {
            fragment.setArguments(args);
        }

        getMvpView().showFragment(fragment);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Location services have failed to connected : " + connectionResult);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Location services have been successfully connected");

        calculateAndSetScore();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location services have been suspended : " + i);
    }

    @Override
    public void setupUserProfile() {
        mDataManager.setUserProfile(this);

    }

    @Override
    public void onUserProfileSetFinished() {
        try {
            getMvpView().onUserProfileSetFinished();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScoreSavedFinished() {
        getMvpView().onScoreSavedFinish("Save successful yo!");
    }

    @Override
    public void onScoreSaveFail() {
        getMvpView().onScoreSavedFinish("Can\'t save");
    }
/*
    @Override
    public void setNavHeaderImageView(ImageView imageView) {
        Glide.with(mContext)
                .load("https://unsplash.it/200/200/?random&blur")
                .skipMemoryCache( true )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }*/
    /**
     * Pretty self explanatory yeah?
     * @return the user's current location
     */
    @Override
    public LatLng getLastKnownLocation() {

        LatLng tempLatLng = new LatLng(0, 0);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(mLastLocation != null) {
                tempLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }

        return tempLatLng;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                // same action as StatusCodes.SUCCESS
                Log.d(TAG, "onRequestPermissionResult : StatusCode.SUCCESS");

                calculateAndSetScore();
                Log.e(TAG, "onRequestPermissionsResult");

            } else {
                //todo: Permission was denied or request was cancelled // kick them out of this activity
                Log.d(TAG, "onRequestPermissionResult : StatusCode.FAIL");
            }
        }
    }

    /**
     * For sdk > 21.  Handles runtime permissions and ensures location settings are optimal
     */
    //todo: this needs work for older devices after they select to optimize settings
    protected void setupLocationRequest() {
        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
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
                final LocationSettingsStates states= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        Log.d(TAG, "LocationSettingsStatusCodes.SUCCESS");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        Log.d(TAG, "LocationSettingsStatusCodes.RESOLUTION_REQUIRED");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    mActivity,
                                    REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        Log.d(TAG, "LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");

                        break;
                }
            }
        });
    }

    /**
     * Model retrieves detailed information of every venues neatvy (0.25 mi) and calculates rating.
     * Rating is then sent to ui.
     */
    @Override
    public void calculateAndSetScore() {

        Log.e(TAG, "calculateAndSetScore called");


        if(mGoogleApiClient.isConnected()) {
            mVenueList = new ArrayList<Venue>();
            getLastKnownLocation();

            mDataManager.getVenuesOneByOne(mLastLocation.getLatitude(), mLastLocation.getLongitude())
                    .flatMap(venue -> mDataManager.getAdditionalVenueInfo(venue.getId()))
                    .filter(detailedVenue -> detailedVenue.getRating() > 0.0)
                    .subscribe(new Subscriber<Venue>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "Observable complete");

                            try {
                                getMvpView().updateScore(calcAverage / calcCount);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "RxJava Error :: " + e.toString());
                        }

                        @Override
                        public void onNext(Venue venue) {

                            mVenueList.add(venue);
                            Log.e(TAG, venue.getName());

                            calcAverage += venue.getRating();
                            calcCount ++;
                        }
                    });
        } else {
            setupLocationRequest();
        }

    }

    @Override
    public ArrayList<Venue> getNearbyVenuesList() {

        return mVenueList;
    }

    @Override
    public void saveUserScore(Score score) {

        mDataManager.storeUserScore(score, this);
    }

    @Override
    public int getRemainingSaves() {

        switch (mDataManager.getUserSaves()) { //this is the size of Saves

            case 0 :
                return 3;

            case 1:
                return 2;

            case 2:
                return 1;

            case 3:
                return 0;

            default:
                return 0;

        }

    }

}
