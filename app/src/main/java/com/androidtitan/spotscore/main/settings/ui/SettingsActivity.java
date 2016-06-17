package com.androidtitan.spotscore.main.settings.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.settings.SettingsMvp;
import com.androidtitan.spotscore.main.settings.presenter.SettingsPresenter;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity implements SettingsMvp.View {
    private final String TAG = getClass().getSimpleName();

    @Inject
    SettingsMvp.Presenter mSettingsPresenter;

    private ProfileFragment mProfileFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        App.getAppComponent().inject(this);

        mSettingsPresenter.attachView(this);
        mSettingsPresenter.takeActivity(SettingsActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras() != null) {

            if(getIntent().getIntExtra(ScoreActivity.LAUNCH_SETTINGS_EXTRA, 0) == ScoreActivity.LAUNCH_SETTINGS) {
                mProfileFrag = new ProfileFragment();

                FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
                fragTran.add(R.id.fragment_container, mProfileFrag)
                        .addToBackStack(null).commit();

            } //else if

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingsPresenter.detachView();
        mSettingsPresenter = null;
    }

    public SettingsMvp.Presenter getPresenter() {
        return mSettingsPresenter;
    }

    @Override
    public void setProfileImage(Bitmap bm) {
        mProfileFrag.setProfileImage(bm);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
    }
}
