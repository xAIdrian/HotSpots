package com.androidtitan.hotspots.main.data;

import com.androidtitan.hotspots.main.data.helper.FirebaseHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by amohnacs on 4/19/16.
 */

@Singleton
public class DataManager {

    private final FirebaseHelper mFirebaseHelper;

    @Inject
    public DataManager(FirebaseHelper firebaseHelper) {
        this.mFirebaseHelper = firebaseHelper;
    }

    //todo: Observalbe return type is the end game
    public void firebaseAuthenticateLogin(String email, String password) {

        //in here we are getting back the Observable that was given to us by the helper
        //passing it through some changes and sending it back to the presenter
    }


}
