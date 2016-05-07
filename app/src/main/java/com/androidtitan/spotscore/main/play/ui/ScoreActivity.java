package com.androidtitan.spotscore.main.play.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.play.presenter.ScorePresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoreActivity extends AppCompatActivity implements ScoreView, View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    @Inject
    ScorePresenter mScorePresenter;

    @Bind(R.id.activity_drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.activity_drawer_navigation_view) NavigationView mNavigation;
    private ImageView mNavDrawerHeaderImage;
    private ImageView mDrawerImage;

    @Bind(R.id.challenge_card_view) CardView mChallengeCard;
    @Bind(R.id.save_card_view) CardView mSaveCard;
    @Bind(R.id.venues_card_view) CardView mVenuesCard;

    @Bind(R.id.scoreIndProgressBar) ProgressBar indeterminateProgress;
    @Bind(R.id.scoreTextView) TextView mScoreText;
    @Bind(R.id.locationImageView) ImageView mLocationImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        mScorePresenter.attachView(this);
        mScorePresenter.takeActivity(ScoreActivity.this);
        mScorePresenter.getLastKnownLocation();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = mNavigation.getHeaderView(0);
        mNavDrawerHeaderImage = (ImageView) headerView.findViewById(R.id.nav_header_bg_imageView);
        mDrawerImage = (ImageView) toolbar.findViewById(R.id.drawer_imageView);
        mScorePresenter.setNavHeaderImageView(mNavDrawerHeaderImage);

        mDrawerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(mNavigation);
            }
        });

        mNavigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        //Handle switching Fragments here
                        mDrawer.closeDrawers();

                        switch (item.getItemId()) {

                            case R.id.nav_drawer_logout:

                                /*
                                mRef.unauth();
                                //todo: this needs to bounce off of our Presenter
                                loadLoginView();
                                */

                                break;

                            default:
                                Log.e(TAG, "Incorrect nav drawer item selected");
                                return false;
                        }

                        return true;
                    }
                });

        mLocationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreText.setVisibility(View.INVISIBLE);
                indeterminateProgress.setVisibility(View.VISIBLE);
                mScorePresenter.getLastKnownLocation();
                mScorePresenter.calculateAndSetScore();
            }
        });

        mChallengeCard.setOnClickListener(this);
        mSaveCard.setOnClickListener(this);
        mVenuesCard.setOnClickListener(this);


    }

    /**
     * Used strictly for actions taken when clicking on cards
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.challenge_card_view:
                Snackbar.make(getCurrentFocus(), "Under Construction", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.save_card_view:
                Snackbar.make(getCurrentFocus(), "Under Construction", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.venues_card_view:
                mScorePresenter.showFragment(new VenueListFragment(), null);

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    ScoreActivity.this.finish();
                }
                return true;

            default:
                Log.e(TAG, "Incorrect optionItemSelected");

                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        mScorePresenter.connectGoogleApiClient();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mScorePresenter.googleApiIsConnected()) {
            mScorePresenter.disconnectGoogleApiClient();
        }
    }

    @Override
    protected void onStop() {
        mScorePresenter.disconnectGoogleApiClient();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScorePresenter.detachView();
    }

    public ScorePresenter getScorePresenter() {
        return mScorePresenter;
    }

    @Override
    public void updateScore(double average) {
        //todo: in here we need to "unlock" our get venues item

        mScoreText.setText(String.format("%.1f", average));
        indeterminateProgress.setVisibility(View.INVISIBLE);
        mScoreText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFragment(Fragment fragment) {

        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
        fragTran.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right);
        fragTran.replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();

    }
}
