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

            void onProfileInformationFinished();

            void onCredentialsChangeFinished(boolean results);
        }

        void saveProfileImageToFirebase(String base64Image);

        void saveProfileInformationToFirebase(SettingsViewListener listener, String username, String name, String location);

        void changeUserCredentials(SettingsViewListener listener, String email, String existingPassword, String newPassword);
    }

    interface View extends MvpView {

        void setProfileImage(Bitmap bm);

        void showSnackbar(String message);

    }

    interface Presenter {

        void attachView(View mvpView);
        void detachView();
        void takeActivity(SettingsActivity activity);

        void takeDialogInterface(CredentialsFragmentInterface credentialsInterface);

        void storeProfileImageToFirebase(ImageView imageView, Uri uri);

        void storeProfileInformationToFirebase(String username, String name, String location);

        void changeUserCredentials(String email, String existingPassword, String newPassword);
    }
}
