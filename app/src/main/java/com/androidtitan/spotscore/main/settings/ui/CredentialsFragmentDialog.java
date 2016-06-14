package com.androidtitan.spotscore.main.settings.ui;


import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.App;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.settings.CredentialsFragmentInterface;
import com.androidtitan.spotscore.main.settings.presenter.SettingsPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CredentialsFragmentDialog extends DialogFragment implements CredentialsFragmentInterface {
    private final String TAG = getClass().getSimpleName();

    @Inject
    SettingsPresenter mPresenter;

    private User mUser;

    @Bind(R.id.emailInput)
    EditText mEmailEdit;
    @Bind(R.id.existingpwInput)
    EditText mExistingEdit;
    @Bind(R.id.pwOneInput)
    EditText mNewPasswordEdit;
    @Bind(R.id.pwTwoInput)
    EditText mReenterPasswordEdit;

    @Bind(R.id.cancelTextView)
    TextView mCancelText;
    @Bind(R.id.saveTextView)
    TextView mSaveText;

    public CredentialsFragmentDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);

        mUser = User.getInstance();
        mPresenter.takeDialogInterface(this);

        if (getArguments() != null) {
            //what do we need to receive from our Activity
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Change Login Info");
        View v = inflater.inflate(R.layout.fragment_dialog_password_change, container, false);
        ButterKnife.bind(this, v);

        mEmailEdit.setText(mUser.getEmail(), TextView.BufferType.EDITABLE);

        mSaveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmailEdit.getText().toString().isEmpty() || mExistingEdit.getText().toString().isEmpty()) {

                    Snackbar.make(getView(), R.string.missing_field, Snackbar.LENGTH_LONG).show();

                } else if (!mNewPasswordEdit.getText().toString().equals(mReenterPasswordEdit.getText().toString())) {

                    Snackbar.make(getView(), R.string.passwords_no_match, Snackbar.LENGTH_LONG).show();

                } else {

                    mPresenter.changeUserCredentials(mEmailEdit.getText().toString(),
                            mExistingEdit.getText().toString(), mReenterPasswordEdit.getText().toString());


                }

            }
        });

        mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        mReenterPasswordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mSaveText.callOnClick();
                return true;
            }
        });

        return v;
    }

    @Override
    public void showSnackbar(String message) {
        //Log.e(TAG, message);
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
