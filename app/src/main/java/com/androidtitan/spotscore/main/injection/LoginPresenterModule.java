package com.androidtitan.spotscore.main.injection;

import android.content.Context;


import com.androidtitan.spotscore.main.login.presenter.LoginPresenter;
import com.androidtitan.spotscore.main.login.presenter.LoginPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 4/21/16.
 */
@Module(
        includes = { AppModule.class }
)
public class LoginPresenterModule {

    public LoginPresenterModule() {

    }

    @Provides
    public LoginPresenter providesLoginPresenterModule(Context context) {
        return new LoginPresenterImpl(context);
    }

}
