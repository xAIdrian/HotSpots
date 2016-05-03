package com.androidtitan.spotscore.main.data;

import android.content.Context;

/**
 * Created by amohnacs on 5/2/16.
 */
public class User {

    private static User mInstance;

    private String mEmail;

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

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
