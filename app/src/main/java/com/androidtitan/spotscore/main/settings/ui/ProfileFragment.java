package com.androidtitan.spotscore.main.settings.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
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
import com.androidtitan.spotscore.utils.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private int PICK_IMAGE_REQUEST = 1;

    @Bind(R.id.profileCircleImageView) ImageView mProfileImage;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);


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

                 InputStream inputStream = null;
                 ContentResolver contentResolver = getActivity().getContentResolver();
                 inputStream = contentResolver.openInputStream(uri);
                 BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                 Bitmap region = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
                 int i = 0;


                 //todo:mPresenter.saveImageToFireBase(Bitmap bitmap)
                 //     todo: have our NavDrawer image call set our Firebase image

                 mProfileImage.setImageBitmap(imageBitmap);

             } catch (Exception e) {
                e.printStackTrace();
             }
         }
        }
}
