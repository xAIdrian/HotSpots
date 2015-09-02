package com.androidtitan.hotspots.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A. Mohnacs on 5/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";

    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "troopTrackerDatabase";
    private static final int DATABASE_VERSION = 1;

    //tables
    public static final String TABLE_COORDINATES = "coordinates";
    private static final String TABLE_STARTER_COORDS = "randocoordinates";
    public static final String TABLE_VENUES = "venues";
    public static final String TABLE_COORDINATES_VENUES = "coordinates_venues";

    //Shared columns
    public static final String KEY_ID = "_id";

    //coordinates table
    public static final String KEY_LOCAL = "local";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LOCKED = "locationLocked";
    public static final String KEY_VISITED = "visistedMap";

    //random coordinates table
    private static final String KEY_STARTER_LOCAL = "randolocal";
    private static final String KEY_STARTER_LATITUDE = "randolatitude";
    private static final String KEY_STARTER_LONGITUDE = "randolongitude";

   //venues table
    public static final String KEY_VENUE_NAME = "venue_name";
    public static final String KEY_VENUE_CITY = "venue_city";
    public static final String KEY_VENUE_CATEGORY = "venue_category";
    public static final String KEY_VENUE_STRING = "venue_string";
    public static final String KEY_VENUE_RATING = "venue_rating";
    public static final String KEY_VENUE_LOCATION_ID = "assignedLocation";

    //coordinates_venues table
    public static final String KEY_COORDS_ID = "coords_id";
    public static final String KEY_VENUES_ID = "venue_id";


    // Table Creation Statements

    //Coordinates Table
    private static final String CREATE_TABLE_COORDINATES = "CREATE TABLE " + TABLE_COORDINATES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_LOCAL + " TEXT,"
            + KEY_LATITUDE + " DOUBLE PRECISION,"
            + KEY_LONGITUDE + " DOUBLE PRECISION, "
            + KEY_LOCKED + " BIT, "
            + KEY_VISITED + " BIT" + ")";

    //Random Coordinates Table
    private static final String CREATE_TABLE_STARTER_COORDS = "CREATE TABLE " + TABLE_STARTER_COORDS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_STARTER_LOCAL + " TEXT,"
            + KEY_STARTER_LATITUDE + " DOUBLE PRECISION,"
            + KEY_STARTER_LONGITUDE + " DOUBLE PRECISION" + ")";

    //Venues Table
    private static final String CREATE_TABLE_VENUES = "CREATE TABLE " + TABLE_VENUES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_VENUE_NAME + " TEXT,"
            + KEY_VENUE_CITY + " TEXT,"
            + KEY_VENUE_CATEGORY + " TEXT, "
            + KEY_VENUE_STRING + " TEXT,"
            + KEY_VENUE_RATING + " REAL, "
            + KEY_VENUE_LOCATION_ID + " INTEGER" + ")";


    //Venue-Location Table
    private static final String CREATE_TABLE_COORDINATES_VENUES = "CREATE TABLE " + TABLE_COORDINATES_VENUES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COORDS_ID + " INTEGER, "
            + KEY_VENUES_ID + " INTEGER" + ")";


    public static synchronized DatabaseHelper getInstance(Context context) {

        //Singleton Pattern. Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating the required tables

        db.execSQL(CREATE_TABLE_COORDINATES);
        db.execSQL(CREATE_TABLE_STARTER_COORDS);
        db.execSQL(CREATE_TABLE_VENUES);
        db.execSQL(CREATE_TABLE_COORDINATES_VENUES);

        //maybe we can insert a sample user

        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (1, 'Seattle, Washington', 47.6062095, -122.3320708)");
        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (2, 'Miami, Florida', 25.761680, -80.191790)");
        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (3, 'Washington, DC', 38.8951, -77.0367)");
        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (4, 'Tokyo, Japan', 35.6894875, 139.69170639999993)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STARTER_COORDS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VENUES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COORDINATES_VENUES);

        // create new tables
        onCreate(db);
    }

    //todo: COORDINATES TABLE
    //I did NOT include update or delete...


    public long createLocation(LocationBundle locBun) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCAL, locBun.getLocalName());
        try {
            values.put(KEY_LATITUDE, locBun.getLatlng().latitude);
            values.put(KEY_LONGITUDE, locBun.getLatlng().longitude);
            values.put(KEY_LOCKED, locBun.getIsLocationLockedDatabase());
            values.put(KEY_VISITED, locBun.getVisitedMapDatabase());
        } catch(NullPointerException e) {
            Log.e("createLocation", "location created without coordinates!");
        }

        long coordinate_id = database.insert(TABLE_COORDINATES, null, values);
        locBun.setId(coordinate_id);

        return coordinate_id;
    }

    public List<LocationBundle> getAllLocations() {
        List<LocationBundle> geoPoints = new ArrayList<LocationBundle>();
        String selectQuery = "SELECT * FROM " + TABLE_COORDINATES;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocationBundle locBun = new LocationBundle();
                locBun.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                locBun.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
                locBun.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));
                locBun.setIsLocationLockedDatabase(cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));
                locBun.setVisitedMapDatabase(cursor.getInt(cursor.getColumnIndex(KEY_VISITED)));

                geoPoints.add(locBun);
            } while (cursor.moveToNext());
        }
        return geoPoints;
    }

    public LocationBundle getLocationBundle(long coord_id) {
        //coord_id = coord_id + 1;  IDK if we will actually need this as this method might not even be used

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_COORDINATES
                + " WHERE " + KEY_ID + " = " + coord_id;
        Log.e("DBHgetCoordinate", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        LocationBundle locBundle = new LocationBundle();
        locBundle.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        locBundle.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
        locBundle.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));
        locBundle.setIsLocationLockedDatabase(cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));
        locBundle.setVisitedMapDatabase(cursor.getInt(cursor.getColumnIndex(KEY_VISITED)));


        return locBundle;
    }


    //Following function will update Soldier values only, not Division
    public int updateLocationBundle(LocationBundle locBun) {
        SQLiteDatabase database = this.getWritableDatabase();
        //prepping/formatting data for update(replace) row
        ContentValues values = new ContentValues();
        values.put(KEY_LOCAL, locBun.getLocalName());
        values.put(KEY_LATITUDE, locBun.getLatlng().latitude);
        values.put(KEY_LONGITUDE, locBun.getLatlng().longitude);
        values.put(KEY_LOCKED, locBun.getIsLocationLockedDatabase());
        values.put(KEY_VISITED, locBun.getVisitedMapDatabase());
        //updating
        Log.i("DBHupdateSoldier", "Updated!" + TABLE_COORDINATES + " " + KEY_ID + " = " + String.valueOf(locBun.getId()));

        return database.update(TABLE_COORDINATES, values,
                KEY_ID + " = ?", new String[]{String.valueOf(locBun.getId())});

    }

    public void deleteLocation(LocationBundle locBun) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.i("DBHdeleteLocation", "Deleted! " + TABLE_COORDINATES + ": " + KEY_ID + " = " + locBun.getId());

        //delete all of it's associated Venues.
            //add a boolean parameter to make this optional later???
        List<Venue> ourLocationsVenues = getAllVenuesFromLocation(locBun);
        for(Venue venue : ourLocationsVenues) {
            deleteVenue(venue.getId());
        }

        database.delete(TABLE_COORDINATES,
                KEY_ID + " =?", new String[]{ String.valueOf(locBun.getId()) });

        /*
        Log.i("insideDeleteDivision", TABLE_DIVISION + ": " + KEY_ID + " = ? " + division.getId());
        database.delete(TABLE_DIVISION, KEY_ID + " = ?",
                new String[]{String.valueOf(division.getId())});
         */

    }

    public void printCoordinatesTable() {
        String selectQuery = "SELECT * FROM " + TABLE_COORDINATES;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //looping through all the rows to create objects to add to our list
        if (cursor.moveToFirst()) {
            do {
                LocationBundle locationBundle = new LocationBundle();
                locationBundle.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                locationBundle.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
                locationBundle.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));
                locationBundle.setIsLocationLockedDatabase(cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));
                locationBundle.setVisitedMapDatabase(cursor.getInt(cursor.getColumnIndex(KEY_VISITED)));
                //logging
                Log.i("DBHprintAllCoordinates", cursor.getLong(cursor.getColumnIndex(KEY_ID)) + " "
                        + cursor.getString(cursor.getColumnIndex(KEY_LOCAL)) + ": "
                        + cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)) + ", "
                        + cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));

            } while (cursor.moveToNext());
        }
    }

    //todo: STARTER COORDS TABLE

    public LocationBundle getStarterLocationBundle(long coord_id) {
        //coord_id = coord_id + 1;  IDK if we will actually need this as this method might not even be used

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_STARTER_COORDS
                + " WHERE " + KEY_ID + " = " + coord_id;
        Log.i("DBHgetCoordinate", selectQuery);

        Cursor cursor = database.query(TABLE_STARTER_COORDS, new String[]{KEY_ID, KEY_STARTER_LOCAL,
                        KEY_STARTER_LATITUDE, KEY_STARTER_LONGITUDE},
                KEY_ID + " =?", new String[]{String.valueOf(coord_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        LocationBundle locBundle = new LocationBundle();
        locBundle.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        locBundle.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_STARTER_LOCAL)));
        locBundle.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_STARTER_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KEY_STARTER_LONGITUDE))));

        return locBundle;
    }


    //TODO:     VENUES table

    //venue added to the database and the location that it is going to be assigned to at creation time
    public long createVenue(Venue venue, long locationBundleId) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        //try/catch?
        values.put(KEY_VENUE_NAME, venue.getName());
        values.put(KEY_VENUE_CITY, venue.getCity());
        values.put(KEY_VENUE_CATEGORY, venue.getCategory());
        values.put(KEY_VENUE_STRING, venue.getVenueIdString());
        values.put(KEY_VENUE_RATING, venue.getRating());
        values.put(KEY_VENUE_LOCATION_ID, venue.getLocation_id());

        //insert row
        long venue_id = database.insert(TABLE_VENUES, null, values);
        venue.setId(venue_id);

        //assign venue to LocationBundle
        assignVenueToLocation(venue.getId(), locationBundleId);
        Log.e(TAG, "createVenue: " + venue.getName());

        return venue_id;
    }

    //our one little TABLE_COORDS_VENUES method
    //keep an eye on this bad boy
    public long assignVenueToLocation(long venue_id, long location_id) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_COORDS_ID, location_id);
        values.put(KEY_VENUES_ID, venue_id);

        long coords_venues_id = database.insert(TABLE_COORDINATES_VENUES, null, values);

        return coords_venues_id;
    }


    public Venue getVenue(long venue_id) {
        SQLiteDatabase database = getReadableDatabase();

        String selectionQuery = "SELECT * FROM " + TABLE_VENUES + " WHERE " + KEY_ID + " = " + venue_id;
        Log.e(TAG, selectionQuery);

        Cursor cursor = database.rawQuery(selectionQuery, null);

        if(cursor != null)
            cursor.moveToFirst();

        Venue v = new Venue();
        v.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        v.setName(cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)));
        v.setCity(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY)));
        v.setCategory(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY)));
        v.setVenueIdString(cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)));
        v.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));
        v.setLocation_id(cursor.getInt(cursor.getColumnIndex(KEY_VENUE_LOCATION_ID)));

        return v;
    }

    public List<Venue> getAllVenues() {
        SQLiteDatabase database = getReadableDatabase();

        List<Venue> allVenues = new ArrayList<Venue>();

        String selectionQuery = "SELECT * FROM " + TABLE_VENUES;
        Log.e(TAG, selectionQuery);

        Cursor cursor = database.rawQuery(selectionQuery, null);

        //looping through all of the rows and adding them to our list
        if(cursor.moveToFirst()) {
            do {
                Venue v = new Venue();
                v.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                v.setName(cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)));
                v.setCity(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY)));
                v.setCategory(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY)));
                v.setVenueIdString(cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)));
                v.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));
                v.setLocation_id(cursor.getInt(cursor.getColumnIndex(KEY_VENUE_LOCATION_ID)));

                allVenues.add(v);

            } while (cursor.moveToNext()); //so long as the cursor is not at the end keep adding Venues
        }

        return allVenues;

    }

    public List<Venue> getAllVenuesFromLocation(LocationBundle locationBundle) {
        SQLiteDatabase database = getReadableDatabase();

        String localName = locationBundle.getLocalName();
        List<Venue> venuesByLoc = new ArrayList<Venue>();

        String selectionQuery = "SELECT * FROM " + TABLE_VENUES + " td, "
                + TABLE_COORDINATES + " tg, " + TABLE_COORDINATES_VENUES + " tt WHERE tg."
                + KEY_LOCAL + " = '" + localName + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_COORDS_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_VENUES_ID;
        //select * from venues tv, coordinates tc, coordinates_venues tt where tc._id = location_id
            //  and tc._id = tt.coords_id and tv._id = tt.venues_id
        Log.e(TAG, selectionQuery);

        Cursor cursor = database.rawQuery(selectionQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Venue v = new Venue();
                v.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                v.setName(cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)));
                v.setCity(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY)));
                v.setCategory(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY)));
                v.setVenueIdString(cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)));
                v.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));
                v.setLocation_id(cursor.getInt(cursor.getColumnIndex(KEY_VENUE_LOCATION_ID)));

                venuesByLoc.add(v);

            } while (cursor.moveToNext()); //so long as the cursor is not at the end keep adding Venues
        }

        return venuesByLoc;

    }

    public int updateVenue(Venue venue) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_VENUE_NAME, venue.getName());
        values.put(KEY_VENUE_CITY, venue.getCity());
        values.put(KEY_VENUE_CATEGORY, venue.getCategory());
        values.put(KEY_VENUE_STRING, venue.getVenueIdString());
        values.put(KEY_VENUE_RATING, venue.getRating());
        values.put(KEY_VENUE_LOCATION_ID, venue.getLocation_id());

        //updating row
        return database.update(TABLE_VENUES, values, KEY_ID + " = ?", new String[] { String.valueOf(venue.getId()) });
    }

    public void deleteVenue(long venue_id) {
        SQLiteDatabase database = getWritableDatabase();

        database.delete(TABLE_VENUES, KEY_ID + " = ?", new String[] { String.valueOf(venue_id) });
    }

    //takes a parameter that can be used to get a set of venues by location.
    //to get all input a negative number
    public void printVenuesByLocation(LocationBundle locationBundle) {
        SQLiteDatabase database = getReadableDatabase();

        String localName = locationBundle.getLocalName();

        String selectionQuery = "SELECT * FROM " + TABLE_VENUES + " td, "
                + TABLE_COORDINATES + " tg, " + TABLE_COORDINATES_VENUES + " tt WHERE tg."
                + KEY_LOCAL + " = '" + localName + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_COORDS_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_VENUES_ID;

        Log.e(TAG, selectionQuery);

        Cursor cursor = database.rawQuery(selectionQuery, null);

//        Log.e(TAG, String.valueOf(cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)).isEmpty()));

        if(cursor.moveToFirst()) {
            do {
                Venue v = new Venue();
                v.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                v.setName(cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)));
                v.setCity(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY)));
                v.setCategory(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY)));
                v.setVenueIdString(cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)));
                v.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));
                v.setLocation_id(cursor.getInt(cursor.getColumnIndex(KEY_VENUE_LOCATION_ID)));

                Log.e("DBHprintVenuesLocation", cursor.getInt(cursor.getColumnIndex(KEY_ID)) +
                        " name: " + cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)) + ", city: " +
                    cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY))+ ", cat: " +
                    cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY))+ ", venueID: " +
                    cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)) + ", rating: " +
                    cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));

            } while (cursor.moveToNext()); //so long as the cursor is not at the end keep adding Venues
        }
    }

    public void printVenuesTable() {
        SQLiteDatabase database = getReadableDatabase();
        String selectionQuery;
        selectionQuery = "SELECT * FROM " + TABLE_VENUES;
        Cursor cursor = database.rawQuery(selectionQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Venue v = new Venue();
                v.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                v.setName(cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)));
                v.setCity(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY)));
                v.setCategory(cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY)));
                v.setVenueIdString(cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)));
                v.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));
                v.setLocation_id(cursor.getInt(cursor.getColumnIndex(KEY_VENUE_LOCATION_ID)));

                Log.e("DBHprintALLvenues", cursor.getInt(cursor.getColumnIndex(KEY_ID)) +
                        " name: " + cursor.getString(cursor.getColumnIndex(KEY_VENUE_NAME)) + ", city: " +
                        cursor.getString(cursor.getColumnIndex(KEY_VENUE_CITY)) + ", cat: " +
                        cursor.getString(cursor.getColumnIndex(KEY_VENUE_CATEGORY)) + ", venueID: " +
                        cursor.getString(cursor.getColumnIndex(KEY_VENUE_STRING)) + ", rating: " +
                        cursor.getFloat(cursor.getColumnIndex(KEY_VENUE_RATING)));

            } while (cursor.moveToNext()); //so long as the cursor is not at the end keep adding Venues
        }
    }

    public void printLinkingTable() {
        SQLiteDatabase database = getReadableDatabase();
        String selectionQuery = "SELECT * FROM " + TABLE_COORDINATES_VENUES;
        Cursor cursor = database.rawQuery(selectionQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Venue v = new Venue();
                v.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                v.setName(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_COORDS_ID))));
                v.setCity(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_VENUES_ID))));

                Log.e("printCoordsVenueTable", cursor.getInt(cursor.getColumnIndex(KEY_ID)) + ", " +
                        String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_COORDS_ID))) + ", " +
                        String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_VENUES_ID))));
            } while (cursor.moveToNext());
        }
    }

}
