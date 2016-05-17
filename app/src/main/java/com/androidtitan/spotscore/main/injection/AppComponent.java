package com.androidtitan.spotscore.main.injection;

import android.app.Application;
import android.content.Context;


import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                LoginPresenterModule.class,
                PlayPresenterModule.class,
                DataManagerModule.class}
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();
    PlayMvp.Model getPlayDataManagerInterface();
    //todo: we need additional getters for our additional Model interfaces

    void inject(LoginActivity activity);
    void inject(ScoreActivity activity);

}
