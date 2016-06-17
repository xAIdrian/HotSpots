package com.androidtitan.spotscore.main.play.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.data.Constants;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.adapter.ScoreOptionsAdapter;
import com.androidtitan.spotscore.main.settings.ui.SettingsActivity;
import com.firebase.client.Firebase;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.doorbell.android.Doorbell;

public class ScoreActivity extends AppCompatActivity implements PlayMvp.View {
    private final String TAG = getClass().getSimpleName();

    public static final String LAUNCH_SETTINGS_EXTRA = "scoreactivity.launchsettingsextra";
    public static final String SAVE_DIALOG_DISPLAY_SCORE = " scoreactivity.savedialogdisplayscore";

    public static final int LAUNCH_SETTINGS = 1;


    @Inject
    PlayMvp.Presenter mPlayPresenter;

    private Firebase mRef = new Firebase(Constants.FIREBASE_URL);
    private User mUser;

    private SaveScoreFragDialog saveFrag;

    private ActionBarDrawerToggle mActionToggle;

    @Bind(R.id.activity_drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.activity_drawer_navigation_view) NavigationView mNavigation;
    private TextView mProfileText;
    private ImageView mProfileImage;

    @Bind(R.id.scoreTextView) TextView mScoreText;
    @Bind(R.id.scoreIndProgressBar) ProgressBar indeterminateProgress;

    @Bind(R.id.locationFab) FloatingActionButton mLocationFab;
    @Bind(R.id.scoreList) RecyclerView mRecyclerView;
    private ScoreOptionsAdapter mAdapter;

    private boolean mScoreIsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        mPlayPresenter.attachView(this);
        mPlayPresenter.takeActivity(ScoreActivity.this);

        mUser = User.getInstance();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        android.view.View headerView = mNavigation.getHeaderView(0);
        mProfileText = (TextView) headerView.findViewById(R.id.nav_drawer_header_username);
        mProfileImage = (ImageView) headerView.findViewById(R.id.profileCircleImageView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ScoreOptionsAdapter(this, mPlayPresenter);
        mRecyclerView.setAdapter(mAdapter);

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

                            case R.id.nav_drawer_feedback:
                                new Doorbell(ScoreActivity.this, 3836,
                                        Constants.DOORBELLIO_API_KEY)
                                        .setEmail(mUser.getEmail())
                                        .show();
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

            }
        });

        //additional tasks for setting things up.
        mPlayPresenter.setupUserProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_score, menu);
        return true;
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

//        mPlayPresenter.getLastKnownLocation();
        mPlayPresenter.calculateAndSetScore();

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mPlayPresenter.googleApiIsConnected()) {
            mPlayPresenter.connectGoogleApiClient();
        }
//        mPlayPresenter.getLastKnownLocation();
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
        mPlayPresenter = null;
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

    }

    @Override
    public void showFragment(Fragment fragment) {

        mLocationFab.hide();

        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
        fragTran.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right);
        fragTran.add(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();

        animationUpIndicator(true);

    }

    @Override
    public void onUserProfileSetFinished() {
        mProfileImage.setImageBitmap(mUser.getProfileImage());
        mProfileText.setText(mUser.getName());
    }

    @Override
    public void onScoreSavedFinish (String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
        saveFrag.getDialog().dismiss();
    }


    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        //prevents the user from going back to the main activity when pressing back
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void showScoreDialog() {

        if(mScoreIsLoaded) {

            Bundle args = new Bundle();
            args.putString(SAVE_DIALOG_DISPLAY_SCORE, mScoreText.getText().toString());

            saveFrag = new SaveScoreFragDialog();
            saveFrag.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
            saveFrag.setArguments(args);

            saveFrag.show(getSupportFragmentManager(), "saveFrag");

        } else {
            Snackbar.make(getCurrentFocus(), "Patience padowon.  The score is loading", Snackbar.LENGTH_LONG).show();

        }
    }
}
