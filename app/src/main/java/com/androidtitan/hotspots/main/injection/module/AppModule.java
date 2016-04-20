package com.androidtitan.hotspots.main.injection.module;

import android.app.Application;
import android.content.Context;

import com.androidtitan.hotspots.main.data.helper.FirebaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 4/19/16.
 */
@Module
public class AppModule {

    private static Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides @Singleton
    Context providesApplicationContext() {
        return application.getBaseContext();
    }

    @Provides @Singleton
    FirebaseHelper providesFirebaseHelper() {
        return new FirebaseHelper();
    }
}
