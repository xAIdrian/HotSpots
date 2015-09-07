package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidtitan.hotspots.Adapter.ChampionCursorAdapter;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Interface.ChampionInterface;
import com.androidtitan.hotspots.R;



//todo:we need to create a CONTENT_PROVIDER before we can use a CurosrLoader
public class ChampionListFragment extends Fragment {
    private static final String TAG = "ChampionListFragment";
    private static final int URL_LOADER = 0;

    public DatabaseHelper databaseHelper;
    public ChampionInterface championInterface;

    LoaderManager loaderManager;
    //we are using a 'LoaderCallback' because Loaders should always be called from the main thread...your Activity
    private static final String[] PROJECTION = new String[] {"_id", "first", "last"};
    public ChampionCursorAdapter cursorAdapter;
    Cursor cursor;

    private ImageView editer;
    private ImageView adder;

    private ListView listView;
    private ImageButton submitFAB;

    private Animation leftSlideIn;
    private Animation leftSlideOut;
    private Animation bottomSlideIn;
    private Animation bottomSlideOut;

    private LocationBundle focusBundle;

    private int position = -1;
    private int selection = -1;
    public int receivedIndex = -1;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            championInterface = (ChampionInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    public ChampionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
        }

        //getActivity Extra

        databaseHelper = DatabaseHelper.getInstance(getActivity());

        //Cursor Adapter Implementation
        Cursor cursor = getListItems();
        if (cursor != null)
            cursor.moveToFirst();

        getActivity().startManagingCursor(cursor);  //todo: this is deprecated.
                                                    //todo: Do we want to eventually consider using a 'Content Provider' and 'CursorLoader'?
        String[] dataColumns = new String[] {"local"};
        int[] viewIDs = {R.id.primary_champ_text};
        cursorAdapter = new ChampionCursorAdapter(getActivity(), R.layout.listview_champion_item,
                cursor, dataColumns, viewIDs, 0);


/*        //getting Extras from Activity
        Intent intent = getActivity().getIntent();
       shouldCursorUpdate = intent.getBooleanExtra(AdderActivity.CURSOR_UPDATE, false);

        if(shouldCursorUpdate) {
            cursorAdapter.changeCursor(getListItems());
        }*/


        //Animations
        leftSlideIn = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slidein_left);
        leftSlideOut = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slideout_left);
        bottomSlideIn = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slidin_bottom);
        bottomSlideOut = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slideout_bottom);

        //runnable placeholder

        //variables

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview_champion, container, false);
/*these are not necessary
        for(Soldier s : databaseHelper.getAllSoldiers()) {
            Log.e("troopChecker", "ID: " + s.getId() + "  Name: " + s.getfName() + " " + s.getlName());
        }

        databaseHelper.printSoldierTable();
*/
        editer = (ImageView) getActivity().findViewById(R.id.editBtn);
        editer.setVisibility(View.GONE);
        adder = (ImageView) getActivity().findViewById(R.id.addBtn);
        submitFAB = (ImageButton) v.findViewById(R.id.submitImageButton);
        submitFAB.setVisibility(View.GONE);


        listView = (ListView) v.findViewById(R.id.champion_list);
        listView.setAdapter(cursorAdapter);
        View emptyView = v.findViewById(R.id.empty);
        listView.setEmptyView(emptyView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Handler handler = new Handler();

                for (int i = 0; i <= listView.getLastVisiblePosition() - listView.getFirstVisiblePosition(); i++) {
                    View item = listView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }

                //ITEM HIGHLIGHT

                //Deselection of an item
                if (selection == position) {
                    view.setBackgroundColor(0xFFFFFFFF);

                    selection = -1;

                    editer.startAnimation(leftSlideOut);
                    submitFAB.startAnimation(bottomSlideOut);
                    listView.setEnabled(false);

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            editer.setVisibility(View.GONE);
                            submitFAB.setVisibility(View.GONE);

                            adder.setVisibility(View.VISIBLE);
                            adder.startAnimation(leftSlideIn);

                            listView.setEnabled(true);
                        }
                    }, leftSlideOut.getDuration());
                }
                //Selection of an item
                else {
                    view.setBackgroundColor(0xCCFFCD38);

                    //slidein animation
                    if (selection == -1) {

                        adder.startAnimation(leftSlideOut);
                        listView.setEnabled(false);

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                adder.setVisibility(View.GONE);

                                if(!focusBundle.getIsLocationLocked()) {
                                    editer.setVisibility(View.VISIBLE);
                                    editer.startAnimation(leftSlideIn);
                                }

                                submitFAB.setVisibility(View.VISIBLE);
                                submitFAB.startAnimation(bottomSlideIn);

                                listView.setEnabled(true);
                            }
                        }, leftSlideOut.getDuration());
                    }
                    selection = position;

                }

                championInterface.setListViewSelection(position);
                focusBundle = databaseHelper.getAllLocations().get(position);

            }
        });

        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("CLFediter", "Position: " + position + " Selection: " + selection);
                championInterface.soldierPasser(selection,
                        focusBundle.getLocalName());
            }
        });

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //when we receive our divIndex then that is what we will pass into this method

                championInterface.soldierPasser(position, null);
            }
        });


        submitFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LocationManager manager = (LocationManager) getActivity()
                        .getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    Log.e(TAG, "Selection: " + String.valueOf(selection));

                    //databaseHelper.getAllSoldiers().get(selection)
                    championInterface.selectionToMap(selection);
                }


            }
        });


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        championInterface = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        //cursor.requery();
        getListItems();
    }

    /////////////// todo: custom methods //////////////////////////////////////////////////////////////

    private Cursor getListItems() {

        //DatabaseHandler is a SQLiteOpenHelper class connecting to SQLite
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        // Get access to the underlying writeable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back

        /*String selectQuery = "SELECT * FROM soldiers ts, divisions td, command tc WHERE td."
                + "name = '" + databaseHelper.getAllDivisions().get(receivedIndex).getName() + "' AND td."
                + "_id = tc.division_id AND ts._id = tc.soldier_id";*/

        //Cursor cursor;

        if (receivedIndex == -1) {
            cursor = db.rawQuery("SELECT * FROM locations", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM locations", null);
        }

        return cursor;
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        //this should stop processes when navigating away to open Location
        super.onStop();
    }

}


