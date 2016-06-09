package com.androidtitan.spotscore.main.login.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.BaseFragment;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.login.LoginMvp;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginFragment extends BaseFragment {


    private LoginMvp.Presenter mPresenter;

    @Bind(R.id.input_email) EditText mEmailEditText;
    @Bind(R.id.input_password) EditText mPasswordEditText;
    @Bind(R.id.loginFab) FloatingActionButton fab;
    @Bind(R.id.signupTextView) TextView mSignupTextView;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent();



        if (getArguments() != null) {
            //
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);

        mSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showSignUpFragment();
            }
        });

        mPasswordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    fab.callOnClick();
                    return true;
                }
                return false;
            } });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View mView = view;

                //todo: need to account for special chars
                //todo:     we should also consider using a WidgetObservable here
                final String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                email.trim();
                password.trim();

                if(email.isEmpty() || password.isEmpty()) {

                    Snackbar.make(mView, getResources().getString(R.string.missing_field), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    mPresenter.authenticateLogin(email, password);
                }
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void showFailureSnack(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}

