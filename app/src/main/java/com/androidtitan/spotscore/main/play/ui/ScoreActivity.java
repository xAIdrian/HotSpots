package com.androidtitan.spotscore.main.play.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.data.Constants;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.settings.ui.SettingsActivity;
import com.firebase.client.Firebase;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoreActivity extends AppCompatActivity implements PlayMvp.View, android.view.View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    public static String LAUNCH_SETTINGS_EXTRA = "scoreactivity.launchsettingsextra";
    public static int LAUNCH_SETTINGS = 1;

    @Inject
    PlayMvp.Presenter mPlayPresenter;

    private Firebase mRef = new Firebase(Constants.FIREBASE_URL);
    private String mUserId;

    ActionBarDrawerToggle mActionToggle;
    @Bind(R.id.activity_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.activity_drawer_navigation_view)
    NavigationView mNavigation;
    private ImageView mNavDrawerHeaderImage;
    private TextView mUsernameText;

    @Bind(R.id.challengeTextView)
    TextView mChallengeText;
    @Bind(R.id.saveTextView)
    TextView mSaveText;
    @Bind(R.id.venuesListTextView)
    RelativeLayout mVenuesCard;
    @Bind(R.id.venuesTextView)
    TextView mVenueText;

    @Bind(R.id.scoreIndProgressBar)
    ProgressBar indeterminateProgress;
    @Bind(R.id.scoreTextView)
    TextView mScoreText;
    @Bind(R.id.locationFab)
    FloatingActionButton mLocationFab;

    private boolean mScoreIsLoaded = false;
    private boolean mFragmentIsShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        mPlayPresenter.attachView(this);
        mPlayPresenter.takeActivity(ScoreActivity.this);

        /**
         * checking for authentication
         */

            mUserId = mRef.getAuth().getUid();
            mPlayPresenter.setNavDrawerUserName(mUserId);


            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            android.view.View headerView = mNavigation.getHeaderView(0);
            mUsernameText = (TextView) headerView.findViewById(R.id.nav_drawer_header_username);
            mNavDrawerHeaderImage = (ImageView) headerView.findViewById(R.id.nav_header_bg_imageView);
            mPlayPresenter.setNavHeaderImageView(mNavDrawerHeaderImage);

            mActionToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            );
            mActionToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mActionToggle);

            mActionToggle.setToolbarNavigationClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    Log.e(TAG, "HOME");
                }
            });

            mNavigation.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            //Handle switching Fragments here
                            mDrawerLayout.closeDrawers();

                            switch (item.getItemId()) {




                                case R.id.nav_drawer_profile:

                                    Intent profileIntent = new Intent(ScoreActivity.this, SettingsActivity.class);
                                    profileIntent.putExtra(LAUNCH_SETTINGS_EXTRA, LAUNCH_SETTINGS);
                                    startActivity(profileIntent);

                                    break;

                                case R.id.nav_drawer_logout:

                                    mRef.unauth();
                                    //todo: this needs to bounce off of our Presenter
                                    loadLoginView();

                                    break;

                                default:
                                    Log.e(TAG, "Incorrect nav drawer item selected");
                                    return false;
                            }

                            return true;
                        }
                    });

            //mLocationFab.hide();
            mLocationFab.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    mScoreIsLoaded = false;

                    mLocationFab.hide();
                    mScoreText.setVisibility(android.view.View.INVISIBLE);
                    indeterminateProgress.setVisibility(android.view.View.VISIBLE);
                    mPlayPresenter.getLastKnownLocation();
                    mPlayPresenter.calculateAndSetScore();

                    //todo: as we add functionality disable the colors

                    mVenueText.setTextColor(ContextCompat.getColor(ScoreActivity.this, R.color.colorDivider));
                }
            });

            mChallengeText.setOnClickListener(this);
            mSaveText.setOnClickListener(this);
            mVenuesCard.setOnClickListener(this);


    }

    /**
     * Used strictly for actions taken when clicking on cards
     *
     * @param view
     */
    @Override
    public void onClick(android.view.View view) {

        if (mScoreIsLoaded) {

            switch (view.getId()) {
                case R.id.saveTextView:
                    Snackbar.make(getCurrentFocus(), "Under Construction", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;

                case R.id.challengeTextView:
                    Snackbar.make(getCurrentFocus(), "Under Construction", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;

                case R.id.venuesListTextView:
                    mPlayPresenter.showFragment(new VenueListFragment(), null);

                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportFragmentManager().popBackStack();
                    animationUpIndicator(false);
                    mLocationFab.show();

                } else {

                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

                return true;

            default:
                Log.e(TAG, "Incorrect optionItemSelected");

                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportFragmentManager().popBackStack();
            animationUpIndicator(false);
            mLocationFab.show();

        } else {

            this.finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionToggle.syncState();
    }

    @Override
    protected void onStart() {
        mPlayPresenter.connectGoogleApiClient();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPlayPresenter.getLastKnownLocation();
        mPlayPresenter.calculateAndSetScore();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayPresenter.googleApiIsConnected()) {
            mPlayPresenter.disconnectGoogleApiClient();
        }
    }

    @Override
    protected void onStop() {
        mPlayPresenter.disconnectGoogleApiClient();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayPresenter.detachView();
    }

    public PlayMvp.Presenter getScorePresenter() {
        return mPlayPresenter;
    }

    private void animationUpIndicator(boolean animateToArrow) {

        ValueAnimator anim;

        if (animateToArrow) {
            anim = ValueAnimator.ofFloat(0, 1);
        } else {
            anim = ValueAnimator.ofFloat(1, 0);
        }

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mActionToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.start();
    }

    @Override
    public void updateScore(double average) {

        mScoreIsLoaded = true;

        mLocationFab.show();
        mScoreText.setText(String.format("%.1f", average));
        indeterminateProgress.setVisibility(android.view.View.INVISIBLE);
        mScoreText.setVisibility(android.view.View.VISIBLE);

        //todo: as we add functionality enable the colors for the other clickable textviews

        mVenueText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    public void showFragment(Fragment fragment) {

        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActionToggle.syncState();*/
        mLocationFab.hide();

        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
        fragTran.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right);
        fragTran.add(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();

        animationUpIndicator(true);

    }

    @Override
    public void setNavDrawerUserName(String userName) {
        mUsernameText.setText(userName);
    }

    private void loadLoginView() {
        //prevents the user from going back to the main activity when pressing back
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
