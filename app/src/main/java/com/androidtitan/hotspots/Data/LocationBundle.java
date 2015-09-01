package com.androidtitan.hotspots.Data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by amohnacs on 7/18/15.
 */
public class LocationBundle {

    private long id;
    private String localName;
    private LatLng latlng;
    private int isLocationLocked; //we translate this into a boolean for easier programmatic use
    private int visitedMap;

    public LocationBundle () {

    }

    public LocationBundle(String name) {
        this.localName = name;
        setVisitedMap(false);
    }

    public LocationBundle(String name, double latitude, double longitude) {
        this.localName = name;
        this.latlng = new LatLng(latitude, longitude);
        setVisitedMap(false);
    }

    public LocationBundle (String name, double latitude, double longitude, int isLocked) {
        this.localName = name;
        this.latlng = new LatLng(latitude, longitude);
        this.isLocationLocked = isLocked;
        setVisitedMap(false);
    }

    public LocationBundle (String name, double latitude, double longitude, int isLocked, int visitedMap) {
        this.localName = name;
        this.latlng = new LatLng(latitude, longitude);
        this.isLocationLocked = isLocked;
        this.visitedMap = visitedMap;
        setVisitedMap(false);
    }

    public LocationBundle(LatLng latlng) {
        this.localName = "Random Location";
        this.latlng = latlng;
    }

    public LocationBundle(String name, LatLng latlng) {
        this.localName = name;
        this.latlng = latlng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public Boolean getVisistedMap() {

        if (visitedMap == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getIsLocationLockedDatabase() {
        return isLocationLocked;
    }

    public void setIsLocationLocked(boolean isLocationLockeded) {
        if(isLocationLockeded) {
            setIsLocationLockedDatabase(1);
        }
        else {
            setIsLocationLockedDatabase(0);
        }
    }

    public void setIsLocationLockedDatabase(int isLocationLockedInt) {
        this.isLocationLocked = isLocationLockedInt;
    }

    public Boolean getIsLocationLocked() {

        if (isLocationLocked == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getVisitedMapDatabase() {
        return visitedMap;
    }

    public void setVisitedMap(boolean visitedMap) {
        if(visitedMap) {
            setVisitedMapDatabase(1);
        }
        else {
            setVisitedMapDatabase(0);
        }
    }

    public void setVisitedMapDatabase(int isVisitedMapInt) {
        this.visitedMap = isVisitedMapInt;
    }
}
