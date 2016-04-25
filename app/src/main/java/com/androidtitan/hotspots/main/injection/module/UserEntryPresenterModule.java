package com.androidtitan.hotspots.main.injection.module;

import android.content.Context;

import com.androidtitan.hotspots.main.presenter.UserEntryPresenter;
import com.androidtitan.hotspots.main.presenter.UserEntryPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 4/19/16.
 */

@Module (
        includes = { AppModule.class }
)
public class UserEntryPresenterModule {

    public UserEntryPresenterModule() {

    }

    @Provides @Singleton
    public UserEntryPresenter providesUserEntryPresenter(Context context) {
        return new UserEntryPresenterImpl(context);
    }
}
