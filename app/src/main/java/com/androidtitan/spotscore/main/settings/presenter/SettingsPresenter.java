package com.androidtitan.spotscore.main.settings.presenter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import com.androidtitan.spotscore.common.BasePresenter;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.settings.SettingsMvp;
import com.androidtitan.spotscore.main.settings.ui.SettingsActivity;
import com.androidtitan.spotscore.utils.BitmapUtils;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by amohnacs on 5/16/16.
 */
public class SettingsPresenter extends BasePresenter<SettingsMvp.View> implements SettingsMvp.Presenter,
    SettingsMvp.Model.SettingsViewListener {

    //@Inject
    SettingsMvp.Model mDataManager;

    private Context mContext;
    private SettingsActivity mActivity;

    private User mUser;

    @Inject
    public SettingsPresenter(Context context, SettingsMvp.Model dataManager) {
        mContext = context;
        this.mDataManager = dataManager;

        mUser = User.getInstance();

    }

    @Override
    public void attachView(SettingsMvp.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        //if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void takeActivity(SettingsActivity activity) {
        mActivity = activity;
    }

    /**
     * Scaled bitmap to match ImageView. Check orientation of image and correct.
     * Delegate saving to firebase to DataManager
     *
     * @param profileImage
     * @param imageUri
     */
    @Override
    public void storeProfileImageToFirebase(ImageView profileImage, Uri imageUri) {


        Bitmap startBitmap = null;
        try {
            startBitmap = Bitmap.createScaledBitmap(
                    MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri),
                    profileImage.getWidth(), profileImage.getHeight(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap rotatedBitmap;

        Uri tempUri = BitmapUtils.getImageUri(mContext, startBitmap);
        String path = BitmapUtils.getRealPathFromURI(mContext, tempUri);

        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = mContext.getContentResolver().query(imageUri, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        rotatedBitmap = Bitmap.createBitmap(startBitmap, 0, 0,
                startBitmap.getWidth(), startBitmap.getHeight(), matrix, true);

        getMvpView().setProfileImage(rotatedBitmap);
        mUser.setProfileImage(rotatedBitmap);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        mDataManager.saveProfileImageToFirebase(base64Image);

    }

    @Override
    public void getStoredProfileImageFromFirebase() {
        mDataManager.getProfileImageFromFirebase(this);
    }

    @Override
    public void onProfileImageFinished(String base64Strings) {

        byte[] imageAsBytes = Base64.decode(base64Strings.getBytes(), Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        getMvpView().setProfileImage(bm);
    }
}
