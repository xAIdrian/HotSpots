package com.androidtitan.hotspots.Provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.androidtitan.hotspots.Data.DatabaseHelper;

/**
 * Created by amohnacs on 8/24/15.
 */

public class VenueProvider extends ContentProvider {
    public static final String TAG = "VenueProvider";

    DatabaseHelper databaseHelper;

    public static final String AUTHORITY = "com.androidtitan.hotspots.Provider.VenueProvider";
    public static final String BASE_PATH = DatabaseHelper.TABLE_VENUES;

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

        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    //todo: could we call our DATABASEHELPER methods here instead of calling a rawQuery???
        //yes we can.  Let's do it at some point.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor queryCursor;

        switch(uriMatcher.match(uri)) {
            case GET_ALL:

                queryCursor = databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM "
                        + DatabaseHelper.TABLE_VENUES, null);
                break;

            case GET_ONE:

                queryCursor = databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM "
                        + DatabaseHelper.TABLE_VENUES
                        + " WHERE " + DatabaseHelper.KEY_ID + " = " + uri.getLastPathSegment(), null);
                break;

            case GET_SELECT:

                String starterQuery = "SELECT * FROM " + DatabaseHelper.TABLE_COORDINATES + " WHERE "
                        + DatabaseHelper.KEY_LOCAL_NAME + " = ?";
                queryCursor = databaseHelper.getReadableDatabase().rawQuery(starterQuery,
                        new String[] { uri.getLastPathSegment() });

                if(queryCursor != null)
                    queryCursor.moveToFirst();

                long locationId = queryCursor.getLong(queryCursor.getColumnIndex(DatabaseHelper.KEY_ID));
                String selectionString = "SELECT * FROM " + DatabaseHelper.TABLE_VENUES + " WHERE "
                        + DatabaseHelper.KEY_VENUE_LOCATION_ID + " = ?";
                queryCursor = databaseHelper.getReadableDatabase().rawQuery(selectionString,
                        new String[] { String.valueOf(locationId) });

                break;

            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }

        // Tell the cursor what uri to watch, so it knows when its source data changes

        queryCursor.setNotificationUri(getContext().getContentResolver(), Uri.parse(base_CONTENT_URI));
        return queryCursor;

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
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        long insertId = 0;
        switch(uriType) {
            case GET_ONE:

                insertId = database.insert(DatabaseHelper.TABLE_VENUES, null, values);

                break;

            case GET_ALL:
                break;

            case GET_SELECT:
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        /*Log.e(TAG, "KEY_ID " + uri.getLastPathSegment()
                + " KEY_LOCATION_ID " + values.getAsLong(DatabaseHelper.KEY_VENUE_LOCATION_ID));
                */

        databaseHelper.assignVenueToLocation(Long.valueOf(uri.getLastPathSegment()),
                values.getAsLong(DatabaseHelper.KEY_VENUE_LOCATION_ID));


        getContext().getContentResolver().notifyChange(Uri.parse(base_CONTENT_URI), null);
        return uri;
        //return Uri.parse(DatabaseHelper.TABLE_VENUES + "/" + insertId);

    }

    //todo:::
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int updateId = 0;

        switch(uriType) {
            case GET_ONE:

                updateId = database.update(DatabaseHelper.TABLE_VENUES, values, null, null);

                break;

            case GET_ALL:
                break;

            case GET_SELECT:
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(Uri.parse(base_CONTENT_URI), null);
        return updateId;
    }

}
