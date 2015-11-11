package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.hotspots.R;


public class NavigationDrawerFragment extends Fragment {
    private String TAG = getClass().getSimpleName();

    private TextView userTitleBtn;
    private TextView alertTitleBtn;
    private TextView scoreNav;
    private ListView navDrawerListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //Navigation Drawer yo!
        userTitleBtn = (TextView) v.findViewById(R.id.userTitleBtn);
        alertTitleBtn = (TextView) v.findViewById(R.id.alertTitleBtn);
        scoreNav = (TextView) v.findViewById(R.id.scoreTextNavDrawer);
        navDrawerListView = (ListView) v.findViewById(R.id.navList);
        addDrawerItems();

        navDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    private void addDrawerItems() {
        String[] optionsArray = {"Flash Back", "Top 10 HotSpots", "IceCubes", "Combat Record"};
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, optionsArray);

        /*try {
            scoreNav.setText(getResult());
        } catch(Exception e) {
            scoreNav.setText(String.valueOf((int) databaseHelper.getMostRecentVenue().getRating()));
        }*/
        navDrawerListView.setAdapter(mAdapter);
    }
}
