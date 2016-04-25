package com.androidtitan.hotspots.main.ui.view;

import com.androidtitan.hotspots.base.MvpView;

/**
 * Created by amohnacs on 4/19/16.
 */
public interface UserEntryView extends MvpView {

    void launchMainActivity();
    void showFailureSnack(String message);
    /*
    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();
    */
}
