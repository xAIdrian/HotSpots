package com.androidtitan.spotscore.main.play.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.androidtitan.spotscore.common.MvpView;

/**
 * Created by amohnacs on 5/2/16.
 */
public interface ScoreView extends MvpView {

    void updateScore(double average);
    void showFragment(Fragment fragment);
}
