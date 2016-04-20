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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpFragment extends BaseFragment {


    @Inject UserEntryPresenter mUserEntryPresenter;


    @Bind(R.id.input_email) EditText emailEditText;
    @Bind(R.id.input_password) EditText passwordEditText;
    @Bind(R.id.signupFab) FloatingActionButton fab;
    @Bind(R.id.loginTextView) TextView loginTextView;

    public SignUpFragment() {
        // Required empty public constructor
    }


    /*public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // was something passed to us?
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(v, getActivity());


        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserEntryPresenter.showLoginFragment();

                /*//this needs to be sent up to our presenter, then back down to our Activity
                //     we are going to POP off of the stack
                Intent intent = new Intent(SignUpActivity.this, UserEntryActivity.class);
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
                    /*signup
                    mRef.createUser(email, password, new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
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
                            Snackbar.make(mView, getResources().getString(R.string.signup_eror), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }); */

                }
            }
        });


        return v;
    }

}
