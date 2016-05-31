package com.androidtitan.spotscore.main.injection;

import android.app.Application;
import android.content.Context;


import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.settings.SettingsMvp;
import com.androidtitan.spotscore.main.settings.ui.CredentialsFragmentDialog;
import com.androidtitan.spotscore.main.settings.ui.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                LoginPresenterModule.class,
                PlayPresenterModule.class,
                SettingsPresenterModule.class,
                DataManagerModule.class}
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    PlayMvp.Model getPlayDataManagerInterface();
    SettingsMvp.Model getSettingsDataManagerInterface();

    void inject(LoginActivity activity);
    void inject(ScoreActivity activity);
    void inject(SettingsActivity activity);

    void inject(CredentialsFragmentDialog fragment);

}
