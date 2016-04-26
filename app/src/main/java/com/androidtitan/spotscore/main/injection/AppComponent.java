package com.androidtitan.spotscore.main.injection;

import android.app.Application;
import android.content.Context;


import com.androidtitan.spotscore.main.ui.activity.LoginActivity;
import com.androidtitan.spotscore.main.ui.fragment.LoginFragment;
import com.androidtitan.spotscore.main.ui.fragment.SignUpFragment;

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
