package com.androidtitan.spotscore.main.injection;

import android.content.Context;

import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.login.presenter.LoginPresenter;
import com.androidtitan.spotscore.main.login.presenter.LoginPresenterImpl;
import com.androidtitan.spotscore.main.play.presenter.ScorePresenter;
import com.androidtitan.spotscore.main.play.presenter.ScorePresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 5/3/16.
 */

@Module (
        includes = { AppModule.class }
)
public class ScorePresenterModule {

    public ScorePresenterModule() {

    }

    @Provides
    public ScorePresenter providesPlayPresenterModule(Context context) {
        return new ScorePresenterImpl(context);
    }
}
