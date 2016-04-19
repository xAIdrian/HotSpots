package com.androidtitan.hotspots.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.input_email)
    EditText emailEditText;
    @Bind(R.id.input_password)
    EditText passwordEditText;
    @Bind(R.id.signupFab)
    FloatingActionButton fab;
    @Bind(R.id.loginTextView)
    TextView loginTextView;

    //todo: Dagger 2
    final Firebase mRef = new Firebase(Constants.FIREBASE_URL);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);


        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
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
                    //signup
                    mRef.createUser(email, password, new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            //todo: let's replace this with something cooler later
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setMessage(R.string.signup_success)
                                    .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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
                    });

                }
            }
        });
    }

}
