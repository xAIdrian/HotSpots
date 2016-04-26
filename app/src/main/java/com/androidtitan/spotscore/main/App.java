package com.androidtitan.spotscore.main;

import android.app.Application;

import com.androidtitan.spotscore.main.injection.AppComponent;
import com.androidtitan.spotscore.main.injection.AppModule;
import com.androidtitan.spotscore.main.injection.DaggerAppComponent;
import com.firebase.client.Firebase;

/**
 * Created by amohnacs on 3/14/16.
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

    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }


}
