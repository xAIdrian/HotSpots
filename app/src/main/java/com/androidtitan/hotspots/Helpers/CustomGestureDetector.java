package com.androidtitan.hotspots.Helpers;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by amohnacs on 7/25/15.
 */
public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.e("TAG", "simpletouch");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Log.e("TAG", "touch! touch!");

        if (e1.getX() < e2.getX()) {
            Log.d("customGestureDetector", "Left to Right swipe performed");
        }

        if (e1.getX() > e2.getX()) {
            Log.d("customGestureDetector", "Right to Left swipe performed");
        }

        if (e1.getY() < e2.getY()) {
            Log.d("customGestureDetector", "Up to Down swipe performed");
        }

        if (e1.getY() > e2.getY()) {
            Log.d("customGestureDetector", "Down to Up swipe performed");
        }
        return true;
    }
}
