package com.androidtitan.spotscore.main.data;

/**
 * Created by amohnacs on 4/21/16.
 */
public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onAuthenticationSuccess();
        void onAuthenticationFailure();
    }

    interface OnSignUpFinishedListener {
        void onSignUpSuccess();
        void onSignUpFailure();
    }

    void authenticateLogin(String email, String password, OnLoginFinishedListener listener);
    void createAuthenticatedUser(String email, String password, OnSignUpFinishedListener listener);
}
