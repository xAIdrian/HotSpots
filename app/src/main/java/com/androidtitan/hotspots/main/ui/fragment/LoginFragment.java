package com.androidtitan.hotspots.main.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.base.BaseFragment;
import com.androidtitan.hotspots.main.presenter.UserEntryPresenter;
import com.androidtitan.hotspots.main.ui.view.UserEntryView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginFragment extends BaseFragment implements UserEntryView {


    @Inject UserEntryPresenter mUserEntryPresenter;

    @Bind(R.id.input_email) EditText emailEditText;
    @Bind(R.id.input_password) EditText passwordEditText;
    @Bind(R.id.loginFab) FloatingActionButton fab;
    @Bind(R.id.signupTextView) TextView signupTextView;

    public LoginFragment() {
        // Required empty public constructor
    }

    /*public static LoginFragment newInstance(String param1, String param2) {

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(v, getActivity());
        mUserEntryPresenter.attachView(this);


        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserEntryPresenter.showSignUpFragment();

                /*//todo: call to Presenter, which calls back down to the activity to launch a new Fragment
                Intent intent = new Intent(UserEntryActivity.this, SignUpActivity.class);
                startActivity(intent);*/
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View mView = view;

                final String email = emailEditText.getText().toString(); //todo: need to account for special chars
                String password = passwordEditText.getText().toString();

                email.trim();
                password.trim();

                if(email.isEmpty() || password.isEmpty()) {

                    Snackbar.make(mView, getResources().getString(R.string.missing_field), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    /*final String emailAddress = email;
                    //login
                    mRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) { //successful
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("email", emailAddress);
                            mRef.child("users").child(authData.getUid()).setValue(map); //uid is the unique id obtained from Auth

                            Intent intent = new Intent(UserEntryActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) { //authentication failed
                            Snackbar.make(mView, getResources().getString(R.string.auth_failed), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });*/
                }
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mUserEntryPresenter.detachView();
    }


    @Override
    public void showToast() {

    }


}
