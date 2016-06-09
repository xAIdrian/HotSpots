package com.androidtitan.spotscore.main.login.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.login.LoginMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity implements LoginMvp.View {

    @Inject
    LoginMvp.Presenter mPresenter;

    @Bind(R.id.loginCard) CardView mLoginCard;
    @Bind(R.id.input_email) EditText mEmailEditText;
    @Bind(R.id.input_password) EditText mPasswordEditText;
    @Bind(R.id.goTextView) TextView mGoText;
    @Bind(R.id.forgotPasswordTextView) TextView mForgotPwText;


    /*todo: we are going to replace with logic with an incorrect login
    @Bind(R.id.mSignupTextView) TextView mSignupTextView;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        mPresenter.attachView(this);

        mGoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View mView = v;

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

        mPasswordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mGoText.callOnClick();
                    return true;
                }
                return false;
            } });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter = null;
    }

    @Override
    public void showLoginFragment() {

    }

    @Override
    public void showSignUpFragment() {

    }

    @Override
    public void launchScoreActivity() {

        //mPresenter.setupUser();

        Intent intent = new Intent(this, ScoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showLoginFailureSnack(String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSignUpSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.signup_success)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mPresenter.showLoginFragment();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showSignUpFailureSnack(String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
    }
}
