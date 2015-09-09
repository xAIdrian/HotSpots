package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Interface.AdderInterface;
import com.androidtitan.hotspots.R;
import com.google.android.gms.maps.model.LatLng;


public class AdderFragment extends Fragment {
    private static final String TAG = "AdderFragment";

    private static final String SAVED_FIRST = "savedFirst";

    DatabaseHelper databaseHelper;
    AdderInterface adderInterface;

    private LinearLayout backLayout;

    private EditText firstEdit;
//    private TextView deleteBtn;
    private ImageButton addBtn;

    public String newFname;

    private double receivedLat;
    private double receivedLng;
    public LatLng receivedLatLng;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            adderInterface = (AdderInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ADDERINTERFACE");
        }
    }

    public AdderFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loads data that is saved when the screen is rotated
        databaseHelper = new DatabaseHelper(getActivity());

        setRetainInstance(true); //retains our data object when activity is desroyed
        if(savedInstanceState != null) {
            newFname = savedInstanceState.getString(SAVED_FIRST);
        }
        Bundle bundle = new Bundle();
        bundle = this.getArguments();

        receivedLat = bundle.getDouble(MapsActivity.adderFragmentLatitude);
        receivedLng = bundle.getDouble(MapsActivity.adderFragmentLongitude);
        receivedLatLng = new LatLng(receivedLat, receivedLng);

        Log.e(TAG, String.valueOf(receivedLatLng));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adder, container, false);

        backLayout = (LinearLayout) v.findViewById(R.id.back_layout);

        firstEdit = (EditText) v.findViewById(R.id.name_edit);

        /*deleteBtn = (TextView) v.findViewById(R.id.deleteBtn);
        if(locationIndex == -1) {
            deleteBtn.setTextColor(0xFFFFFFFF);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle("Delete?");

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                LocationBundle focusBundle = databaseHelper.getLocationBundle(locationIndex);
                                Log.e("AFdeleter", focusBundle.getLocalName() + " " + focusBundle.getId());

                                databaseHelper.deleteLocation(focusBundle);

                                //adderInterface.returnToChamp(true);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
*/
        addBtn = (ImageButton) v.findViewById(R.id.floatingActionImageButton);

        backLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                adderInterface.quitToMap();
                //fragment slide out


            }
        });

        firstEdit.setText(newFname);

        firstEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstEdit.setSelectAllOnFocus(true);

            }
        });
        firstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newFname = s.toString();

                //todo: consider implementing this.  We would need to do it for all SPECIAL CHARS
                //newFname = newFname.replace("'","\'");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;

    }


    //Saves data that is lost on rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(SAVED_FIRST, firstEdit.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adderInterface = null;
    }

    //todo: method to get the contents of our EDITEXT for our activity
    public Boolean getEditTextStatus() {
        if (firstEdit.getText().toString().matches("")) {
            return false;
            //Below goes in activity
        } else {
            return true;
        }
    }

}
