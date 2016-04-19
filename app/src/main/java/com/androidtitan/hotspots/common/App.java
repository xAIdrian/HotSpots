package com.androidtitan.hotspots.common;

import android.app.Application;

import com.androidtitan.hotspots.BuildConfig;
import com.firebase.client.Firebase;

import timber.log.Timber;

/**
 * Created by amohnacs on 4/18/16.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


}