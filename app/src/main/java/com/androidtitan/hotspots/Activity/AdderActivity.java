package com.androidtitan.hotspots.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.androidtitan.hotspots.Fragment.AdderFragment;
import com.androidtitan.hotspots.R;

//ToDo: THIS IS GETTING PHASED OUT
public class AdderActivity extends FragmentActivity {
    private final String ADD_FRAG_TAG = "adderTag";

    public static final String CURSOR_UPDATE = "cursorUpdateFromAdder";

    private FragmentManager fragMag;
    private FragmentTransaction fragTran;
    private AdderFragment adderFragment;

    private int soldierIndex;
    private String soldierFname;
    private String soldierLname;

    private Boolean isEditAdder = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);


        Intent intent = getIntent();
        isEditAdder = intent.getBooleanExtra("landingEdit", false);



        try {
            getSoldierEditItems();

        } catch (NullPointerException e) {
            Log.e("AAonCreate", String.valueOf(e));
        }

        //onOrientationChange Block
        if (savedInstanceState != null) {
            //savedInstanceState, fragment may exist. Look up the instance that already exists by tag
            adderFragment = (AdderFragment) getFragmentManager().findFragmentByTag(ADD_FRAG_TAG);
        } else if (adderFragment == null) {
            adderFragment = new AdderFragment();
        }
        if (!adderFragment.isInLayout()) {
            fragMag = getSupportFragmentManager();
            fragTran = fragMag.beginTransaction();
            //todo: fragTran.addToBackStack(null).replace(R.id.landingContainer, adderFragment, ADD_FRAG_TAG).commit();
        }


    }

    @Override
    public void onBackPressed() {
/*
        if(isDivisionAdder == true) {
            isDivisionAdder = false;*/
            Intent intent = new Intent(this, ChampionActivity.class);
            startActivity(intent);
        /*
        else {
            returnToChamp(divisionIndex);
        }*/
    }

    //called from SecondF2AInterface.  Passes integer so main activity can page to
    //newly added soldier's division
    public void returnToChamp(boolean shouldCursorUpdate) {
        Intent intent = new Intent(this, ChampionActivity.class);

        if(shouldCursorUpdate) {
            intent.putExtra(CURSOR_UPDATE, shouldCursorUpdate);
        }

        startActivity(intent);


    }


    public String getSoldierEditItems() {
        Intent intent = getIntent();
//        divisionIndex = intent.getIntExtra("editSoloDivIndex", -1);
        soldierIndex = intent.getIntExtra("editSoloIndex", -1);
        soldierFname = intent.getStringExtra("editSoloFirst");
        soldierLname = intent.getStringExtra("editSoloLast");

        Bundle editArgs = new Bundle();
//        editArgs.putInt("editSoloDivIndex", divisionIndex);
        editArgs.putInt("editSoloIndex", soldierIndex);
        editArgs.putString("editSoloFirst", soldierFname);
        editArgs.putString("editSoloLast", soldierLname);
        adderFragment = new AdderFragment();
        adderFragment.setArguments(editArgs);

        Log.e("AAgetSoldierEditItems", "selection: " + soldierIndex);


        return soldierFname; //this is simply a check to make sure we received
        // for the purpose of the try/catch block
    }
}
