package com.androidtitan.spotscore.common.data;

import com.androidtitan.spotscore.BuildConfig;

/**
 * Created by amohnacs on 5/4/16.
 */
public class Constants {

    public static String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static String FOURSQUARE_SEARCH_URL = "https://api.foursquare.com/v2/";
    public static String[] FIREBASE_EXCLUSIONS = new String[] {".", "$", "#", "[", "]", "/"};

    public static String FOURSQUARE_CLIENT_ID = BuildConfig.UNIQUE_FOURSQUARE_CLIENT_ID;
    public static String FOURSQUARE_CLIENT_SECRET = BuildConfig.UNIQUE_FOURSQUARE_CLIENT_SECRET;

    public static String DOORBELLIO_API_KEY = BuildConfig.UNIQUE_DOORBELLIO_API_KEY;
}
