package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Data.Venue;
import com.androidtitan.hotspots.Interface.VenueInterface;
import com.androidtitan.hotspots.Provider.VenueProvider;
import com.androidtitan.hotspots.R;


public class VenueResultsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VenueResultsFragment";

    DatabaseHelper databaseHelper;
    VenueInterface venueInterface;

    Handler handler = new Handler();
    private int selection = -1;

    Cursor venueCursor;

    private Bundle providerBundle;
    private LocationBundle focusLocation;
    private long locationIndex;

    private TextView locationNameView;
    private TextView scoreView;
    private ImageButton foursquareBtn;

    private static final String[] PROJECTION = new String[]{
            VenueProvider.InterfaceConstants.id,
            VenueProvider.InterfaceConstants.venue_name,
            VenueProvider.InterfaceConstants.venue_city,
            VenueProvider.InterfaceConstants.venue_category,
            VenueProvider.InterfaceConstants.venue_id_string,
            VenueProvider.InterfaceConstants.venue_rating
    };

    private static final int LOADER_ID = 1;

    private LoaderManager.LoaderCallbacks<Cursor> callBacks;
    private SimpleCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(getActivity());

        locationIndex = getArguments().getLong(MapsActivity.venueFragmentLocIndex);
        focusLocation = databaseHelper.getLocationBundle(locationIndex);

        Log.e(TAG, "locationIndex: " + locationIndex + ", focusLocation: " + focusLocation.getLocalName());


        String[] dataColumns = { VenueProvider.InterfaceConstants.venue_name,
                VenueProvider.InterfaceConstants.venue_rating };
        int[] viewItems = { R.id.nameTextView, R.id.ratingTextView };

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_venue_item, null,
                dataColumns, viewItems, 0);

        setListAdapter(adapter);

        callBacks = this;

        getLoaderManager().initLoader(LOADER_ID, null, callBacks);
        //makeProviderBundle(PROJECTION, viewItems, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_venue_results, container, false);

        locationNameView = (TextView) v.findViewById(R.id.locationNameText);
        locationNameView.setText(focusLocation.getLocalName());

        scoreView = (TextView) v.findViewById(R.id.scoreText);
        Log.e(TAG, String.valueOf(focusLocation.getLocationRating()));
        scoreView.setText(String.valueOf(focusLocation.getLocationRating()));

        foursquareBtn = (ImageButton) v.findViewById(R.id.foursquareBtn);
        foursquareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selection == -1) {
                    //do nothing
                }
                else {
                    //use an implicit intent to send the user to the foursquare app
                    //if they do not have the foursquare app send them to the download screen
                }
            }
        });


        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

       /* for (int i = 0; i <= getListView().getLastVisiblePosition() - getListView().getFirstVisiblePosition(); i++) {
            View item = getListView().getChildAt(i);
            item.setBackgroundColor(0xCCFFCD38);
        }*/

        if(selection == pos) {  //if we are selecting an already highlighted button
            v.setBackgroundColor(0xCCFFFFFF);
            selection = -1;
        }
        else {
            v.setBackgroundColor(0xCCFFCD38);
        }
        venueInterface.selectionPasser(pos);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            venueInterface = (VenueInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement venueInterface...Oops!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        venueInterface = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "onCreateLoader");

        return new CursorLoader(getActivity(),
                Uri.parse(VenueProvider.base_CONTENT_URI + focusLocation.getLocalName()),
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        switch (loader.getId()) {
            case LOADER_ID:
                adapter.swapCursor(cursor);
                break;
        }

        venueCursor = cursor;

        Log.e(TAG, "onLoadFinished: " + cursor.getCount());

        //todo: LET'S SEE IF WE CAN GET NOTIFIED WHEN THE "background thread" HAS COMPLETED
        //TODO: NO WORK IS TOO MUCH
       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yourScore();
            }
        }, 1000);*/


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, "onLoaderReset");

        adapter.swapCursor(venueCursor);
    }


    /////////// custom methods ////////////////////////////

    //I can see this being useful when we want to handle an orientation change
    public void makeProviderBundle(String[] projection, String selection, String[] selectionArgs, String sortOrder){
    /*this is a convenience method to pass it arguments
     * to pass into myBundle which in turn is passed
     * into the Cursor loader to query the smartcal.db*/
        providerBundle = new Bundle();
        providerBundle.putStringArray("projection", projection);
        providerBundle.putString("selection", selection);
        providerBundle.putStringArray("selectionArgs", selectionArgs);
        if(sortOrder != null) providerBundle.putString("sortOrder", sortOrder);
    }

    //this method will go through all of the venues associated to this location and score the location
    //Average of the ratings
        //maybe more will be added
    public void yourScore() {


        int xRated = 0;
        int divisor = 0;
        int dividend = 0;

        Log.e(TAG, "SIZE: " + databaseHelper.getAllVenuesFromLocation(focusLocation));
        for(Venue v : databaseHelper.getAllVenuesFromLocation(focusLocation)) {
            divisor += 1;
            dividend += v.getRating();
        }


        if(dividend/divisor == 0) {
            Toast.makeText(getActivity(), "Loading Score", Toast.LENGTH_SHORT).show();

            //new FoursquareHandler(getActivity(), focusLocation.getLatlng().latitude,
                    //focusLocation.getLatlng().longitude, focusLocation.getId());

            //getLoaderManager().restartLoader(LOADER_ID, providerBundle, callBacks);
        }
        else {
            xRated = dividend / divisor;
            scoreView.setText(String.valueOf(xRated));

        }

        Log.e(TAG, dividend + " / " + divisor);
        Log.e(TAG, "result: " + xRated);

        focusLocation.setLocationRating(xRated);
        databaseHelper.updateLocationBundle(focusLocation);

    }

}
