package com.androidtitan.hotspots.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Data.Venue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by amohnacs on 8/25/15.
 */
public class FoursquareHandler {
    private static String TAG = "FoursquareHandler";

    DatabaseHelper databaseHelper;
    VenueProvider venueProvider;

    int venueIndexOverride;

    public static final String CLIENT_ID = "CE1BU4JLX2UL5ESYMNKRO14QBOMDCKXONG55XUCBX1MEAPRW";
    public static final String CLIENT_SECRET = "NQ01XNIA4CM0WID0QWML5LSSCU1UU4QDUBFVGUZHNIESGEVT";

    private Context context;

    private String latitude;
    private String longitude;
    private long location_id;
    private LocationBundle locationHandle;

    public static int venueCounter;


    public FoursquareHandler(Context context, double latitude, double longitude, long location_id){
        this.context=context;
        databaseHelper = DatabaseHelper.getInstance(context);
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
        this.location_id = location_id;
        locationHandle = databaseHelper.getLocationBundle(location_id);

        Log.e(TAG, "Location Handle ::: " + locationHandle.getLocalName());

        this.venueIndexOverride = databaseHelper.getAllVenues().size();

        new fourquare().execute();
    }

    public class fourquare extends AsyncTask<View, Void, String> {

        String tempString;


        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            tempString = makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
                    + "&v=20130815&ll=" + latitude + "," + longitude + "&radius=" + 400);

            return "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            if (tempString == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right
                parseFoursquare(tempString);

                //todo ::: perhaps we can call this outside of this method
                for(Venue freshVenue : databaseHelper.getAllVenuesFromLocation(locationHandle)) {
                    new FoursquareVenueHandler(context, freshVenue.getId(), location_id);
                }
            }

            Log.i(TAG, "onPostExecute!");
            Toast.makeText(((MapsActivity)context), "One sec...", Toast.LENGTH_LONG);
            ((MapsActivity) context).lockingAction();


        }
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
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Venue poi = new Venue();
/**/                        poi.setLocation_id(location_id);

                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).getString("name"));

                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
    /**/                                    poi.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
                                    }

                                    ////////////////////////////
                                    //VENUE_ID
                                    //we will use this for another URI query and get more detailed information on the venue!!!
                                    if (jsonArray.getJSONObject(i).has("id")) {
                                        //Log.e(TAG, "realVenueId: " + jsonArray.getJSONObject(i).getString("id"));
/**/                                        poi.setVenueIdString(jsonArray.getJSONObject(i).getString("id"));
                                    }

                                    //////////////////////////
                                    if (jsonArray.getJSONObject(i).has("categories")) {
                                        if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
/**/                                                poi.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                                            }
                                        }
                                    }

                                    //todo: uri & ContentValues

                                    venueIndexOverride ++;
 /**/                                  // poi.setId(venueIndexOverride); //todo

                                    creater(poi);

                                    //databaseHelper.assignVenueToLocation();

                                    //todo:
                                    //we replace createVenue with venueProvider.insert()
                                        //make appropriate changes to .insert()
                                    //we assign to location outside of the insert

                                    //perhaps we can do the same thing with VenueHandler
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void creater(Venue venue) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        long id = 0;
        ContentValues values = new ContentValues();

//        Log.e(TAG, venueProvider.base_CONTENT_URI + venue.getId());

        //try/catch?
        //values.put(DatabaseHelper.KEY_ID, venue.getId());
        values.put(DatabaseHelper.KEY_VENUE_NAME, venue.getName());
        values.put(DatabaseHelper.KEY_VENUE_CITY, venue.getCity());
        values.put(DatabaseHelper.KEY_VENUE_CATEGORY, venue.getCategory());
        values.put(DatabaseHelper.KEY_VENUE_STRING, venue.getVenueIdString());
        values.put(DatabaseHelper.KEY_VENUE_RATING, venue.getRating());
        values.put(DatabaseHelper.KEY_VENUE_LOCATION_ID, venue.getLocation_id());


        //insert row
        context.getContentResolver().insert(Uri.parse(venueProvider.base_CONTENT_URI + venueIndexOverride), values);

    }

}

