package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Provider.VenueProvider;
import com.androidtitan.hotspots.R;


public class VenueResultsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VenueResultsFragment";

    DatabaseHelper databaseHelper;

    String locationName;

    private static final String[] PROJECTION = new String[] {
            VenueProvider.InterfaceConstants.id,
            VenueProvider.InterfaceConstants.venue_name,
            VenueProvider.InterfaceConstants.venue_city,
            VenueProvider.InterfaceConstants.venue_category,
            VenueProvider.InterfaceConstants.venue_id_string,
            VenueProvider.InterfaceConstants.venue_rating};

    private static final int LOADER_ID = 1;

    private LoaderManager.LoaderCallbacks<Cursor> callBacks;
    private SimpleCursorAdapter adapter;

    private int startID;
    private int endID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(getActivity());

        locationName = getArguments().getString(MapsActivity.venueFragmentString);

        String[] dataColumns = { VenueProvider.InterfaceConstants.venue_name };
        int[] viewItems = { R.id.nameTextView };

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_venue_item, null,
                dataColumns, viewItems, 0);

        setListAdapter(adapter);

        callBacks = this;

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, callBacks);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_venue_results, container, false);

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

        Log.e(TAG, "pre-cursorLoader locationName: " + locationName);

        return new CursorLoader(getActivity(), Uri.parse(VenueProvider.base_CONTENT_URI + locationName ), //+ "/" + locationName
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:

                adapter.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }


    /////////// custom methods ////////////////////////////

    public void startNendIds() {
        //startID = databaseHelper.getAllVenuesFromLocation()

    }

    //this method will go through all of the venues associated to this location and score the location
    //Average of the ratings
        //maybe more will be added
    public void yourScore() {

    }
}
