package com.androidtitan.spotscore.main.data.foursquare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class VenueResponse {

    @SerializedName("venues")
    @Expose
    private List<Venue> venues = new ArrayList<Venue>();
    @SerializedName("confident")
    @Expose
    private Boolean confident;

    /**
     *
     * @return
     * The venues
     */
    public List<Venue> getVenues() {
        return venues;
    }

    /**
     *
     * @param venues
     * The venues
     */
    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    /**
     *
     * @return
     * The confident
     */
    public Boolean getConfident() {
        return confident;
    }

    /**
     *
     * @param confident
     * The confident
     */
    public void setConfident(Boolean confident) {
        this.confident = confident;
    }

}
