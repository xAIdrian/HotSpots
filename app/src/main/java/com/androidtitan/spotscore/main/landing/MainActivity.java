package com.androidtitan.spotscore.main.landing;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    public static final String MAP_TYPE_EXTRA = "mainactivity.maptypeextra";
    public static final int MAP_VIEW = 0;
    public static final int MAP_PLAY = 1;

    public static final String FIREBASE_URL = "https://androidtitanhotspots.firebaseio.com";
    Firebase mRef = new Firebase(FIREBASE_URL);

    private User mUser;

    private String mUserId;
    private Firebase fb;

    @Bind(R.id.activity_drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.activity_drawer_navigation_view) NavigationView mNavigation;
    @Bind(R.id.play_imageView) ImageView mPlayImage;
    @Bind(R.id.leaderboard_imageView) ImageView mLeaderboardImage;
    @Bind(R.id.saved_imageView) ImageView mSavedImage;
    @Bind(R.id.past_imageView) ImageView mExtraImage;
    private TextView mUsernameText;
    private ImageView mDrawerImage;
    private ImageView mMapImage;

    private View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mUser = User.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(null);

        View headerView = mNavigation.getHeaderView(0);
        mUsernameText = (TextView) headerView.findViewById(R.id.nav_drawer_header_username);
        mDrawerImage = (ImageView) toolbar.findViewById(R.id.drawer_imageView);
        mMapImage = (ImageView) toolbar.findViewById(R.id.map_imageView);

        mPlayImage.setOnClickListener(this);

        if (mRef.getAuth() == null) {
            loadLoginView();
        }

        try {
            mUserId = mRef.getAuth().getUid();
            fb = new Firebase(FIREBASE_URL + "/users/" + mUserId + "/email");

        } catch (Exception e) {
            loadLoginView();
        }

        try {
            //this is used to populate using data from firebase
            fb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUser.setEmail(dataSnapshot.getValue().toString());
                    mUsernameText.setText(mUser.getEmail());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Timber.e("Cancelled :" + firebaseError);
                }
            });
        } catch (NullPointerException e) {
            Timber.e("We're not ready yet...");
        }

        mDrawerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(mNavigation);
            }
        });

        mMapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getCurrentFocus(), "Under Construction", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

                                mRef.unauth();
                                loadLoginView();

                                break;

                            default:
                                Timber.e("Incorrect nav drawer item selected");
                                return false;
                        }

                        return true;
                    }
                });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.play_imageView:
                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra(MAP_TYPE_EXTRA, MAP_PLAY);
                startActivity(intent);

                break;

            case R.id.leaderboard_imageView:


                break;

            case R.id.saved_imageView:


                break;

            case R.id.past_imageView:


                break;

            default:
                Timber.e("Failed to click on an appropriate image");
                break;

        }
    }

    private void loadLoginView() {
        //prevents the user from going back to the main activity when pressing back
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
