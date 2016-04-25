/*
package com.androidtitan.hotspots.main.util;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.firebase.client.core.Constants;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class RxFirebase {

    */
/**
     * Essentially a struct so that we can pass all children events through as a single object.
     *//*

    public static class FirebaseChildEvent {
        public DataSnapshot snapshot;
        public Constants.EventType eventType;
        public String prevName;

        public FirebaseChildEvent(DataSnapshot snapshot, Constants.EventType eventType, String prevName) {
            this.snapshot = snapshot;
            this.eventType = eventType;
            this.prevName = prevName;
        }
    }

    public static Observable<FirebaseChildEvent> observeChildren(final Query ref) {
        return Observable.create(new Observable.OnSubscribe<FirebaseChildEvent>() {

            @Override
            public void call(final Subscriber<? super FirebaseChildEvent> subscriber) {
                final ChildEventListener listener = ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevName) {
                        subscriber.onNext(new FirebaseChildEvent(dataSnapshot, Constants.EventType.CHILD_ADDED, prevName));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String prevName) {
                        subscriber.onNext(new FirebaseChildEvent(dataSnapshot, Constants.EventType.CHILD_CHANGED, prevName));
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        subscriber.onNext(new FirebaseChildEvent(dataSnapshot, Constants.EventType.CHILD_REMOVED, null));
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String prevName) {
                        subscriber.onNext(new FirebaseChildEvent(dataSnapshot, Constants.EventType.CHILD_MOVED, prevName));
                    }

                    @Override
                    public void onCancelled(FirebaseError error) {
                        // Turn the FirebaseError into a throwable to conform to the API
                        subscriber.onError(new FirebaseException(error.getMessage()));
                    }
                });

                // When the subscription is cancelled, remove the listener
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        ref.removeEventListener(listener);
                    }
                }));
            }
        });
    }

    private static Func1<FirebaseChildEvent, Boolean> makeEventFilter(final Constants.EventType eventType) {
        return new Func1<FirebaseChildEvent, Boolean>() {

            @Override
            public Boolean call(FirebaseChildEvent firebaseChildEvent) {
                return firebaseChildEvent.eventType == eventType;
            }
        };
    }

    public static Observable<FirebaseChildEvent> observeChildAdded(Query ref) {
        return observeChildren(ref).filter(makeEventFilter(Constants.EventType.CHILD_ADDED));
    }

    public static Observable<FirebaseChildEvent> observeChildChanged(Query ref) {
        return observeChildren(ref).filter(makeEventFilter(Constants.EventType.CHILD_CHANGED));
    }

    public static Observable<FirebaseChildEvent> observeChildMoved(Query ref) {
        return observeChildren(ref).filter(makeEventFilter(Constants.EventType.CHILD_MOVED));
    }

    public static Observable<FirebaseChildEvent> observeChildRemoved(Query ref) {
        return observeChildren(ref).filter(makeEventFilter(Constants.EventType.CHILD_REMOVED));
    }

    public static Observable<DataSnapshot> observe(final Query ref) {

        return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {

            @Override
            public void call(final Subscriber<? super DataSnapshot> subscriber) {
                final ValueEventListener listener = ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subscriber.onNext(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(FirebaseError error) {
                        // Turn the FirebaseError into a throwable to conform to the API
                        subscriber.onError(new FirebaseException(error.getMessage()));
                    }
                });

                // When the subscription is cancelled, remove the listener
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        ref.removeEventListener(listener);
                    }
                }));
            }
        });
    }
}
*/
