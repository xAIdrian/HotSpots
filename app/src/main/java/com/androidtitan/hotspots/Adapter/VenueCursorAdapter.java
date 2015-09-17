package com.androidtitan.hotspots.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.R;

/**
 * Created by amohnacs on 9/8/15.
 */
public class VenueCursorAdapter extends SimpleCursorAdapter {
    private static final String TAG = "VenueCursorAdapter";

    DatabaseHelper databaseHelper;
    private Context context;
    private int layout;

    private int selection; //MapsActivity.getVenueSelection();

    public VenueCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

        databaseHelper = DatabaseHelper.getInstance(context);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

        selection = ((MapsActivity) context).getVenueSelection();

        int position = c.getPosition();

        int nameColumn = c.getColumnIndex(DatabaseHelper.KEY_VENUE_NAME);
        int ratingColumn = c.getColumnIndex(DatabaseHelper.KEY_VENUE_RATING);

        String name = c.getString(nameColumn);
        String rating = c.getString(ratingColumn);

        TextView nameText = (TextView) v.findViewById(R.id.nameTextView);
        TextView ratingText = (TextView) v.findViewById(R.id.ratingTextView);

        if(nameText != null && ratingText != null) {
            nameText.setText(name);
            ratingText.setText(rating);
        }

        //persistent highlighting
        if (position == selection) {
            Log.e(TAG, "swerve girl");
            v.setBackgroundColor(0xCCFFCD38);
        }
        else {
            v.setBackgroundColor(0xCCFFFFFF);
        }

    }
}
