package com.androidtitan.hotspots.Provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.androidtitan.hotspots.Data.DatabaseHelper;

/**
 * Created by amohnacs on 8/24/15.
 */

public class VenueProvider extends ContentProvider {
    public static final String TAG = "VenueProvider";

    DatabaseHelper databaseHelper;
    String locationId;

    public static final String AUTHORITY = "com.androidtitan.hotspots.Provider.VenueProvider";
    public static final String BASE_PATH = DatabaseHelper.TABLE_VENUES;

    private String uriMatcherInput = "";

    public static String base_CONTENT_URI = "content://" + AUTHORITY
            + "/" + BASE_PATH + "/";



    //MIME Types for getting a single item or a list of them

    public static final String VENUES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.com.androidtitan.Data.venues";
    public static final String VENUE_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.com.androidtitan.Data.venues";

    //Column names
    public static class InterfaceConstants {
        public static final String id = "_id";
        public static final String venue_name = "venue_name";
        public static final String venue_city = "venue_city";
        public static final String venue_category = "venue_category";
        public static final String venue_id_string = "venue_string";
        public static final String venue_rating = "venue_rating";
    }

    //URI matcher variable
    private static final int GET_ALL = 0;
    private static final int GET_ONE = 1;
    private static final int GET_SELECT = 2;

    static UriMatcher uriMatcher = BuildUriMatcher();

    static UriMatcher BuildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Uri to match and the code to return when matched

        matcher.addURI(AUTHORITY, BASE_PATH, GET_ALL);
        matcher.addURI(AUTHORITY, BASE_PATH + "/#", GET_ONE);
        matcher.addURI(AUTHORITY, BASE_PATH + "/*", GET_SELECT);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        databaseHelper = DatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case GET_ALL:

                return databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_VENUES, null);

            case GET_ONE:

                return databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_VENUES
                    + " WHERE " + DatabaseHelper.KEY_ID + " = " + uri.getLastPathSegment(), null);


            case GET_SELECT:

                String selectionQuery = "SELECT * FROM " + DatabaseHelper.TABLE_VENUES + " td, "
                        + DatabaseHelper.TABLE_COORDINATES + " tg, " + DatabaseHelper.TABLE_COORDINATES_VENUES + " tt WHERE tg."
                        + DatabaseHelper.KEY_LOCAL + " = ? AND tg." + DatabaseHelper.KEY_ID
                        + " = " + "tt." + DatabaseHelper.KEY_COORDS_ID + " AND td." + DatabaseHelper.KEY_ID + " = "
                        + "tt." + DatabaseHelper.KEY_VENUES_ID;

                Log.e(TAG, "selectionQuery: " + selectionQuery);
                return databaseHelper.getReadableDatabase().rawQuery(selectionQuery,
                        new String[] { String.valueOf(uri.getLastPathSegment()) });

            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);

        }
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {

            case GET_ALL:
                return VENUES_MIME_TYPE;
            case GET_ONE:
                return VENUE_MIME_TYPE;
            case GET_SELECT:
                return VENUES_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

//todo: we may need to implement these at some point
    //http://developer.xamarin.com/guides/android/platform_features/intro_to_content_providers/part_3_-_creating_a_custom_contentprovider/

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        long id = 0;
        switch(uriMatcher.match(uri)) {
            case GET_ONE:

                //id = database.insert(DatabaseHelper.TABLE_VENUES, null, values);
                break;

            case GET_ALL:
                break;

            case GET_SELECT:
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DatabaseHelper.TABLE_VENUES + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri, null);
        return Integer.valueOf(uri.getLastPathSegment());
    }

    /*
    As of API level 11 there is a method ContentResolver.call(Uri, String, String, Bundle) which provides extra flexibility.
     */
}
