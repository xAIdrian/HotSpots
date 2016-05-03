package com.androidtitan.spotscore.main.injection;

import android.app.Application;
import android.content.Context;


import com.androidtitan.spotscore.main.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                    LoginPresenterModule.class }
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    void inject(LoginActivity activity);

}
