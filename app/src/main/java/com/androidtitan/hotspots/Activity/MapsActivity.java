package com.androidtitan.hotspots.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Fragment.AdderFragment;
import com.androidtitan.hotspots.Fragment.VenueResultsFragment;
import com.androidtitan.hotspots.Interface.AdderInterface;
import com.androidtitan.hotspots.Interface.VenueInterface;
import com.androidtitan.hotspots.Provider.FoursquareHandler;
import com.androidtitan.hotspots.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

/*
COMPLETED???

- //Replace the careful lock in dialog with a transparent splash...for only the FIRST time
        remove the 'INITIAL_VISIT' column form location_table
        we need to reconsider the FAB action "switch statement" without the inital location logic
- Make MapsActivity the Launcher Activity
        remove the 'focusLocation' variable and change it's creation to after the add button.
        we need to add a 'LOCATION_FINAL_RATING' column for our location table. this will be for "View all locations"
        our zoom needs to be closer when we start and when we get the location
        -we will need to name the location after it is created and the coordinates set.
        this can be done just by launching the ADDERACTIVITY
        we will need to pass the same variables on creation that we passed from the listview for camera purposes

 */



/*
TODO:::

-Things will continue as usual...but...UPGRADED from here to get to the VENUE functionality
        VENUE:
            map coordinates (direction?)

        We need to make the "Venue Fragment" exciting!!!
        * Custom Cursor Adapter. include direction, distance, and category
        * Implicit intent to FourSquare app for review OR to google maps for direction.

-> NAVIGATION DRAWER.
        Top half is going to be past stats
        View All past locations and their associated rankings.  View them on the map
        View all past venues.  Provide more information (View all of them on the map?)

REFACTOR. WE NEED TO BE MORE OBJECT ORIENTED
    PUT REPEATED TASKS INTO A FUNCITON e.g. ANIMATIONS
 */


public class MapsActivity extends FragmentActivity implements AdderInterface, VenueInterface, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapActivity";

    public static final String SAVED_INITIAL_BOOL = "hasVisitedMaps";
    public static final String SAVED_DIALOG_BOOL = "savedDialogBool";

    private DatabaseHelper databaseHelper;
    private VenueResultsFragment venueFragment;
    private AdderFragment adderFragment;
    public String venueFragmentTag = "venueFragment";
    public String adderFragmentTag = "adderFragment";
    public static final String venueFragmentLocIndex = "venueFragString";
    public static final String adderFragmentLatitude = "adderFragLat";
    public static final String adderFragmentLongitude = "adderFragLng";

    private GoogleMap map; // NULL if Google Play services APK is not available.
    private GoogleMapOptions options = new GoogleMapOptions();
    private GoogleApiClient googleAPIclient;

    private LocationBundle focusLocation;
    //private Location tempLocation;

    private Handler handler;

    private ImageButton actionButton;
    private ImageView backer;
    private ImageView locker;

    private LinearLayout markConfirmLayout;
    private TextView markConfirmCancel;
    private TextView markConfirmMark;
    private View shadow;

    private Animation slidein;
    private Animation slideOut;
    private Animation leftSlideIn;
    private Animation leftSlideOut;

    private Location lastLocation;
    private LatLng lastLatLang;
    private double currentLatitude;
    private double currentLongitude;

    private int locationIndex = -1;

    private int FABstatus = 0; //0=location 1=add   2=adder submit 3=submit 4=back

    private boolean isLocationAdded = false; //use this to control yo dialogs
    private boolean isLocked = false;

    private Bundle adderBundle;
    private Bundle venueBundle;

    private int venueSelection;


    //// TODO: 9/14/15
    public int divisor;
    public int dividend;

    public int getResult() {
        //logic to account for 0;
        Log.e(TAG, "Map Result: " + divisor + "/" + dividend + "=" + divisor/dividend);
        return divisor/dividend;
    }

    public void setMathResult(float plus) {
        dividend += plus;
        divisor += 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (savedInstanceState != null) {

            isLocationAdded = savedInstanceState.getBoolean(SAVED_DIALOG_BOOL);
        }

        databaseHelper = DatabaseHelper.getInstance(this);

        Intent intent = getIntent();
        locationIndex = intent.getIntExtra(ChampionActivity.SELECTION_TO_MAP, -1);

        //our handle for our MapFragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpMapIfNeeded();

        buildGoogleApiClient();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        //camera
        if (locationIndex > -1) {
            focusLocation = databaseHelper.getLocationBundle(locationIndex);

            if (!focusLocation.getLatlng().equals(new LatLng(0.0, 0.0))) { //if there is something present
            /*this currently gets the first location in their many saved locations...
            eventually we want to be able to 'page' through all of them
            */
                cameraLocation(false, -1, null);
            }

            isLocked = focusLocation.getIsLocationLocked();


            try {
                //tempLocation = new Location("tempLocation");
                //tempLocation.setLatitude(focusLocation.getLatlng().latitude);
                //tempLocation.setLongitude(focusLocation.getLatlng().longitude);

                //focusLocation.getLatlng().latitude;
                //focusLocation.getLatlng().longitude;
            } catch (NullPointerException e) {
                Log.e(TAG, "Blank MAPS: " + String.valueOf(e));
            }
        } else {
            cameraLocation(true, -1, null);
        }


        //place markers for every saved location
        for (LocationBundle bund : databaseHelper.getAllLocations()) {
            try {

                map.addMarker(new MarkerOptions()
                        .position(bund.getLatlng())
                        .title(bund.getLocalName())
                        .snippet(bund.getLocalName()));
            } catch (CursorIndexOutOfBoundsException e) {

                map.addMarker(new MarkerOptions()
                        .position(bund.getLatlng())
                        .title(bund.getLocalName()));
            }
        }

        //databaseHelper.printLocationsTable();

        //initializations

        backer = (ImageView) findViewById(R.id.back_action);
        backer.setVisibility(View.GONE);
        locker = (ImageView) findViewById(R.id.locker);
        actionButton = (ImageButton) findViewById(R.id.floatingActionImageButton);
        actionButton.setVisibility(View.GONE);

        markConfirmLayout = (LinearLayout) findViewById(R.id.markerLayout);
        markConfirmCancel = (TextView) findViewById(R.id.markerCancel);
        markConfirmMark = (TextView) findViewById(R.id.markerMark);
        markConfirmLayout.setVisibility(View.GONE);
        markConfirmCancel.setVisibility(View.GONE);
        markConfirmMark.setVisibility(View.GONE);

        shadow = (View) findViewById(R.id.dropshadow);

        //if we've used all of our locations then we lock-it up
        //this is our Critical Logic
        //todo: we might get rid of locking

        if (!isLocked) {


        } else {
            isLocationAdded = true;
            //lockingAction();
            locker.setImageResource(R.drawable.lock_closed);
            actionButton.setImageResource(R.drawable.icon_submit);

            FABstatus = 3;
            //todo: LATER we need to include for if we are Locked AND Scored

        }


        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //placeholder
            }
        });


        //using existing variables we are going to check which state it is already in
        //on changing state 'actionButton' is going to slide out and then slide back in
        //new sourceImage and newColor (newColor will require 2 new circle backgrounds)

        //maybe we can use a SWITCH statement
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LocationManager manager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        googleAPIclient);

                final AlertDialog.Builder aDawg = new AlertDialog.Builder(MapsActivity.this);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //if GPS is turned off
                    buildAlertMessageNoGps();
                } else {

                    switch (FABstatus) {

                        case 0: //LOCATION fab

                            if (lastLocation != null) {

                                currentLatitude = lastLocation.getLatitude();
                                currentLongitude = lastLocation.getLongitude();
                                lastLatLang = new LatLng(currentLatitude, currentLongitude);

                                Log.e("MAlocationGetter", "Location Found! " + currentLatitude + ", " + currentLongitude);

                                //camera
                                cameraLocation(false, -1, lastLatLang);

                                FABstatus++;

                                actionButton.startAnimation(slideOut);
                                actionButton.setVisibility(View.GONE);

                                //handler is being used for the return action
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        actionButton.setImageResource(R.drawable.icon_add);
                                        actionButton.setVisibility(View.VISIBLE);
                                        actionButton.startAnimation(slidein);

                                        backer.startAnimation(leftSlideIn);
                                        backer.setVisibility(View.VISIBLE);

                                    }
                                }, slideOut.getDuration());

                            }
                            /*Log.e("!!!!!!!!!!!!!", "All Venues");
                            databaseHelper.printVenuesTable();
                            Log.e("!!!!!!!!!!!!!", "Venues By Location");
                            databaseHelper.printVenuesByLocation(focusLocation);
                            Log.e("!!!!!!!!!!!!!", "Linking Table");
                            databaseHelper.printLinkingTable();*/

                            break;

                        case 1: //ADD FAB

                            FABstatus ++;

                            markConfirmLayout.setVisibility(View.VISIBLE);
                            markConfirmCancel.setVisibility(View.VISIBLE);
                            markConfirmMark.setVisibility(View.VISIBLE);
                            markConfirmLayout.startAnimation(slidein);
                            markConfirmCancel.startAnimation(slidein);
                            markConfirmMark.startAnimation(slidein);

                            markConfirmCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    markConfirmLayout.startAnimation(slideOut);
                                    markConfirmCancel.startAnimation(slideOut);
                                    markConfirmMark.startAnimation(slideOut);
                                    markConfirmLayout.setVisibility(View.GONE);
                                    markConfirmCancel.setVisibility(View.GONE);
                                    markConfirmMark.setVisibility(View.GONE);
                                }
                            });

                            //todo: this is where we are going to create our Location
                            markConfirmMark.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //pass location information to frag
                                    //focusLocation = new LocationBundle("tempLocation");

                                    //focusLocation.setLatlng(new LatLng(currentLatitude, currentLongitude));
                                    //databaseHelper.updateLocationBundle(focusLocation);


                                    adderBundle = new Bundle();

                                    adderBundle.putDouble(adderFragmentLatitude, currentLatitude);
                                    adderBundle.putDouble(adderFragmentLongitude, currentLongitude);

                                    actionButton.startAnimation(slideOut);
                                    actionButton.setVisibility(View.GONE);
                                    backer.startAnimation(leftSlideOut);
                                    backer.setVisibility(View.GONE);

                                    markConfirmLayout.startAnimation(slideOut);
                                    markConfirmCancel.startAnimation(slideOut);
                                    markConfirmMark.startAnimation(slideOut);
                                    markConfirmLayout.setVisibility(View.GONE);
                                    markConfirmCancel.setVisibility(View.GONE);
                                    markConfirmMark.setVisibility(View.GONE);

                                    toggleFragment(false, adderFragmentTag);

                                    //todo
                                    actionButton.startAnimation(slideOut);
                                    actionButton.setVisibility(View.GONE);

                                    //handler is being used for the return action
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            actionButton.setImageResource(R.drawable.icon_add);
                                            actionButton.setVisibility(View.VISIBLE);
                                            actionButton.startAnimation(slidein);

                                        }
                                    }, slideOut.getDuration());

                                }
                            });


                            break;

                        case 2: //ADDER FRAG SUBMIT


                            if(adderFragment.getEditTextStatus()) {
                                //add to database. associate division
                                LocationBundle temp = new LocationBundle(adderFragment.newFname);
                                temp.setLatlng(adderFragment.receivedLatLng);

                                databaseHelper.createLocation(temp);

                                toggleFragment(true, adderFragmentTag);
                                postAdditionActivities(databaseHelper.getAllLocations().get(databaseHelper.getAllLocations().size() - 1));
                                databaseHelper.printLocationsTable();
                            }
                            else {
                                Toast.makeText(MapsActivity.this, "Please complete fields", Toast.LENGTH_LONG).show();
                            }

                            break;

                        case 3: //SUBMIT fab

                            try {
                                fragmentAction();
                            } catch (Exception e) {
                                Toast.makeText(MapsActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
                                fragmentAction();
                            }


                            break;

                        case 4: //BACK-SUBMIT fab

                            toggleFragment(true, venueFragmentTag);

                            FABstatus--;

                            actionButton.startAnimation(slideOut);
                            actionButton.setVisibility(View.GONE);

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    actionButton.setImageResource(R.drawable.icon_submit);
                                    actionButton.setVisibility(View.VISIBLE);
                                    actionButton.startAnimation(slidein);

                                }
                            }, slideOut.getDuration());


                            break;

                        default:
                            Log.e(TAG, "something went wrong in our FAB switch statement");
                            break;
                    }
                }
            }
        });


        backer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FABstatus == 1) {
                    actionButton.startAnimation(slideOut);
                    actionButton.setVisibility(View.GONE);

                    //handler is being used for the return action
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            actionButton.setImageResource(R.drawable.icon_location);
                            actionButton.setVisibility(View.VISIBLE);
                            actionButton.startAnimation(slidein);

                            backer.startAnimation(leftSlideOut);
                            backer.setVisibility(View.GONE);

                            FABstatus--;
                        }
                    }, slideOut.getDuration());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }

        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //actions to take when the map is fully loaded...

    }

    //To connect to the API, you need to create an instance of the Google Play services API client
    protected synchronized void buildGoogleApiClient() {
        googleAPIclient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SAVED_DIALOG_BOOL, isLocationAdded);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("APIclientConnected?", String.valueOf(googleAPIclient.isConnected()));

        googleAPIclient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slidin_bottom);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slideout_bottom);
        leftSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slidein_left);
        leftSlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slideout_left);

        handler = new Handler();

        if (locationIndex > -1) {
            if (!focusLocation.getLatlng().equals(new LatLng(0.0, 0.0)) && !isLocked) {
                actionButton.setVisibility(View.GONE);
                FABstatus = 3;
            } //else { previous handler location }
        } else {
            handler.postDelayed(new Runnable() {
                public void run() {

                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.startAnimation(slidein);

                }
            }, 500);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("APIclientConnected?", "Connection Suspended!");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("APIclientConnected?", "Connection Failed!!!");

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        buildAlertMessageNoGps();
    }

    @Override
    public void onBackPressed() {

        if (venueFragment != null) {
            if (venueFragment.isVisible()) {
                toggleFragment(true, venueFragmentTag);

                actionButton.startAnimation(slideOut);
                actionButton.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    public void run() {
                        actionButton.setImageResource(R.drawable.icon_submit);
                        actionButton.setVisibility(View.VISIBLE);
                        actionButton.startAnimation(slidein);

                    }
                }, slideOut.getDuration());

                FABstatus--;

            }
        }
        if (adderFragment.isVisible()) {
            toggleFragment(true, adderFragmentTag);

            actionButton.startAnimation(slideOut);
            actionButton.setVisibility(View.GONE);

            handler.postDelayed(new Runnable() {
                public void run() {
                    actionButton.setImageResource(R.drawable.icon_add);
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.startAnimation(slidein);

                }
            }, slideOut.getDuration());
        }

    }

    @Override
    public void onMapReturn() {
        //this will show our new FAB

    }

    @Override
    public void quitToMap() {

        FABstatus --;
        toggleFragment(true, adderFragmentTag);

        actionButton.setImageResource(R.drawable.icon_add);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.startAnimation(slidein);
    }

    @Override
    public void selectionPasser(int selectionInt) {
        venueSelection = selectionInt;
    }


    //todo: custom methods
/////////////////////////////////////////////////////////////////////////////////////////////////////

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View v;
        private TextView infoHeader;
        private TextView infoSecondary;

        private String title;
        private String secondary;

        public CustomInfoWindowAdapter() {
            v = getLayoutInflater().inflate(R.layout.info_window_custom, null);
        }

        //onCreate()
        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }

        //onCreateView()
        @Override
        public View getInfoWindow(final Marker marker) {

            infoHeader = (TextView) v.findViewById(R.id.info_header);
            infoSecondary = (TextView) v.findViewById(R.id.info_second);

            title = marker.getTitle();
            infoHeader.setText(title);

            try {
                secondary = marker.getSnippet();
                infoSecondary.setText(secondary);
            } catch (NullPointerException e) {
                infoSecondary.setText("");
            }

            return v;
        }
    }

    //generates a random integer
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    //directs the user to a location on the map
    //eventually we will use tha parameter to navigate to SQL row item
    //used for all of the Map Navigations

    //the int will be used in the future if we need to view a number of locations
    private void cameraLocation(boolean isRandom, int locationIndx, LatLng setLatLang) {


        LatLng starterLocation;
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

        if (setLatLang != null) {
            starterLocation = setLatLang;

        } else {

            if (isRandom) {
                int rando = randInt(1, 4);
                starterLocation = databaseHelper.getStarterLocationBundle(rando).getLatlng();
                //add marker

                map.addMarker(new MarkerOptions()
                        .position(databaseHelper.getStarterLocationBundle(rando).getLatlng()));
                zoom = CameraUpdateFactory.zoomTo(12);
            } else {
                starterLocation = focusLocation.getLatlng();

            }
        }

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(starterLocation);

        map.moveCamera(center);
        map.animateCamera(zoom);

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    //this will be removed eventually...the dialog at least
    public void postAdditionActivities(LocationBundle locationBundle) {

        focusLocation = locationBundle;

        //Foursquare API
        if (databaseHelper.getAllVenuesFromLocation(focusLocation).size() == 0) {
            Log.e(TAG, "!!!!!!!! ::: ");
            new FoursquareHandler(MapsActivity.this, focusLocation.getLatlng().latitude,
                    focusLocation.getLatlng().longitude, focusLocation.getId());
        }

        map.addMarker(new MarkerOptions()
                .title(locationBundle.getLocalName())
                .position(locationBundle.getLatlng()))
                .showInfoWindow();

        isLocationAdded = true;

        FABstatus++;

        actionButton.setVisibility(View.GONE);
        backer.setVisibility(View.GONE);

        markConfirmLayout.setVisibility(View.GONE);
        markConfirmCancel.setVisibility(View.GONE);
        markConfirmMark.setVisibility(View.GONE);


        //todo: reasses the WHY we have locking...
        handler.postDelayed(new Runnable() {
            public void run() {
                lockingAction();

            }
        }, slideOut.getDuration());
    }

    public void lockingAction() {


        if (!isLocked) {
            if (isLocationAdded) {

                Toast.makeText(MapsActivity.this, "Locked-in", Toast.LENGTH_SHORT).show();

                locker.setImageResource(R.drawable.lock_closed);
                focusLocation.setIsLocationLocked(true);
                databaseHelper.updateLocationBundle(focusLocation);

                actionButton.setImageResource(R.drawable.icon_submit);
                actionButton.setVisibility(View.VISIBLE);
                actionButton.startAnimation(slidein);

            }

        } else {
            locker.setImageResource(R.drawable.lock_closed);
            focusLocation.setIsLocationLocked(true);
            databaseHelper.updateLocationBundle(focusLocation);

        }
    }

    public String getFocusVenueName() {
        return focusLocation.getLocalName();
    }

    public void fragmentAction() {

        venueBundle = new Bundle();
        venueBundle.putLong(venueFragmentLocIndex, focusLocation.getId());

        toggleFragment(false, venueFragmentTag);

        /*FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        venueFragment.setArguments(venueBundle);
        fragTran.add(R.id.container, venueFragment, venueFragmentTag)
                .addToBackStack(venueFragmentTag).commit();*/

        FABstatus++;

        //animation
        actionButton.startAnimation(slideOut);
        actionButton.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            public void run() {
                actionButton.setImageResource(R.drawable.left_arrow);
                actionButton.setVisibility(View.VISIBLE);
                actionButton.startAnimation(slidein);

            }
        }, slideOut.getDuration());

    }

    //todo: note: this will replace our addFragment code that we have in two seperate instances
    private void toggleFragment(boolean shouldPop, String fragTag) {

        if(shouldPop) {
            Log.e(TAG, "null trash");
            getFragmentManager().popBackStack();
            shadow.setVisibility(View.VISIBLE);
        }
        else {
            if(fragTag == venueFragmentTag) {
                venueFragment = new VenueResultsFragment();
                venueFragment.setRetainInstance(true);

                venueFragment.setArguments(venueBundle);

                focusLocation.setLocationRating(getResult());


                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_slide_up, R.anim.fragment_slide_down,
                                R.anim.fragment_slide_up, R.anim.fragment_slide_down)
                        .add(R.id.container, venueFragment, fragTag).addToBackStack(null).commit();

                shadow.setVisibility(View.GONE);
            }
            if(fragTag == adderFragmentTag) {
                adderFragment = new AdderFragment();
                adderFragment.setRetainInstance(true);

                adderFragment.setArguments(adderBundle);

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_slide_up, R.anim.fragment_slide_down,
                                R.anim.fragment_slide_up, R.anim.fragment_slide_down)
                        .add(R.id.container, adderFragment, fragTag).addToBackStack(null).commit();

                shadow.setVisibility(View.GONE);
            }
        }

    }

    public int getVenueSelection() {
        return venueSelection;
    }
}

