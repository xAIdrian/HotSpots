package com.androidtitan.spotscore.main.settings.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imageIntent, "Select an Image"), PICK_IMAGE_REQUEST);
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

         if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

             Uri uri = data.getData();
             try {

                 Bitmap imageBitmap = Bitmap.createScaledBitmap(
                         MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri),
                         mProfileImage.getWidth(), mProfileImage.getHeight(), false);

                 /*This is used to load the bitmap in Regions.  Used for large images.

                 InputStream inputStream = null;
                 ContentResolver contentResolver = getActivity().getContentResolver();
                 inputStream = contentResolver.openInputStream(uri);
                 BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                 Bitmap region = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);*/

                 mProfileImage.setImageBitmap(imageBitmap);
                 Log.e(TAG, String.valueOf(mPresenter == null));
                 mPresenter.storeProfileImageToFirebase(imageBitmap);

             } catch (Exception e) {
                e.printStackTrace();
             }
         }
        }

    public void setProfileImage(Bitmap bm) {
        mProfileImage.setImageBitmap(bm);
    }
}
