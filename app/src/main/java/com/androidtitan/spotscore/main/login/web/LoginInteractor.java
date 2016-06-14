package com.androidtitan.spotscore.main.login.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.data.Constants;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.login.LoginMvp;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amohnacs on 4/21/16.
 */
public class LoginInteractor implements LoginMvp.Model {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    private final User mUser;
    private Firebase mRefUserBase;

    Firebase mRef = new Firebase(Constants.FIREBASE_URL);

    private boolean mAuthenticationSuccess;

    public LoginInteractor(Context context) {

        mContext = context;

        mUser = User.getInstance();
        mRefUserBase = new Firebase(Constants.FIREBASE_URL + "/users/" + mUser.getUserId());
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
                //todo: this bad bitch is causing me problems...must have spent some time in the dog fighting rings of Mexico.
                mRef.child("users").child(authData.getUid()).updateChildren(map); //uid is the unique id obtained from Auth

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

    @Override
    public void resetPassword(String email, OnPasswordFinishedListener listener) {
        mRef.resetPassword(email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                listener.onPasswordSuccess();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.e(TAG, "Password reset failed");
            }
        });
    }
}
