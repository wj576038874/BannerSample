package com.joyrun.banner.viewpager;


import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * author: wenjie
 * date: 2019-12-19 14:21
 * descption:
 */
public class BannerScroller extends Scroller {

    //值越大，滑动越慢
    private int mDuration;

    BannerScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }


    public BannerScroller(Context context, Interpolator interpolator, int mDuration) {
        super(context, interpolator);
        this.mDuration = mDuration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
}