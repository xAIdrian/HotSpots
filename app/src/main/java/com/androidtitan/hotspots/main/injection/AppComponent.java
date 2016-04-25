package com.androidtitan.hotspots.main.injection;

import android.app.Application;
import android.content.Context;

import com.androidtitan.hotspots.base.BaseActivity;
import com.androidtitan.hotspots.main.injection.module.AppModule;
import com.androidtitan.hotspots.main.injection.module.UserEntryPresenterModule;
import com.androidtitan.hotspots.main.ui.fragment.LoginFragment;
import com.androidtitan.hotspots.main.ui.fragment.SignUpFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by amohnacs on 4/19/16.
 */

@Singleton
@Component (
        modules = { AppModule.class,
                    UserEntryPresenterModule.class
        }
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    void inject(BaseActivity baseActivity);

    //todo: can we replace this with our base fragment
    void inject(LoginFragment loginFragment);
    void inject(SignUpFragment signUpFragment);

}
