package com.androidtitan.spotscore.main.login.web;

import android.util.Log;

import com.androidtitan.spotscore.common.data.Constants;
import com.androidtitan.spotscore.main.login.LoginMvp;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amohnacs on 4/21/16.
 */
public class LoginInteractor implements LoginMvp.Model {
    private final String TAG = getClass().getSimpleName();

    Firebase mRef = new Firebase(Constants.FIREBASE_URL);

    private boolean mAuthenticationSuccess;

    public LoginInteractor() {
    }

    @Override
    public void authenticateLogin(String email, String password, final OnLoginFinishedListener listener) {
        final String emailAddress = email;

        Log.e(TAG, email + ", " + password);

        mRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) { //successful
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("email", emailAddress);
                mRef.child("users").child(authData.getUid()).setValue(map); //uid is the unique id obtained from Auth

                Log.e(TAG, "Success!");

                listener.onAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e(TAG, "fail!");

                listener.onAuthenticationFailure();
            }
        });
    }

    @Override
    public void createAuthenticatedUser(String email, String password, final OnSignUpFinishedListener listener) {

        mRef.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {

                Log.e(TAG, "User successfully created");
                listener.onSignUpSuccess();

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.e(TAG, "User creation failed");
                listener.onSignUpFailure();
            }
        });
    }
}
