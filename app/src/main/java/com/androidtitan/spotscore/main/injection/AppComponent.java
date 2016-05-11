package com.androidtitan.spotscore.main.injection;

import android.app.Application;
import android.content.Context;


import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.web.DataManager;

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
    DataManager getDataManager();

    void inject(LoginActivity activity);
    void inject(ScoreActivity activity);

}
