package com.androidtitan.hotspots.main.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.App;
import com.androidtitan.hotspots.base.BaseFragment;
import com.androidtitan.hotspots.main.presenter.UserEntryPresenter;
import com.androidtitan.hotspots.main.ui.activity.MainActivity;
import com.androidtitan.hotspots.main.ui.view.UserEntryView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;


public class LoginFragment extends BaseFragment implements UserEntryView {


    @Inject UserEntryPresenter mUserEntryPresenter;

    @Bind(R.id.input_email) EditText emailEditText;
    @Bind(R.id.input_password) EditText passwordEditText;
    @Bind(R.id.loginFab) FloatingActionButton fab;
    @Bind(R.id.signupTextView) TextView signupTextView;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        App.getAppComponent().inject(this);
        Timber.e(String.valueOf(App.getAppComponent() == null));
        if (getArguments() != null) {
            //
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        mUserEntryPresenter.attachView(this);

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserEntryPresenter.showSignUpFragment();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View mView = view;

                //todo: need to account for special chars
                //todo:     we should also consider using a WidgetObservable here
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                email.trim();
                password.trim();

                if(email.isEmpty() || password.isEmpty()) {

                    Snackbar.make(mView, getResources().getString(R.string.missing_field), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    mUserEntryPresenter.authenticateLogin(email, password);
                }
            }
        });

        return v;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        //mUserEntryPresenter.detachView();
    }


    @Override
    public void launchMainActivity() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showFailureSnack(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
