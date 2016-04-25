package com.androidtitan.hotspots.main.data.helper;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by amohnacs on 4/21/16.
 */
public class FirebaseHelper {

    public static final String FIREBASE_URL = "https://androidtitanhotspots.firebaseio.com";

    //todo: let's try this as our observable
    //todo: Firebase wrapped in RxJava
    //  http://andydyer.org/blog/2015/04/06/loading-a-list-of-objects-from-firebase/
    //  https://gist.github.com/gsoltis/86210e3259dcc6998801
   /* public DataSnapshot getUserInformation(String mUserId) {

        Firebase fb = new Firebase(Constants.FIREBASE_URL + "/users/" + mUserId + "/email");


        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Timber.d(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }*/
}
