package com.androidtitan.hotspots.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtitan.hotspots.Activity.MapsActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.R;

/**
 * Created by amohnacs on 8/8/15.
 */
public class ChampionCursorAdapter extends SimpleCursorAdapter {

    DatabaseHelper databaseHelper;
    private Context context;
    private int layout;

    private int selection = -1;

    public ChampionCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
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

        selection = ((MapsActivity) context).getDrawerListViewSelection();
        int position = c.getPosition();

        //Cursor myCursor = c;

        int nameColumn = c.getColumnIndex(DatabaseHelper.KEY_LOCAL_NAME);
        int ratingColumn = c.getColumnIndex(DatabaseHelper.KEY_LOCAL_RATING);

        String locationName = c.getString(nameColumn);
        String locationRating = c.getString(ratingColumn); //0 is Opem aka Unlocked

        TextView nameTextView = (TextView) v.findViewById(R.id.primary_champ_text);
        TextView ratingTextView = (TextView) v.findViewById(R.id.past_rating);


        if(nameTextView != null) {
            nameTextView.setText(locationName);
            ratingTextView.setText(locationRating);
        }


    }

}
