package com.androidtitan.spotscore.main.presenter;


import com.androidtitan.spotscore.main.ui.activity.LoginActivity;
import com.androidtitan.spotscore.main.ui.view.LoginView;

/**
 * Created by amohnacs on 4/21/16.
 */
public interface LoginPresenter {
    void attachView(LoginView mvpView);
    void detachView();

    void takeActivity(LoginActivity activity);
    void showLoginFragment();
    void showSignUpFragment();

    void authenticateLogin(String email, String password);
    void createAuthenticatedUser(String email, String password);
}
