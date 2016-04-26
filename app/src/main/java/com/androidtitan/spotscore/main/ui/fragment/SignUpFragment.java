package com.androidtitan.spotscore.main.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.presenter.LoginPresenter;
import com.androidtitan.spotscore.main.ui.activity.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SignUpFragment extends Fragment{

    LoginPresenter mPresenter;

    @Bind(R.id.input_email)
    EditText emailEditText;
    @Bind(R.id.input_password) EditText passwordEditText;
    @Bind(R.id.signupFab)
    FloatingActionButton fab;
    @Bind(R.id.loginTextView)
    TextView loginTextView;

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

        mPresenter = ((LoginActivity)getActivity()).getPresenter();

        if (getArguments() != null) {
            // was something passed to us?
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, v);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showLoginFragment();
            }
        });

        passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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

                final String email = emailEditText.getText().toString(); //todo: need to account for special chars
                String password = passwordEditText.getText().toString();

                email.trim();
                password.trim();

                if(email.isEmpty() || password.isEmpty()) {

                    Snackbar.make(mView, getResources().getString(R.string.missing_field), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    mPresenter.createAuthenticatedUser(email, password);
                }
            }
        });


        return v;
    }

    public void showSignUpSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
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

    public void showFailureSnack(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
