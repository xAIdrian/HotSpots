package com.androidtitan.spotscore.main.login.ui;


import com.androidtitan.spotscore.common.MvpView;

/**
 * Created by amohnacs on 4/21/16.
 */
public interface LoginView extends MvpView {

    void launchMainActivity();
    void showLoginFailureSnack(String message);

    void showSignUpSuccessDialog();
    void showSignUpFailureSnack(String message);

    void showLoginFragment();
    void showSignUpFragment();
}
