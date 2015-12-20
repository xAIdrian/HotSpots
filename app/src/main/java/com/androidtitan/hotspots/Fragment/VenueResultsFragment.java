package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Adapter.VenueCursorAdapter;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Interface.VenueInterface;
import com.androidtitan.hotspots.Provider.VenueProvider;
import com.androidtitan.hotspots.R;


public class VenueResultsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VenueResultsFragment";

    DatabaseHelper databaseHelper;
    VenueInterface venueInterface;

    //Handler handler = new Handler();
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
            VenueProvider.InterfaceConstants.venue_rating,
            VenueProvider.InterfaceConstants.venue_location_id
    };

    private static final int LOADER_ID = 1;

    private LoaderManager.LoaderCallbacks<Cursor> callBacks;
    public VenueCursorAdapter adapter;

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

        adapter = new VenueCursorAdapter(getActivity(), R.layout.listview_venue_item, null,
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
        scoreView.setText(String.valueOf(focusLocation.getLocationRating()));


        //use an implicit intent to send the user to the foursquare app
        //if they do not have the foursquare app send them to the download screen
        //todo: maybe we can include an option in case they need to download foursquare
        foursquareBtn = (ImageButton) v.findViewById(R.id.foursquareBtn);
        foursquareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection >= 0) {
                    //link construction
                    String selectedVenueId = databaseHelper.getAllVenuesFromLocation(focusLocation)
                            .get(selection).getVenueIdString();
                    String webpageURL = "https://foursquare.com/venue/" + selectedVenueId;

                    Intent fourSquareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webpageURL));
                    Intent intentChooser = Intent.createChooser(fourSquareIntent,
                            getResources().getString(R.string.view_V));

                    //check to make sure there is an application that can handle this intent
                    if (fourSquareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intentChooser);
                    }
                }
            }
        });

        venueInterface.selectionPasser(-1);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

       for (int i = 0; i <= getListView().getLastVisiblePosition() - getListView().getFirstVisiblePosition(); i++) {
            View item = getListView().getChildAt(i);
            item.setBackgroundColor(0xCCFFFFFF);
        }

        if(selection == pos) {  //if we are selecting an already highlighted button
            v.setBackgroundColor(0xCCFFFFFF);
            selection = -1;
        }
        else {
            v.setBackgroundColor(0xCCFFCD38);
            selection = pos;
        }

        //Log.e(TAG, "venuePasserPosition: " + selection + ",, " + );
        venueInterface.selectionPasser(selection);
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

        Log.e(TAG, "onLoadFinished: " + cursor.getCount());
//        getListView().getChildAt(0).setBackgroundColor(0xCCFFFFFF);
        venueCursor = cursor;

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

}
