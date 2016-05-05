package com.androidtitan.spotscore.main.web.deserializers;

import com.androidtitan.spotscore.main.data.Venue;
import com.androidtitan.spotscore.main.data.VenueResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by amohnacs on 5/4/16.
 */
public class ResponseDeserializer implements JsonDeserializer<VenueResponse> {
    private final String TAG = getClass().getSimpleName();


    @Override
    public VenueResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        VenueResponse venueResponse = new VenueResponse();
        JsonObject obj = json.getAsJsonObject().getAsJsonObject("response");

        venueResponse.setConfident(Boolean.valueOf(obj.get("confident").toString()));

        Venue[] venueObj = context.deserialize(obj.getAsJsonObject().get("venues"), Venue[].class);
        venueResponse.setVenues(Arrays.asList(venueObj));


        return venueResponse;

    }
}
