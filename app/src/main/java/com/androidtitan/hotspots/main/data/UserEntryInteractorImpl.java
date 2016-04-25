package com.androidtitan.hotspots.main.data;

import com.androidtitan.hotspots.main.presenter.UserEntryPresenter;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by amohnacs on 4/21/16.
 */
public class UserEntryInteractorImpl implements UserEntryInteractor {

    public static final String FIREBASE_URL = "https://androidtitanhotspots.firebaseio.com";
    Firebase mRef = new Firebase(FIREBASE_URL);

    private boolean mAuthenticationSuccess;

    public UserEntryInteractorImpl() {
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

    public void createAuthenticatedUser(String email, String password) {

        mRef.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                //todo: this needs to be left in our SignupActivity
                //todo:     onUserCreationSuccess()

                /*//todo: let's replace this with something cooler later
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
                dialog.show();*/
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                //todo: this needs to be left in our SignupActivity
                //todo:     onUserCreationFail()

                /*Snackbar.make(mView, getResources().getString(R.string.signup_eror), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

}
