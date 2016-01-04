package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.RandomInputs;
import com.androidtitan.hotspots.Interface.AdderInterface;
import com.androidtitan.hotspots.R;
import com.google.android.gms.maps.model.LatLng;


public class AdderFragment extends Fragment {
    private static final String TAG = "AdderFragment";

    private static final String SAVED_FIRST = "savedFirst";

    RandomInputs randomInputs;

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
        randomInputs = new RandomInputs();

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
        firstEdit.setSelectAllOnFocus(true);

        String quickRandomString = randomInputs.getRandomStringInput();
        firstEdit.setHint(quickRandomString);
        newFname = quickRandomString;

        addBtn = (ImageButton) v.findViewById(R.id.floatingActionImageButton);

        backLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this hides the soft keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                adderInterface.quitToMap();
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

        if (firstEdit!= null && firstEdit.getText().toString().matches("")) {
            return false;
            //Below goes in activity
        } else {
            return true;
        }
    }

}
