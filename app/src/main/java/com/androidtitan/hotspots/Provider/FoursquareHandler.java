package com.androidtitan.hotspots.Provider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by amohnacs on 8/25/15.
 */


public class FoursquareHandler extends AsyncTask<View, Void, String> {
    private static String TAG = "FoursquareHandler";

    String tempString;

    DatabaseHelper databaseHelper;
    VenueProvider venueProvider;

    private int venueIndexOverride;

    public static final String CLIENT_ID = "CE1BU4JLX2UL5ESYMNKRO14QBOMDCKXONG55XUCBX1MEAPRW";
    public static final String CLIENT_SECRET = "NQ01XNIA4CM0WID0QWML5LSSCU1UU4QDUBFVGUZHNIESGEVT";

    private Context context;

    private String latitude;
    private String longitude;
    private long location_id;
    private LocationBundle locationHandle;

    public ArrayList<Venue> venueArrayList = new ArrayList<>();


    public FoursquareHandler(Context context, double latitude, double longitude, long location_id) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
        this.location_id = location_id;
        locationHandle = databaseHelper.getLocationBundle(location_id);

        Log.e(TAG, "Location Handle ::: " + locationHandle.getLocalName());

        this.venueIndexOverride = databaseHelper.getAllVenues().size();
    }

    @Override
    protected String doInBackground(View... urls) {
        // make Call to the url
        tempString = makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
                + "&v=20130815&ll=" + latitude + "," + longitude + "&radius=" + 400);


        if (tempString == null) {
            // we have an error to the call
            // we can also stop the progress bar

        } else {
            // all things went right
            parseFoursquare(tempString);

        }
        return tempString;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {
        /**
         * TODO: This needs to be improved
         *
         * How?  We need efficient concurrency
         * ThreadPool to manage the AsyncTasks for each Venue
         * Service that is constantly downloading information as soon as a location is set
         *
         * Let's try and launch each individual async task as a venue is found, instead of onPostExecute()
         *     This way they can stay out of the UI thread
         *
         * We can actually run the Async Tasks from on Progress update
         *     If we return a Venue, call PublishProgress
         *
         */

//TODO: if(tempstring == null)

        //todo ::: perhaps we can call this outside of this method
        //for(Venue freshVenue : databaseHelper.getAllVenuesFromLocation(locationHandle)) {
        //new FoursquareVenueHandler(context, freshVenue.getId(), location_id);
        //}

        Log.i(TAG, "onPostExecute!");
        //((MapsActivity) context).setTaskSize(venueArrayList.size());

        int NUMBER_OF_CORES =
                Runtime.getRuntime().availableProcessors();
        // A queue of Runnables
        final BlockingQueue<Runnable> mDecodeWorkQueue;
        // Instantiates the queue of Runnables as a LinkedBlockingQueue
        mDecodeWorkQueue = new LinkedBlockingQueue<>();

        ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES, NUMBER_OF_CORES, 5, TimeUnit.SECONDS, mDecodeWorkQueue);

        for (Venue venueItem : venueArrayList) {
            new FoursquareVenueHandler(context, venueItem , venueIndexOverride).executeOnExecutor(mThreadPoolExecutor);
        }

        ((MapsActivity) context).lockingAction();
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
            //double for loop
            //array of what are acceptable key words
            //iterate through array and IF they match take it

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Venue venueOfInterest = new Venue();
/**/
                        venueOfInterest.setLocation_id(location_id);

                        if (jsonArray.getJSONObject(i).has("name")) {
                            venueOfInterest.setName(jsonArray.getJSONObject(i).getString("name"));

                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
/**/
                                        venueOfInterest.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
                                    }

                                    ////////////////////////////
                                    //VENUE_ID
                                    //we will use this for another URI query and get more detailed information on the venue!!!
                                    if (jsonArray.getJSONObject(i).has("id")) {
                                        //Log.e(TAG, "realVenueId: " + jsonArray.getJSONObject(i).getString("id"));
/**/
                                        venueOfInterest.setVenueIdString(jsonArray.getJSONObject(i).getString("id"));
                                    }

                                    //////////////////////////
                                    if (jsonArray.getJSONObject(i).has("categories")) {
                                        if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
/**/
                                                venueOfInterest.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                                            }
                                        }
                                    }

                                    //todo: uri & ContentValues

                                    venueIndexOverride++;
                                    venueArrayList.add(venueOfInterest);
                                    Log.e(TAG, venueOfInterest.getName());
 /**/

                                    // venueOfInterest.setId(venueIndexOverride);



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




}



