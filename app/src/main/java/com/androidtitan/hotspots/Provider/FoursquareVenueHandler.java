package com.androidtitan.hotspots.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.Venue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by amohnacs on 8/25/15.
 */
/*TODO: let's call this in our fragment
    What needs to happen?
    We are going to hold off on adding all our venues to the VenueProvider


    */
public class FoursquareVenueHandler extends AsyncTask<View, Void, Boolean>{
    private static String TAG = "FoursquareVenueHandler";

    //test
    DatabaseHelper databaseHelper;
    //VenueProvider venueProvider;

    private Context context;

    private String focus_venue_id;

    private String tempString;
    private Venue focusVenue;
    private int venueIndexOverride;


    public FoursquareVenueHandler(Context context, Venue passerVenue, int venueindexoverride) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        this.focusVenue = passerVenue;
        this.venueIndexOverride = venueindexoverride;

        //new fourquareVenue().execute();
    }


        @Override
        protected Boolean doInBackground(View... urls) {
            // make Call to the url
            tempString = makeCall("https://api.foursquare.com/v2/venues/" + focus_venue_id + "?client_id="
                    + FoursquareHandler.CLIENT_ID + "&client_secret=" + FoursquareHandler.CLIENT_SECRET
                    + "&v=20130815");

            if (tempString == null) {
                // we have an error to the call
                // we can also stop the progress bar
                Log.e(TAG, "ERROR: TEMPSTRING == NULL");
            } else {
                // all things went right
                parseFoursquare(tempString);
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            focus_venue_id = focusVenue.getVenueIdString();

        }

        @Override
        protected void onPostExecute(Boolean result) {


            //int topHat = ((MapsActivity)context).getResult();
            Log.i(TAG, "On Post Execute!");
        }




    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // trim the whitespaces
        return replyString.trim();
    }

    public void parseFoursquare(final String response) {

        try {

            // make an jsonObject in order to parse the response
            JSONObject initialObject = new JSONObject(response);
            if (initialObject.has("response")) {

                JSONObject jsonObject = new JSONObject(response).getJSONObject("response");

                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
//                    Log.i("**********", "**********");
//                    Log.i("resonse key", key);

                    JSONObject innerJObject = jsonObject.getJSONObject(key);
                    if(innerJObject.has("rating")) {
                        String rating = innerJObject.getString("rating");

                        Log.i(TAG, focusVenue.getName() + " has a rating of " + rating);
/**/                    focusVenue.setRating(Float.parseFloat((rating)));
                        //Log.e(TAG, focusVenue.getName() + ": " + focusVenue.getRating());

                        addFoursquareHandler(focusVenue);

                    }
                    else {
                        //todo: block for a null rating
                        Log.i(TAG, focusVenue.getName() + " has NO rating, setting to 0");
                        focusVenue.setRating(0);

                        addFoursquareHandler(focusVenue);
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    this creates your Venue item and populates your VenueProvider as a means to update
    your List of Venues
    */
    public void addFoursquareHandler(Venue venue) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if(venue.getRating() == 0) {
            //insert row
            //context.getContentResolver().delete(Uri.parse(VenueProvider.base_CONTENT_URI),
              //      DatabaseHelper.KEY_ID + " = ?", new String[] { String.valueOf(venue.getId()) });
        }
        else {
            ContentValues values = new ContentValues();

            //Log.e(TAG, venueProvider.base_CONTENT_URI + venue.getId());

            //try/catch?
            //values.put(DatabaseHelper.KEY_ID, venue.getId());
            values.put(DatabaseHelper.KEY_VENUE_NAME, venue.getName());
            values.put(DatabaseHelper.KEY_VENUE_CITY, venue.getCity());
            values.put(DatabaseHelper.KEY_VENUE_CATEGORY, venue.getCategory());
            values.put(DatabaseHelper.KEY_VENUE_STRING, venue.getVenueIdString());
            values.put(DatabaseHelper.KEY_VENUE_RATING, (int) venue.getRating());
            values.put(DatabaseHelper.KEY_VENUE_LOCATION_ID, venue.getLocation_id());

            //insert row
            context.getContentResolver().insert(Uri.parse(VenueProvider.base_CONTENT_URI + venueIndexOverride), values);



            ((MapsActivity) context).setMathResult(venue.getRating());



/*
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_VENUE_RATING, venue.getRating());

            //insert row
            context.getContentResolver().update(Uri.parse(VenueProvider.base_CONTENT_URI), values,
                    DatabaseHelper.KEY_ID + " = ?", new String[]{String.valueOf(venue.getId())});*/
        }

    }




}
