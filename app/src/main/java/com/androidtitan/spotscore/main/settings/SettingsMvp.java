package com.androidtitan.spotscore.main.settings;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.androidtitan.spotscore.common.MvpView;
import com.androidtitan.spotscore.main.settings.ui.SettingsActivity;

/**
 * Created by amohnacs on 5/16/16.
 */
public interface SettingsMvp {

    interface Model {
        interface SettingsViewListener {
            void onProfileImageFinished(String base64Strings);
        }

        void saveProfileImageToFirebase(String base64Image);
        void getProfileImageFromFirebase(SettingsViewListener listener);
    }

    interface View extends MvpView {

        void setProfileImage(Bitmap bm);

    }

    interface Presenter {

        void attachView(View mvpView);
        void detachView();
        void takeActivity(SettingsActivity activity);

        void storeProfileImageToFirebase(ImageView imageView, Uri uri);
        void getStoredProfileImageFromFirebase();


    }
}
