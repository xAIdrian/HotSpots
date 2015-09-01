package com.androidtitan.hotspots.Provider;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by amohnacs on 8/25/15.
 */
//TODO: let's call this in our fragment
public class FoursquareVenueHandler {
    private static String TAG = "FoursquareVenueHandler";

    DatabaseHelper databaseHelper;
    VenueProvider venueProvider;

    private Context context;

    //private long venueDBid;
    private String venue_id;
    private long venueDBid;

    Venue focusVenue;

    List<Integer> ratingsList;

    public FoursquareVenueHandler(Context context, long venueDBid) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        this.venueDBid = venueDBid;
        //this.venueDBid = venueDBid;
        ratingsList = new ArrayList<Integer>();

        new fourquareVenue().execute();
    }

    public class fourquareVenue extends AsyncTask<View, Void, String> {

        String tempString;


        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            tempString = makeCall("https://api.foursquare.com/v2/venues/" + venue_id + "?client_id="
                    + FoursquareHandler.CLIENT_ID + "&client_secret=" + FoursquareHandler.CLIENT_SECRET
                    + "&v=20130815");

            Log.e(TAG, tempString);

            return "";
        }

        @Override
        protected void onPreExecute() {
            focusVenue = databaseHelper.getVenue(venueDBid);
            venue_id = focusVenue.getVenueIdString();

        }

        @Override
        protected void onPostExecute(String result) {
            if (tempString == null) {
                // we have an error to the call
                // we can also stop the progress bar
                Log.e(TAG, "ERROR: TEMPSTRING == NULL");
            } else {
                // all things went right
                parseFoursquare(tempString);
            }
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
            JSONObject initialObject = new JSONObject(response);
            if (initialObject.has("response")) {

                JSONObject jsonObject = new JSONObject(response).getJSONObject("response");

                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Log.i("**********", "**********");
                    Log.i("resonse key", key);

                    JSONObject innerJObject = jsonObject.getJSONObject(key);
                    if(innerJObject.has("rating")) {
                        String rating = innerJObject.getString("rating");

                        focusVenue.setRating(Float.parseFloat((rating)));
                        databaseHelper.updateVenue(focusVenue);
                        venueProvider.update(Uri.parse(VenueProvider.base_CONTENT_URI + focusVenue.getId()),
                                null, null, null);

                        //Log.e(TAG, focusVenue.getName() + " : " + focusVenue.getRating());
                    }
                    else {
                        //Log.e(TAG, "no rating");
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
