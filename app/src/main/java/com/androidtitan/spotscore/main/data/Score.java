package com.androidtitan.spotscore.main.data;

import com.androidtitan.spotscore.main.data.foursquare.VenueIcon;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

/**
 * Created by amohnacs on 6/15/16.
 */
public class Score {

    private LatLng mLatLng;
    private String mNote;
    private double mScore;

    public Score() {

    }

    public Score(LatLng mLatLng, String mNote, double mScore) {
        this.mLatLng = mLatLng;
        this.mNote = mNote;
        this.mScore = mScore;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public double getScore() {
        return mScore;
    }

    public void setScore(double mScore) {
        this.mScore = mScore;
    }
}
