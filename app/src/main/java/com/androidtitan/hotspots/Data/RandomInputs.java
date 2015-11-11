package com.androidtitan.hotspots.Data;

import java.util.Random;

/**
 * Created by amohnacs on 11/9/15.
 */
public class RandomInputs {

    //This is used for the random assignment of
    public String randomStringArray[] = { "BackBar", "Brick and Mortar", "Tres Gatos", "Highland Kitchen",
            "Trina's Starlite Lounge", "Deep Ellum", "Adubon Circle", "Friendly Toast", "Charlie's Kitchen",
            "Memphis Taproom", "Johnny Brenda's", "Ortlieb’s Lounge", "El Bar", "Bob and Barbaras",
            "Cantina Los Caballitos", "Khyber Pass Pub", "The Barbary", "Barcade", "Guru's Cafe",
            "The Black Sheep", "Sammy's", "Station 22", "Provo Food Truck Roundup", "Muse Music Cafe" };

    public String getRandomStringInput() {
        return randomStringArray[randInt(0, randomStringArray.length - 1)];
    }

    //generates a random integer
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

}
