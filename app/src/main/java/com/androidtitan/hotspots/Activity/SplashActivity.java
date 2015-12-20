package com.androidtitan.hotspots.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidtitan.hotspots.R;

public class SplashActivity extends Activity {
    private static final String TAG = "hotspots";

    TextView titleTextView;
    //Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        titleTextView = (TextView) findViewById(R.id.splashTitle);
        //customFont = Typeface.createFromAsset(getAssets(), "gangbangfont.ttf");
        //titleTextView.setTypeface(customFont);


        try{
            splashScreen();

        } catch (Exception e) {
            //todo: we could display a picture here as an alternative
            Log.e(TAG, String.valueOf(e));
        }

        //this returns
        if(titleTextView.isShown()) {
            Log.e(TAG, "titleTextView.isShown()");
        }
        else {
            Log.e(TAG, "NOT SHOWN");
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public void splashScreen() {
        VideoView videoHolder = (VideoView) findViewById(R.id.splashVideo);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.splash);
        videoHolder.setVideoURI(video);
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                jumpMain(); //jump to the next Activity
            }
        });


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        videoHolder.setLayoutParams(new FrameLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels));

        videoHolder.start();

        videoHolder.setOnTouchListener(new View.OnTouchListener() {

            @Override
             public boolean onTouch(View v, MotionEvent event) {
                ((VideoView) v).stopPlayback();
                jumpMain();
                return true;
            }
        });
    }

    private synchronized void jumpMain() {
        /*Intent intent = new Intent(SplashActivity.this, ChampionActivity.class);
        startActivity(intent);
        finish();*/
    }

}