package com.androidtitan.spotscore.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.data.Constants;
import com.androidtitan.spotscore.main.login.ui.LoginActivity;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.firebase.client.Firebase;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private Firebase mRef = new Firebase(Constants.FIREBASE_URL);
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (mRef.getAuth() == null) {
            //todo: this needs to bounce off of our Presenter
            loadLoginView();

        } else {
            Intent intent = new Intent(this, ScoreActivity.class);
            //
            startActivity(intent);
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
