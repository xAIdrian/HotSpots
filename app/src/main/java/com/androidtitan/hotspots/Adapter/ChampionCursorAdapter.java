package com.androidtitan.hotspots.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidtitan.hotspots.Activity.ChampionActivity;
import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.R;

/**
 * Created by amohnacs on 8/8/15.
 */
public class ChampionCursorAdapter extends SimpleCursorAdapter {

    DatabaseHelper databaseHelper;
    private Context context;

    private int layout;

    private int selection;

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

        selection = ((ChampionActivity) context).getListViewSelection();
        int position = c.getPosition();


        Cursor myCursor = c;

        int nameColumn = myCursor.getColumnIndex(DatabaseHelper.KEY_LOCAL_NAME);
        //int latColumn = myCursor.getColumnIndex(DatabaseHelper.KEY_LATITUDE);
        //int longColumn = myCursor.getColumnIndex(DatabaseHelper.KEY_LONGITUDE);
        int lockedColumn = myCursor.getColumnIndex(DatabaseHelper.KEY_LOCKED);

        String firstName = myCursor.getString(nameColumn);
        int lockedStatus = myCursor.getInt(lockedColumn); //0 is Opem aka Unlocked

        TextView textView = (TextView) v.findViewById(R.id.primary_champ_text);

        ImageView lockIcon = (ImageView) v.findViewById(R.id.lockImageView);
        lockIcon.setVisibility(View.GONE);

        if(textView != null) {
            textView.setText(firstName);
        }

        //locked location notifier
        if(databaseHelper.getAllLocations().get(position).getIsLocationLocked()) {
            lockIcon.setVisibility(View.VISIBLE);
        }

        //persistent highlighting
        if (position == selection) {
            v.setBackgroundColor(0xCCFFCD38);
        }
        else {
            v.setBackgroundColor(0xFFFFFFFF);
        }


    }

}
