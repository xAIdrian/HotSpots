package com.androidtitan.spotscore.main.login.presenter;


import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.login.ui.LoginView;

/**
 * Created by amohnacs on 4/21/16.
 */
public interface LoginPresenter {
    void attachView(LoginView mvpView);
    void detachView();

    void showLoginFragment();
    void showSignUpFragment();

    void authenticateLogin(String email, String password);
    void createAuthenticatedUser(String email, String password);
}
