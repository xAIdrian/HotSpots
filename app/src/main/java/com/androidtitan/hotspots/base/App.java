package com.androidtitan.hotspots.base;

import android.app.Application;

import com.androidtitan.hotspots.BuildConfig;
import com.androidtitan.hotspots.main.injection.AppComponent;
import com.androidtitan.hotspots.main.injection.module.AppModule;
import com.androidtitan.hotspots.main.injection.DaggerAppComponent;
import com.firebase.client.Firebase;

import timber.log.Timber;

/**
 * Created by amohnacs on 4/18/16.
 */
public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }


}