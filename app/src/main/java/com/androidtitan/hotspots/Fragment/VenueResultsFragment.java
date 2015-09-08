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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Data.Venue;
import com.androidtitan.hotspots.Provider.FoursquareHandler;
import com.androidtitan.hotspots.Provider.VenueProvider;
import com.androidtitan.hotspots.R;


public class VenueResultsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VenueResultsFragment";

    DatabaseHelper databaseHelper;

    Handler handler = new Handler();

    private Bundle providerBundle;
    private LocationBundle focusLocation;
    private long locationIndex;

    private TextView locationNameView;
    private TextView scoreView;

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

        if (databaseHelper.getAllVenuesFromLocation(focusLocation).size() == 0) {
            Log.e(TAG, "!!!!!!!! ::: ");
            new FoursquareHandler(getActivity(), focusLocation.getLatlng().latitude,
                    focusLocation.getLatlng().longitude, focusLocation.getId());
        }

        Log.e(TAG, "locationIndex: " + locationIndex + ", focusLocation: " + focusLocation.getLocalName());



        // getLoaderManager().restartLoader(LOADER_ID, providerBundle, callBacks);
        String[] dataColumns = { VenueProvider.InterfaceConstants.venue_name };
        int[] viewItems = { R.id.nameTextView };

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_venue_item, null,
                dataColumns, viewItems, 0);

        setListAdapter(adapter);

        callBacks = this;

        getLoaderManager().initLoader(LOADER_ID, null, callBacks);
        //makeProviderBundle(PROJECTION, VenueProvider.InterfaceConstants.venue_name, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_venue_results, container, false);

        locationNameView = (TextView) v.findViewById(R.id.locationNameText);
        locationNameView.setText(focusLocation.getLocalName());
        scoreView = (TextView) v.findViewById(R.id.scoreText);



        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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

        Log.e(TAG, "onLoadFinished: " + cursor.getCount());

        //todo: LET'S SEE IF WE CAN GET NOTIFIED WHEN THE "background thread" HAS COMPLETED
        //TODO: NO WORK IS TOO MUCH
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yourScore();
            }
        }, 1500);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, "onLoaderReset");

        adapter.swapCursor(null);
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


        int divisor = 0;
        int dividend = 0;

        for(Venue v : databaseHelper.getAllVenuesFromLocation(focusLocation)) {
            divisor ++;
            dividend += v.getRating();
        }
        int xRated = dividend / divisor;
        scoreView.setText(String.valueOf(xRated));

        focusLocation.setLocationRating(xRated);
        databaseHelper.updateLocationBundle(focusLocation);

    }

}
