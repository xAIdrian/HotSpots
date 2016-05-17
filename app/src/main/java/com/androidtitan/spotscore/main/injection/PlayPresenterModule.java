package com.androidtitan.spotscore.main.injection;

import android.content.Context;

import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.presenter.ScorePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 5/3/16.
 */

@Module (
        includes = {
                    AppModule.class,
                    DataManagerModule.class}
)
public class PlayPresenterModule {

    public PlayPresenterModule() {

    }

    @Provides
    public PlayMvp.Presenter providesPlayPresenterModule(Context context, PlayMvp.Model manager) {
        return new ScorePresenter(context, manager);
    }
}
