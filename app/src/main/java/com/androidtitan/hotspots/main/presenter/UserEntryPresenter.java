package com.androidtitan.hotspots.main.presenter;

import com.androidtitan.hotspots.main.ui.activity.UserEntryActivity;
import com.androidtitan.hotspots.main.ui.view.UserEntryView;

/**
 * Created by amohnacs on 4/19/16.
 */
public interface UserEntryPresenter {
    void attachView(UserEntryView mvpView);
    void detachView();

    void takeActivity(UserEntryActivity activity);
    void showLoginFragment();
    void showSignUpFragment();
}
