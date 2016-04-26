package com.androidtitan.spotscore.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.presenter.LoginPresenter;
import com.androidtitan.spotscore.main.ui.fragment.LoginFragment;
import com.androidtitan.spotscore.main.ui.fragment.SignUpFragment;
import com.androidtitan.spotscore.main.ui.view.LoginView;

import javax.inject.Inject;


public class LoginActivity extends AppCompatActivity implements LoginView {

    @Inject
    LoginPresenter mLoginPresenter;

    private FragmentTransaction mFragmentTransaction;
    private LoginFragment mLoginFrag;
    private SignUpFragment mSignUpFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        App.getAppComponent().inject(this);

        mLoginPresenter.attachView(this);

        mLoginFrag = new LoginFragment();
        mSignUpFrag = new SignUpFragment();

        //do we need this everytime
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.fragment_container, mLoginFrag)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }

    public LoginPresenter getPresenter() {
        return mLoginPresenter;
    }

    @Override
    public void showLoginFragment() {
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mLoginFrag)
                .commit();
    }

    @Override
    public void showSignUpFragment() {
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mSignUpFrag)
                .commit();
    }

    @Override
    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showLoginFailureSnack(String message) {
        mLoginFrag.showFailureSnack(message);
    }

    @Override
    public void showSignUpSuccessDialog() {
        mSignUpFrag.showSignUpSuccessDialog();
    }

    @Override
    public void showSignUpFailureSnack(String message) {
        mSignUpFrag.showFailureSnack(message);
    }
}
