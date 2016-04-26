package com.androidtitan.spotscore.main.data;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by amohnacs on 4/21/16.
 */
public class LoginInteractorImpl implements LoginInteractor{

    public static final String FIREBASE_URL = "https://androidtitanhotspots.firebaseio.com";
    Firebase mRef = new Firebase(FIREBASE_URL);

    private boolean mAuthenticationSuccess;

    public LoginInteractorImpl() {
    }

    @Override
    public void authenticateLogin(String email, String password, final OnLoginFinishedListener listener) {
        final String emailAddress = email;

        mRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) { //successful
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("email", emailAddress);
                mRef.child("users").child(authData.getUid()).setValue(map); //uid is the unique id obtained from Auth

                listener.onAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onAuthenticationFailure();
            }
        });
    }

    @Override
    public void createAuthenticatedUser(String email, String password, final OnSignUpFinishedListener listener) {

        mRef.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {

                Timber.e("create user success");
                listener.onSignUpSuccess();

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Timber.e("create user failure");
                listener.onSignUpFailure();
            }
        });
    }
}
