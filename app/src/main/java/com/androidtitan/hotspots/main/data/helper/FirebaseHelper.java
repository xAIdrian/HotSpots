package com.androidtitan.hotspots.main.data.helper;

import android.content.Intent;
import android.support.design.widget.Snackbar;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.ui.activity.UserEntryActivity;
import com.androidtitan.hotspots.main.ui.activity.MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amohnacs on 4/19/16.
 */
public class FirebaseHelper {
    public static final String FIREBASE_URL = "https://androidtitanhotspots.firebaseio.com";

    //todo: Firebase wrapped in RxJava
    //  http://andydyer.org/blog/2015/04/06/loading-a-list-of-objects-from-firebase/
    //  https://gist.github.com/gsoltis/86210e3259dcc6998801

    Firebase mRef = new Firebase(FIREBASE_URL);

    public FirebaseHelper() {
    }

    public void authenticateLogin(String email, String password) {
        final String emailAddress = email;

        mRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) { //successful
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("email", emailAddress);
                mRef.child("users").child(authData.getUid()).setValue(map); //uid is the unique id obtained from Auth

                //todo: this needs to be left in our UserEntryActivity
                //todo: passed down using Observables
                //todo:     onAuthenticationApproved()
                Intent intent = new Intent(UserEntryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) { //authentication failed
                //todo: this needs to be left in our UserEntryActivity
                //todo:     onAuthenticationDenied()
                Snackbar.make(mView, getResources().getString(R.string.auth_failed), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void createAuthenticatedUser(String email, String password) {

        mRef.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                //todo: this needs to be left in our SignupActivity
                //todo:     onUserCreationSuccess()

                //todo: let's replace this with something cooler later
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(R.string.signup_success)
                        .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(SignUpActivity.this, UserEntryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                //todo: this needs to be left in our SignupActivity
                //todo:     onUserCreationFail()

                Snackbar.make(mView, getResources().getString(R.string.signup_eror), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public DataSnapshot getUserInformation(String mUserId) {

        Firebase fb = new Firebase(Constants.FIREBASE_URL + "/users/" + mUserId + "/email");


        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Timber.d(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
