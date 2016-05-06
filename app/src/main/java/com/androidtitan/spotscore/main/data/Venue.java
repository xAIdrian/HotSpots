package com.androidtitan.spotscore.main.data;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Venue {

    @SerializedName("id")
    @Expose
    @Nullable
    private String id;
    @SerializedName("name")
    @Expose
    @Nullable
    private String name;
    @SerializedName("contact")
    @Expose
    @Nullable
    private Contact contact;
    @SerializedName("location")
    @Expose
    @Nullable
    private Location location;
    @SerializedName("canonicalUrl")
    @Expose
    @Nullable
    private String canonicalUrl;
    @SerializedName("categories")
    @Expose
    @Nullable
    private List<Category> categories = new ArrayList<Category>();
    @SerializedName("verified")
    @Expose
    @Nullable
    private Boolean verified;
    @SerializedName("url")
    @Expose
    @Nullable
    private String url;
    @SerializedName("hasMenu")
    @Expose
    @Nullable
    private Boolean hasMenu;
    @SerializedName("like")
    @Expose
    @Nullable
    private Boolean like;
    @SerializedName("dislike")
    @Expose
    @Nullable
    private Boolean dislike;
    @SerializedName("ok")
    @Expose
    @Nullable
    private Boolean ok;
    @SerializedName("rating")
    @Expose
    @Nullable
    private Double rating;
    @SerializedName("ratingColor")
    @Expose
    @Nullable
    private String ratingColor;
    @SerializedName("ratingSignals")
    @Expose
    @Nullable
    private Integer ratingSignals;
    @SerializedName("allowMenuUrlEdit")
    @Expose
    @Nullable
    private Boolean allowMenuUrlEdit;
    @SerializedName("createdAt")
    @Expose
    @Nullable
    private Integer createdAt;
    @SerializedName("tags")
    @Expose
    @Nullable
    private List<String> tags = new ArrayList<String>();
    @SerializedName("shortUrl")
    @Expose
    @Nullable
    private String shortUrl;
    @SerializedName("timeZone")
    @Expose
    @Nullable
    private String timeZone;
    @SerializedName("hours")
    @Expose
    @Nullable
    private Hours hours;
    @SerializedName("bestPhoto")
    @Expose
    @Nullable
    private BestPhoto bestPhoto;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * @param contact The contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * @return The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return The canonicalUrl
     */
    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    /**
     * @param canonicalUrl The canonicalUrl
     */
    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    /**
     * @return The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * @return The verified
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * @param verified The verified
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The hasMenu
     */
    public Boolean getHasMenu() {
        return hasMenu;
    }

    /**
     * @param hasMenu The hasMenu
     */
    public void setHasMenu(Boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    /**
     * @return The like
     */
    public Boolean getLike() {
        return like;
    }

    /**
     * @param like The like
     */
    public void setLike(Boolean like) {
        this.like = like;
    }

    /**
     * @return The dislike
     */
    public Boolean getDislike() {
        return dislike;
    }

    /**
     * @param dislike The dislike
     */
    public void setDislike(Boolean dislike) {
        this.dislike = dislike;
    }

    /**
     * @return The ok
     */
    public Boolean getOk() {
        return ok;
    }

    /**
     * @param ok The ok
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    /**
     * @return The rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * @return The ratingColor
     */
    public String getRatingColor() {
        return ratingColor;
    }

    /**
     * @param ratingColor The ratingColor
     */
    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    /**
     * @return The ratingSignals
     */
    public Integer getRatingSignals() {
        return ratingSignals;
    }

    /**
     * @param ratingSignals The ratingSignals
     */
    public void setRatingSignals(Integer ratingSignals) {
        this.ratingSignals = ratingSignals;
    }

    /**
     * @return The allowMenuUrlEdit
     */
    public Boolean getAllowMenuUrlEdit() {
        return allowMenuUrlEdit;
    }

    /**
     * @param allowMenuUrlEdit The allowMenuUrlEdit
     */
    public void setAllowMenuUrlEdit(Boolean allowMenuUrlEdit) {
        this.allowMenuUrlEdit = allowMenuUrlEdit;
    }

    /**
     * @return The createdAt
     */
    public Integer getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The createdAt
     */
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return The shortUrl
     */
    public String getShortUrl() {
        return shortUrl;
    }

    /**
     * @param shortUrl The shortUrl
     */
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    /**
     * @return The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone The timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return The hours
     */
    public Hours getHours() {
        return hours;
    }

    /**
     * @param hours The hours
     */
    public void setHours(Hours hours) {
        this.hours = hours;
    }

    /**
     * @return The bestPhoto
     */
    public BestPhoto getBestPhoto() {
        return bestPhoto;
    }

    /**
     * @param bestPhoto The bestPhoto
     */
    public void setBestPhoto(BestPhoto bestPhoto) {
        this.bestPhoto = bestPhoto;
    }
}