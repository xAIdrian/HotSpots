package com.androidtitan.spotscore.main.web.deserializers;

import com.androidtitan.spotscore.main.data.foursquare.BestPhoto;
import com.androidtitan.spotscore.main.data.foursquare.Category;
import com.androidtitan.spotscore.main.data.foursquare.Contact;
import com.androidtitan.spotscore.main.data.foursquare.Hours;
import com.androidtitan.spotscore.main.data.foursquare.Location;
import com.androidtitan.spotscore.main.data.foursquare.Venue;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by amohnacs on 5/5/16.
 */
public class VenueDeserializer implements JsonDeserializer<Venue> {
    private final String TAG = getClass().getSimpleName();

    @Override
    public Venue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Venue tempVenue = new Venue();

        JsonObject obj = json.getAsJsonObject();

        tempVenue.setId(obj.get("id").getAsString());
        tempVenue.setName(obj.get("name").getAsString());

        tempVenue.setContact(context.deserialize(obj.get("contact"), Contact.class));
        tempVenue.setLocation(context.deserialize(obj.get("location"), Location.class));

        tempVenue.setCanonicalUrl(obj.has("canonicalUrl")  ?
                obj.get("canonicalUrl").getAsString() : "");
        tempVenue.setCategories(Arrays.asList(context.deserialize(obj.get("categories"), Category[].class)));
        tempVenue.setVerified(obj.has("verified") ?
                obj.get("verified").getAsBoolean() : false);
        tempVenue.setUrl(obj.has("url") ?
                obj.get("url").getAsString() : "");
        tempVenue.setHasMenu(obj.has("hasMenu") ?
                obj.get("hasMenu").getAsBoolean() : false);

        tempVenue.setLike(obj.has("like") ?
                obj.get("like").getAsBoolean() : false);
        tempVenue.setDislike(obj.has("dislike") ?
                obj.get("dislike").getAsBoolean() : false);
        tempVenue.setOk(obj.has("ok") ?
                obj.get("ok").getAsBoolean() : false);
        tempVenue.setRating(obj.has("rating") ?
                obj.get("rating").getAsDouble() : 0.0);
        tempVenue.setRatingColor(obj.has("ratingColor") ?
                obj.get("ratingColor").getAsString() : "");
        tempVenue.setRatingSignals(obj.has("ratingSignals") ?
                obj.get("ratingSignals").getAsInt() : 0);

        tempVenue.setAllowMenuUrlEdit(obj.has("allowMenuUrlEdit") ?
                obj.get("allowMenuUrlEdit").getAsBoolean() : false);
        tempVenue.setCreatedAt(obj.has("createdAt") ?
                obj.get("createdAt").getAsInt() : 0);

        ArrayList<String> mTagArrayList = new ArrayList<String>();
        if(obj.has("tags")) { //not an instanceof
            JsonArray mJsonTagArray = obj.get("tags").getAsJsonArray();
            for (JsonElement e : mJsonTagArray) {
                mTagArrayList.add(e instanceof JsonNull ? e.getAsString() : "");
            }
            tempVenue.setTags(mTagArrayList);
        } else {
            mTagArrayList.add("");
            tempVenue.setTags(mTagArrayList);
        }

        tempVenue.setShortUrl(obj.has("shortUrl") ?
                obj.get("shortUrl").getAsString() : "");
        tempVenue.setTimeZone(obj.has("timeZone") ?
                obj.get("timeZone").getAsString() : "");
        tempVenue.setHours(context.deserialize(obj.get("hours"), Hours.class));

        tempVenue.setBestPhoto(context.deserialize(obj.get("bestPhoto"), BestPhoto.class));

        return tempVenue;
    }
}
