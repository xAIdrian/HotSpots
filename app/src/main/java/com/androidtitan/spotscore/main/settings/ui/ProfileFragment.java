package com.androidtitan.spotscore.main.settings.ui;

import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.settings.SettingsMvp;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private int PICK_IMAGE_REQUEST = 1;

    private SettingsMvp.Presenter mPresenter;

    private User mUser;

    @Bind(R.id.profileCircleImageView) ImageView mProfileImage;
    @Bind(R.id.usernameInput)
    EditText mUserNameEdit;
    @Bind(R.id.nameInput)
    EditText mNameEdit;
    @Bind(R.id.locationInput)
    EditText mLocationEdit;

    @Bind(R.id.changePasswordTextView)
    TextView mChangePasswordText;
    @Bind(R.id.saveTextView)
    TextView mSaveText;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = ((SettingsActivity)getActivity()).getPresenter();
        mUser = User.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);

        mProfileImage.setImageBitmap(mUser.getProfileImage());
        mUserNameEdit.setText(mUser.getUsername(), TextView.BufferType.EDITABLE);
        mNameEdit.setText(mUser.getName(), TextView.BufferType.EDITABLE);
        mLocationEdit.setText(mUser.getLocation(), TextView.BufferType.EDITABLE);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imageIntent, "Select an Image"), PICK_IMAGE_REQUEST);
            }
        });

        mLocationEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mSaveText.callOnClick();

                }
                return false;
            }
        });

        mChangePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CredentialsFragmentDialog credFrag = new CredentialsFragmentDialog();
                credFrag.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                credFrag.show(getFragmentManager(), "CredentialsFragmentDialog");
            }
        });

        mSaveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mUserNameEdit.getText().toString().isEmpty() || mNameEdit.getText().toString().isEmpty()
                        || mLocationEdit.getText().toString().isEmpty()) {

                    Snackbar.make(getView(), R.string.missing_field, Snackbar.LENGTH_LONG).show();

                } else {

                    mPresenter.storeProfileInformationToFirebase(mUserNameEdit.getText().toString(),
                            mNameEdit.getText().toString(), mLocationEdit.getText().toString());
                }
            }
        });

        return v;
    }

    /**
     * This result is meant to receive the Image that the user has selected form the chooser
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                 && data != null && data.getData() != null) {

             Uri imageUri = data.getData();
             mPresenter.storeProfileImageToFirebase(mProfileImage, imageUri);

         }
    }

    public void setProfileImage(Bitmap bm) {
        mProfileImage.setImageBitmap(bm);
    }
}

/*This is used to load the bitmap in Regions.  Used for large images.

                 InputStream inputStream = null;
                 ContentResolver contentResolver = getActivity().getContentResolver();
                 inputStream = contentResolver.openInputStream(uri);
                 BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                 Bitmap region = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);*/
