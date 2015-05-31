package com.molaja.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by florianhidayat on 30/5/15.
 */
public class YScrollFrameLayout extends FrameLayout {

    // -----------------------------------------------------------------------
    //
    // Constructor
    //
    // -----------------------------------------------------------------------
    public YScrollFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    // -----------------------------------------------------------------------
    //
    // Fields
    //
    // -----------------------------------------------------------------------
    private GestureDetector mGestureDetector;
    private boolean mIsLockOnHorizontalAxis = false;
    private boolean mIsPagingEnabled = true;

    // -----------------------------------------------------------------------
    //
    // Methods
    //
    // -----------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // decide if horizontal axis is locked already or we need to check the scrolling direction
        if (!mIsLockOnHorizontalAxis) {
            mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event);
        }

        // release the lock when finger is up
        if (event.getAction() == MotionEvent.ACTION_UP)
            mIsLockOnHorizontalAxis = false;

        Log.d("YScroll","mIsLockOnHorizontalAxis:" + mIsLockOnHorizontalAxis);

        getParent().requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis);
        if (mIsLockOnHorizontalAxis)
            return false;

        return super.onTouchEvent(event);
    }

    // -----------------------------------------------------------------------
    //
    // Inner Classes
    //
    // -----------------------------------------------------------------------
    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {

        // -----------------------------------------------------------------------
        //
        // Methods
        //
        // -----------------------------------------------------------------------
        /**
         * @return true - if we're scrolling in X direction, false - in Y direction.
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) > Math.abs(distanceY);
        }

    }
}
