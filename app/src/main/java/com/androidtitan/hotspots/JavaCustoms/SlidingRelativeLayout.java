package com.androidtitan.hotspots.JavaCustoms;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by amohnacs on 9/6/15.
 */
public class SlidingRelativeLayout extends RelativeLayout {
    public SlidingRelativeLayout(Context context) {
        super(context);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setYFraction(final float fraction ) {
        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public float getYFraction () {
        if(getHeight() == 0) {
            return 0;
        }
        else {
            return getTranslationY() / getHeight();
        }
    }
}
