package com.androidtitan.spotscore.main.data;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by amohnacs on 5/2/16.
 */
public class User {

    private static User mInstance;

    private String mUserId;
    private String mEmail;
    private String mUsername;
    private String mName;
    private String mLocation;
    private Bitmap mProfileImage;

    public static User getInstance() {
        if(mInstance == null)
            mInstance = new User();

        return mInstance;
    }

    public User() {

    }

    public User(String email) {

        mEmail = email;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public Bitmap getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(Bitmap mProfileImage) {
        this.mProfileImage = mProfileImage;
    }
}
