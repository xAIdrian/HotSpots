package com.androidtitan.spotscore.main.injection;

import android.content.Context;

import com.androidtitan.spotscore.main.landing.presenter.MainPresenter;
import com.androidtitan.spotscore.main.landing.presenter.MainPresenterImpl;
import com.androidtitan.spotscore.main.web.DataManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 5/5/16.
 */

@Module (
        includes = {
                AppModule.class,
                DataManagerModule.class }
)
public class LandingPresenterModule {

    LandingPresenterModule() {

    }

    @Provides
    public MainPresenter privdesMainPresenterModule(Context context, DataManager manager) {
        return new MainPresenterImpl(context, manager);
    }
}
