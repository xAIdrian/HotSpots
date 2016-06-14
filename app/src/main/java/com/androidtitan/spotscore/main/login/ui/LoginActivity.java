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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
    private final String TAG = getClass().getSimpleName();

    private static final int ANIM_DURATION = 300;

    @Inject
    LoginMvp.Presenter mPresenter;

    @Bind(R.id.loginCard) CardView mLoginCard;
    @Bind(R.id.input_email) EditText mEmailEditText;
    @Bind(R.id.input_password) EditText mPasswordEditText;
    @Bind(R.id.goTextView) TextView mGoText;
    @Bind(R.id.forgotPasswordTextView) TextView mForgotPwText;

    @Bind(R.id.forgotPasswordCard) CardView mForgotPasswordCard;
    @Bind(R.id.input_email_pw) EditText mForgottenEmailEditText;
    @Bind(R.id.sendForgottenTextView) TextView mForgottenSendText;
    @Bind(R.id.cancelForgotPasswordTextView) TextView mForgottenCancelText;

    @Bind(R.id.signupCard) CardView mSignupCard;
    @Bind(R.id.signup_input_email) EditText mSignupEmailEditText;
    @Bind(R.id.signup_input_password) EditText mSignupPasswordEditText;
    @Bind(R.id.submitSignupTextView) TextView mSignupSendText;
    @Bind(R.id.cancelSignupTextView) TextView mCancelSignupText;

    @Bind(R.id.signupTextView) TextView mSignupText;


    Animation cardEntryAnim;
    Animation cardExitAnim;
    Animation cardHideAnim;
    Animation cardAppearAnim;


    /*todo: SET UP A SIGN-IN BUTTON THAT IS OUTSIDE OF THE ORIGINAL CARD
    @Bind(R.id.mSignupTextView) TextView mSignupTextView;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this);
        mPresenter.attachView(this);

        cardEntryAnim = AnimationUtils.loadAnimation(this, R.anim.login_card_entry);
        cardExitAnim = AnimationUtils.loadAnimation(this, R.anim.login_card_exit);
        cardHideAnim = AnimationUtils.loadAnimation(this, R.anim.login_card_hide);
        cardHideAnim.setFillAfter(true);
        cardAppearAnim = AnimationUtils.loadAnimation(this, R.anim.login_card_appear);
        cardAppearAnim.setFillAfter(true);


        mGoText.setOnClickListener(v -> {
                final View mView = v;

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

        mForgotPwText.setOnClickListener(v -> {

            signupTextVisibility(true);

            mForgotPasswordCard.setVisibility(View.VISIBLE);
            mForgotPasswordCard.startAnimation(cardEntryAnim);
            mLoginCard.startAnimation(cardHideAnim);
        });

        //these are for when the user forgets their password

        mForgottenCancelText.setOnClickListener(v -> {

            hideForgotPwCard();

        });

        mForgottenSendText.setOnClickListener(v -> {
            final String email = mForgottenEmailEditText.getText().toString();

            email.trim();

            if(email.isEmpty()) {

                Snackbar.make(v, getResources().getString(R.string.missing_email), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            } else {

               mPresenter.resetPassword(email);
            }
        });

        mForgottenEmailEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mForgottenSendText.callOnClick();
                    return true;
                }
                return false;
            } });

        //this is our signup card

        mSignupSendText.setOnClickListener(v -> {

            final String email = mSignupEmailEditText.getText().toString(); //todo: need to account for special chars
            String password = mSignupPasswordEditText.getText().toString();

            email.trim();
            password.trim();

            if(email.isEmpty() || password.isEmpty()) {

                Snackbar.make(v, getResources().getString(R.string.missing_field), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            } else {

                mPresenter.createAuthenticatedUser(email, password);
            }
        });


        mCancelSignupText.setOnClickListener(v -> {

            hideSignupCard();

        });

        //errything else

        mSignupText.setOnClickListener(v -> {

            signupTextVisibility(true);

            mSignupCard.setVisibility(View.VISIBLE);
            mSignupCard.startAnimation(cardEntryAnim);
            mLoginCard.startAnimation(cardHideAnim);

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter = null;
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

                        dialog.dismiss();
                        hideSignupCard();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showSignUpFailureSnack(String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPasswordResetSnack(String message) {
        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
        hideForgotPwCard();
    }

    private void hideForgotPwCard() {
        mForgotPasswordCard.startAnimation(cardExitAnim);
        cardExitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mForgotPasswordCard.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mLoginCard.startAnimation(cardAppearAnim);
        signupTextVisibility(false);
    }

    private void hideSignupCard() {
        mSignupCard.startAnimation(cardExitAnim);
        cardExitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSignupCard.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLoginCard.startAnimation(cardAppearAnim);
        signupTextVisibility(false);
    }

    private void signupTextVisibility(boolean removeVisibility) {
        if(removeVisibility) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(ANIM_DURATION);
            mSignupText.startAnimation(alphaAnimation);
            mSignupText.setVisibility(View.INVISIBLE);
        } else {
            mSignupText.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(ANIM_DURATION);
            mSignupText.startAnimation(alphaAnimation);
        }
    }

    @Override
    public void onBackPressed() {

        if(mForgotPasswordCard.getVisibility() == View.VISIBLE) {
            hideForgotPwCard();

        } else if(mSignupCard.getVisibility() == View.VISIBLE) {
            hideSignupCard();

        } else {
            finish();
        }
    }
}
