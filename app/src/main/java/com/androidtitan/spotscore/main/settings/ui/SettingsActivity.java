package com.androidtitan.spotscore.main.settings.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.settings.SettingsMvp;

public class SettingsActivity extends AppCompatActivity implements SettingsMvp.View, android.view.View.OnClickListener{


    private ProfileFragment mProfileFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(getIntent().getExtras() != null) {

            if(getIntent().getIntExtra(ScoreActivity.LAUNCH_SETTINGS_EXTRA, 0) == ScoreActivity.LAUNCH_SETTINGS) {
                mProfileFrag = new ProfileFragment();

                FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
                fragTran.add(R.id.fragment_container, mProfileFrag)
                        .addToBackStack(null).commit();
            }

        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

    }
}
