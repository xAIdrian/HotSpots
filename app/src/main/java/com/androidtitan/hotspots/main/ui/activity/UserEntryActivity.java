package com.androidtitan.hotspots.main.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.base.BaseActivity;
import com.androidtitan.hotspots.main.presenter.UserEntryPresenter;
import com.androidtitan.hotspots.main.ui.fragment.LoginFragment;
import com.androidtitan.hotspots.main.ui.fragment.SignUpFragment;
import com.androidtitan.hotspots.main.ui.view.UserEntryView;

import javax.inject.Inject;

public class UserEntryActivity extends BaseActivity  {

    private FragmentTransaction mFragmentTransaction;
    private LoginFragment mLoginFrag;
    private SignUpFragment mSignUpFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_entry);

        mLoginFrag = new LoginFragment();
        mSignUpFrag = new SignUpFragment();

        //do we need this everytime
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.fragment_container, mLoginFrag)
                .commit();

    }

    public void showLoginFragment() {
        mFragmentTransaction.replace(R.id.fragment_container, mLoginFrag)
                .commit();
    }

    public void showSignUpFragment() {
        mFragmentTransaction.replace(R.id.fragment_container, mSignUpFrag)
                .commit();
    }


}
