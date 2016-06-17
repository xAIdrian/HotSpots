package com.androidtitan.spotscore.main.web.deserializers;

import com.androidtitan.spotscore.main.data.foursquare.DetailedVenueResponse;
import com.androidtitan.spotscore.main.data.foursquare.Venue;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by amohnacs on 5/5/16.
 */
public class DetailedResponseDeserializer implements JsonDeserializer<DetailedVenueResponse> {
    private final String TAG = getClass().getSimpleName();


    @Override
    public DetailedVenueResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        DetailedVenueResponse venueResponse = new DetailedVenueResponse();
        JsonObject obj = json.getAsJsonObject().getAsJsonObject("response");

        Venue venueObj = context.deserialize(obj.getAsJsonObject().get("venue"), Venue.class);
        venueResponse.setVenue(venueObj);


        return venueResponse;

    }
}
