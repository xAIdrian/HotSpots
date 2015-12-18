package com.androidtitan.hotspots.Activity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Data.RandomInputs;
import com.androidtitan.hotspots.Fragment.AdderFragment;
import com.androidtitan.hotspots.Fragment.NavigationDrawerFragment;
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


/*
TODO:::

//-> On Complete.  Replace Lock to start over.

-> NAVIGATION DRAWER.
        Recycler view of all past searches
        Ordered last to first
            Let's add a date
            Show it's rank too


-> Landscape formatting
-> Configuration changes and Concurrency
-> Upgrade Venue Fragment


- Circular reveal for mark bar
    -->add a shadow
- Scale/Fade FAB instead of "drop"
- "Gullotine" effect for Nav Bar

//todo: configurations and sizes
REFACTOR. WE NEED TO BE MORE OBJECT ORIENTED
    PUT REPEATED TASKS INTO A FUNCITON e.g. MATERIAL DESIGN AND ANIMATIONS
 */


public class MapsActivity extends AppCompatActivity implements AdderInterface, VenueInterface, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapActivity";

    //public static final String SAVED_INITIAL_BOOL = "hasVisitedMaps";
    public static final String SAVED_DIALOG_BOOL = "savedDialogBool";
    public static final String PASSED_RESULT = "getResult2Pass";

    public static final int ANIM_DURATION = 150;

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

    private DrawerLayout navDrawerLayout; //this will be used if we need to forcefully close our nav drawer
    private Handler handler;

    private LocationBundle focusLocation;

    private ImageButton actionButton;
    private ImageView backer;
    private ImageView locker;
    private TextView goText;

    private LinearLayout markConfirmLayout;
    private TextView markConfirmCancel;
    private TextView markConfirmMark;
    private View shadow;

    private Animation slidein;
    private Animation slideOut;
    private Animation leftSlideIn;
    private Animation leftSlideOut;
    private Animation rightSlideOut;
    private Animation rightSlideIn;
    private Animation fab_pop;
    private Animation fab_pop_settle;
    private Animation fab_close;

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

        Log.e(TAG, "Map Result: " + dividend + "/" + divisor + "=" + dividend / divisor);
        return dividend / divisor;

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


        //todo: new method


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

        //initializations
        //todo: need to all go in one method
        navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        backer = (ImageView) findViewById(R.id.back_action);
        backer.setVisibility(View.GONE);
        locker = (ImageView) findViewById(R.id.locker);
        goText = (TextView) findViewById(R.id.newSpot);
        goText.setVisibility(View.GONE);
        goText.setClickable(false);
        actionButton = (ImageButton) findViewById(R.id.floatingActionImageButton);
        actionButton.setVisibility(View.GONE);

        markConfirmLayout = (LinearLayout) findViewById(R.id.markerLayout);
        markConfirmCancel = (TextView) findViewById(R.id.markerCancel);
        markConfirmMark = (TextView) findViewById(R.id.markerMark);
        markConfirmLayout.setVisibility(View.INVISIBLE);

        shadow = (View) findViewById(R.id.dropshadow);

        //NavigationDrawer
        //todo
        Bundle navDrawerBundle = new Bundle();
        NavigationDrawerFragment navDrawerFragment = new NavigationDrawerFragment();

        try {
            navDrawerBundle.putInt(PASSED_RESULT, getResult());

        } catch (Exception e) {
            navDrawerBundle.putInt(PASSED_RESULT, (int) databaseHelper.getMostRecentVenue().getRating());

        }

        navDrawerFragment.setArguments(navDrawerBundle);
        getFragmentManager().beginTransaction()
                .add(R.id.navDrawer_container, navDrawerFragment).addToBackStack(null).commit();

        //if we've used all of our locations then we lock-it up
        //todo: new method
        if (!isLocked) {
        } else {
            isLocationAdded = true;
            //lockingAction();
            locker.setImageResource(R.drawable.lock_closed);
            actionButton.setImageResource(R.drawable.icon_submit);

            FABstatus = 3;
            //todo: LATER we need to include for if we are Locked AND Scored
            //what is locked and scored

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

                                popCloseAnimation();
                                actionButton.setVisibility(View.GONE);

                                //handler is being used for the return action
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        actionButton.setImageResource(R.drawable.icon_add);
                                        actionButton.setVisibility(View.VISIBLE);
                                        popOpenAnimation();

                                        backer.startAnimation(leftSlideIn);
                                        backer.setVisibility(View.VISIBLE);

                                    }
                                }, ANIM_DURATION);

                            }

                            break;

                        case 1: //ADD FAB

                            FABstatus ++;

                            popCloseAnimation();
                            actionButton.setVisibility(View.GONE);


                            markConfirmLayout.setVisibility(View.VISIBLE);

                            //todo: circular reveal
                            markConfirmLayout.startAnimation(slidein);
                            markConfirmCancel.startAnimation(slidein);
                            markConfirmMark.startAnimation(slidein);

                            markConfirmCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //todo:circular hide
                                    markConfirmLayout.startAnimation(slideOut);
                                    markConfirmCancel.startAnimation(slideOut);
                                    markConfirmMark.startAnimation(slideOut);
                                    markConfirmLayout.setVisibility(View.INVISIBLE);
                                    markConfirmCancel.setVisibility(View.INVISIBLE);
                                    markConfirmMark.setVisibility(View.INVISIBLE);
                                }
                            });

                            //todo: this is where we are going to create our Location
                            markConfirmMark.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    adderBundle = new Bundle();

                                    adderBundle.putDouble(adderFragmentLatitude, currentLatitude);
                                    adderBundle.putDouble(adderFragmentLongitude, currentLongitude);

                                    backer.startAnimation(leftSlideOut);
                                    backer.setVisibility(View.GONE);

                                    //todo:circular hide
                                    markConfirmLayout.startAnimation(slideOut);
                                    markConfirmCancel.startAnimation(slideOut);
                                    markConfirmMark.startAnimation(slideOut);
                                    markConfirmLayout.setVisibility(View.GONE);

                                    toggleFragment(false, adderFragmentTag);

                                    //handler is being used for the return action
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            actionButton.setImageResource(R.drawable.icon_add);
                                            actionButton.setVisibility(View.VISIBLE);
                                            popOpenAnimation();

                                        }
                                    }, ANIM_DURATION);

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

                            }
                            else {
                                Toast.makeText(MapsActivity.this, "Please complete fields", Toast.LENGTH_LONG).show();
                            }

                            break;

                        case 3: //VenueResult fab
                            focusLocation.setLocationRating(getResult());
                            databaseHelper.updateLocationBundle(focusLocation);
                            preSubmitActivities();

                            //todo: here we need to slide these bad boys out and then back in
                            if(locker.getVisibility() != View.GONE) {
                                locker.startAnimation(rightSlideOut);
                                rightSlideOut.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        locker.setVisibility(View.GONE);

                                        goText.setVisibility(View.VISIBLE);
                                        goText.setClickable(true);
                                        goText.startAnimation(rightSlideIn);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }

                            break;

                        case 4: //BACK-SUBMIT fab

                            toggleFragment(true, venueFragmentTag);

                            FABstatus--;

                            popCloseAnimation();
                            actionButton.setVisibility(View.GONE);

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    actionButton.setImageResource(R.drawable.icon_submit);
                                    actionButton.setVisibility(View.VISIBLE);
                                    popOpenAnimation();

                                }
                            }, ANIM_DURATION);


                            break;

                        default:
                            Log.e(TAG, "something went wrong in our FAB switch statement");
                            break;
                    }
                }
            }
        });

        //todo: we are going to reset our FAB integer and the icons
        goText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.this.recreate();
                /*Log.e(TAG, "jefferson");
                FABstatus = 0;

                popCloseAnimation();
                toggleFragment(true, venueFragmentTag);
                actionButton.setImageResource(R.drawable.icon_location);
                popOpenAnimation();

                goText.startAnimation(rightSlideOut);
                rightSlideOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        goText.setVisibility(View.GONE);
                        locker.setImageResource(R.drawable.lock_open);
                        locker.setVisibility(View.VISIBLE);
                        locker.startAnimation(rightSlideIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });*/
            }
        });


        backer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FABstatus == 1) {

                    popCloseAnimation();
                    actionButton.setVisibility(View.GONE);

                    //handler is being used for the return action
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            actionButton.setImageResource(R.drawable.icon_location);
                            actionButton.setVisibility(View.VISIBLE);
                            popOpenAnimation();

                            backer.startAnimation(leftSlideOut);
                            backer.setVisibility(View.GONE);

                            FABstatus--;
                        }
                    }, ANIM_DURATION);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SAVED_DIALOG_BOOL, isLocationAdded);
    }

    //todo: Google Map methods () {
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call  once when {@link #map} is not null.
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
                //setUpMap();
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
//    private void setUpMap() {
//        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
//    }

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
    protected void onStart() {
        super.onStart();
        Log.e("APIclientConnected?", String.valueOf(googleAPIclient.isConnected()));

        googleAPIclient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        //todo:new method
        slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slidin_bottom);
        slidein.setDuration(ANIM_DURATION);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slideout_bottom);
        slideOut.setDuration(ANIM_DURATION);
        leftSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slidein_left);
        leftSlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slideout_left);
        fab_pop = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.fab_pop);
        fab_pop.setDuration(ANIM_DURATION);
        fab_pop_settle = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.fab_pop_settle);
        fab_pop_settle.setDuration(ANIM_DURATION);
        fab_close = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.fab_close);
        fab_close.setDuration(ANIM_DURATION);
        rightSlideOut = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.icon_slideout_right);
        rightSlideIn = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.icon_slidein_right);

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
                    popOpenAnimation();

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

                popCloseAnimation();
                actionButton.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    public void run() {
                        actionButton.setImageResource(R.drawable.icon_submit);
                        actionButton.setVisibility(View.VISIBLE);
                        popOpenAnimation();

                    }
                }, ANIM_DURATION);

                FABstatus--;

            }
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
        popOpenAnimation();
    }

    @Override
    public void selectionPasser(int selectionInt) {
        venueSelection = selectionInt;
    }


    //todo: animation functions

    public void popOpenAnimation() {

        actionButton.setClickable(false);
        actionButton.startAnimation(fab_pop);

        //TODO: do we want a handler or onAnimationEnd() ???
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actionButton.startAnimation(fab_pop_settle);
                actionButton.setClickable(true);
            }
        }, fab_pop.getDuration());
    }

    public void circularReveal() {
        int centerX = (markConfirmLayout.getLeft() + markConfirmLayout.getRight()) / 2;
        int centerY = (markConfirmLayout.getTop() + markConfirmLayout.getBottom()) / 2;

        int startRadius = 0;
        //get the clipping circles final width
        int endRadius = Math.max(markConfirmLayout.getWidth(), markConfirmLayout.getHeight());

        Animator circularRevealAnim = ViewAnimationUtils.createCircularReveal(markConfirmLayout,
                centerX, centerY, startRadius, endRadius);
        markConfirmLayout.setVisibility(View.VISIBLE);
        circularRevealAnim.start();
    }

    public void circularHide() {

    }

    public void popCloseAnimation() {

        actionButton.setClickable(false);
        actionButton.startAnimation(fab_close);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actionButton.setClickable(true);
            }
        }, fab_close.getDuration());
    }

    //todo: view functions

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
                int rando = RandomInputs.randInt(1, 4);
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


    //todo: custom methods
/////////////////////////////////////////////////////////////////////////////////////////////////////



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


    //Called when Location is added
    public void postAdditionActivities(LocationBundle locationBundle) {

        focusLocation = locationBundle;

        map.addMarker(new MarkerOptions()
                .title(locationBundle.getLocalName())
                .position(locationBundle.getLatlng()))
                .showInfoWindow();

        isLocationAdded = true;

        FABstatus++;

        popCloseAnimation();
        actionButton.setVisibility(View.GONE);
        backer.startAnimation(leftSlideOut);
        backer.setVisibility(View.GONE);

        markConfirmLayout.setVisibility(View.GONE);
        markConfirmCancel.setVisibility(View.GONE);
        markConfirmMark.setVisibility(View.GONE);

        //Foursquare API
        //if (databaseHelper.getAllVenuesFromLocation(focusLocation).size() == 0) {
            Log.e(TAG, "Querying Foursquare API...");
            new FoursquareHandler(MapsActivity.this, focusLocation.getLatlng().latitude,
                    focusLocation.getLatlng().longitude, focusLocation.getId()).execute();
        /*}
        else {
            Log.e(TAG, "Trouble");
        }*/
    }

    public void lockingAction() {


        if (!isLocked) {
            if (isLocationAdded) {
                //Toast.makeText(MapsActivity.this, "Locked-in", Toast.LENGTH_SHORT).show();

                locker.setImageResource(R.drawable.lock_closed);
                focusLocation.setIsLocationLocked(true);
                databaseHelper.updateLocationBundle(focusLocation);

                actionButton.setImageResource(R.drawable.icon_submit);
                actionButton.setVisibility(View.VISIBLE);
                popOpenAnimation();

            }

        } else {
            locker.setImageResource(R.drawable.lock_closed);
            focusLocation.setIsLocationLocked(true);
            databaseHelper.updateLocationBundle(focusLocation);

        }
    }

    public void preSubmitActivities() {

        venueBundle = new Bundle();
        venueBundle.putLong(venueFragmentLocIndex, focusLocation.getId());

        toggleFragment(false, venueFragmentTag);

        FABstatus++;

        //animation
        popCloseAnimation();
        actionButton.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            public void run() {
                actionButton.setImageResource(R.drawable.left_arrow);
                actionButton.setVisibility(View.VISIBLE);
                popOpenAnimation();

            }
        }, ANIM_DURATION);

    }

    //todo: note: this will replace our addFragment code that we have in two seperate instances
    private void toggleFragment(boolean shouldPop, String fragTag) {

        if(shouldPop) {
            Log.e(TAG, "should pop");
            getFragmentManager().popBackStack();
            shadow.setVisibility(View.VISIBLE);
        }
        else {
            if(fragTag == venueFragmentTag) {
                venueFragment = new VenueResultsFragment();
                venueFragment.setRetainInstance(true);

                venueFragment.setArguments(venueBundle);


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

