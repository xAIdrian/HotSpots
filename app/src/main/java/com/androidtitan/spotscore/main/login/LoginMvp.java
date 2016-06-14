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

        interface OnPasswordFinishedListener {
            void onPasswordSuccess();
        }

        void authenticateLogin(String email, String password, OnLoginFinishedListener listener);
        void createAuthenticatedUser(String email, String password, OnSignUpFinishedListener listener);

        void resetPassword(String email, OnPasswordFinishedListener listener);
    }

    interface View extends MvpView {

        void launchScoreActivity();
        void showLoginFailureSnack(String message);

        void showSignUpSuccessDialog();
        void showSignUpFailureSnack(String message);

        void showPasswordResetSnack(String message);

    }

    interface Presenter {
        void attachView(View mvpView);
        void detachView();

        void authenticateLogin(String email, String password);
        void createAuthenticatedUser(String email, String password);

        void resetPassword(String email);

    }
}
