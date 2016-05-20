package com.androidtitan.spotscore.main.settings.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.androidtitan.spotscore.common.BasePresenter;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.ui.ScoreActivity;
import com.androidtitan.spotscore.main.settings.SettingsMvp;
import com.androidtitan.spotscore.main.settings.ui.SettingsActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;

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

    @Override
    public void storeProfileImageToFirebase(Bitmap bm) {

        mUser.setProfileImage(bm);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
