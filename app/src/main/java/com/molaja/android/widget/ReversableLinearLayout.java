package com.molaja.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by florianhidayat on 1/7/15.
 */
public class ReversableLinearLayout extends LinearLayout {
    private boolean isReversed = false;

    public ReversableLinearLayout(Context context) {
        super(context);
    }

    public ReversableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReversableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void reverse() {
        int childCount = getChildCount();
        for (int i=1; i<=childCount; i++) {
            View child = getChildAt(0);
            removeViewAt(0);
            addView(child, childCount - i);
        }

        setReversed(!isReversed);
    }

    private void setReversed(boolean b) {
        isReversed = b;
    }

    public boolean isReversed() {
        return isReversed;
    }

}
