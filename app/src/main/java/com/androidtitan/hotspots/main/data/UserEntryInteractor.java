package com.androidtitan.hotspots.main.data;

import com.androidtitan.hotspots.main.presenter.UserEntryPresenter;

/**
 * Created by amohnacs on 4/21/16.
 */
public interface UserEntryInteractor {

    interface OnLoginFinishedListener {
        void onAuthenticationSuccess();
        void onAuthenticationFailure();
    }

    void authenticateLogin(String email, String password, OnLoginFinishedListener listener);
}
