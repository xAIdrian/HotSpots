package com.androidtitan.spotscore.main.data;

import android.graphics.drawable.Icon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Category {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pluralName")
    @Expose
    private String pluralName;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("icon")
    @Expose
    private VenueIcon icon;
    @SerializedName("primary")
    @Expose
    private Boolean primary;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The pluralName
     */
    public String getPluralName() {
        return pluralName;
    }

    /**
     *
     * @param pluralName
     * The pluralName
     */
    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    /**
     *
     * @return
     * The shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     *
     * @param shortName
     * The shortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }



/**
     *
     * @return
     * The icon
     */

    public VenueIcon getIcon() {
        return icon;
    }


/**
     *
     * @param icon
     * The icon
     */

    public void setIcon(VenueIcon icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     * The primary
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     *
     * @param primary
     * The primary
     */
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

}
