package com.androidtitan.spotscore.main.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.ui.view.MainView;
import com.firebase.client.Firebase;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainView {

    public static final String FIREBASE_URL = "https://androidtitanhotspots.firebaseio.com";
    Firebase mRef = new Firebase(FIREBASE_URL);
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //todo: put this in our presenter
        if(mRef.getAuth() == null) {
            loadLoginView();
        }

        try {
            mUserId = mRef.getAuth().getUid();

        } catch (Exception e) {
            loadLoginView();
        }

        /*Firebase fb = new Firebase(Constants.FIREBASE_URL + "/users/" + mUserId + "/email");

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Timber.d(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout:

                mRef.unauth();
                loadLoginView();
                break;

            default:
                Timber.e("Appropriate menu option was not selected  ");
                break;

        }

        return true;
    }

    private void loadLoginView() {
        //prevents the user from going back to the main activity when pressing back
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
