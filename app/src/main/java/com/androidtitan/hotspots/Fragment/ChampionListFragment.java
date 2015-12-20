package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidtitan.hotspots.Adapter.ChampionCursorAdapter;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Interface.NavDrawerInterface;
import com.androidtitan.hotspots.R;



//todo:we need to create a CONTENT_PROVIDER before we can use a CurosrLoader
public class ChampionListFragment extends Fragment {
    private static final String TAG = "ChampionListFragment";

    public DatabaseHelper databaseHelper;
    NavDrawerInterface navDrawerInterface;

    //private static final String[] PROJECTION = new String[] {"_id", "first", "last"};
    public ChampionCursorAdapter cursorAdapter;
    Cursor cursor;

    private ListView listView;

    private int selection = -1;
    public int receivedIndex = -1;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       try {
            navDrawerInterface = (NavDrawerInterface) activity;
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

        getActivity().startManagingCursor(cursor);
        String[] dataColumns = new String[] {"local", "locationRating"}; //database column names
        int[] viewIDs = {R.id.primary_champ_text, R.id.past_rating};
        cursorAdapter = new ChampionCursorAdapter(getActivity(), R.layout.listview_champion_item,
                cursor, dataColumns, viewIDs, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview_champion, container, false);

        listView = (ListView) v.findViewById(R.id.champion_list);
        listView.setAdapter(cursorAdapter);
        View emptyView = v.findViewById(R.id.empty);
        listView.setEmptyView(emptyView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navDrawerInterface = null;
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


