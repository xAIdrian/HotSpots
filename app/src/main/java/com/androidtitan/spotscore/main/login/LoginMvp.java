package com.androidtitan.spotscore.main.login;

import com.androidtitan.spotscore.common.MvpView;

/**
 * Created by amohnacs on 5/16/16.
 */
public interface LoginMvp {

    interface Model {

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

    interface View extends MvpView {

        void launchScoreActivity();
        void showLoginFailureSnack(String message);

        void showSignUpSuccessDialog();
        void showSignUpFailureSnack(String message);

        void showLoginFragment();
        void showSignUpFragment();
    }

    interface Presenter {
        void attachView(View mvpView);
        void detachView();

        void showLoginFragment();
        void showSignUpFragment();

        void authenticateLogin(String email, String password);
        void createAuthenticatedUser(String email, String password);

    }
}
