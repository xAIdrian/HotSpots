package com.androidtitan.spotscore.main.landing.presenter;

import android.widget.ImageView;

import com.androidtitan.spotscore.main.landing.ui.MainView;
import com.androidtitan.spotscore.main.login.ui.LoginView;

/**
 * Created by amohnacs on 5/5/16.
 */
public interface MainPresenter {
    void attachView(MainView mvpView);
    void detachView();

    void setNavDrawerUserName(String userId);
    void setNavHeaderImageView(ImageView imageView);

}
