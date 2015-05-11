package com.molaja.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.molaja.android.R;

/**
 * Created by florianhidayat on 3/5/15.
 */
public class BuddyButton extends LinearLayout{

    public BuddyButton(Context context) {
        super(context);
        init(context);
    }

    public BuddyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BuddyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.layout_buddies_btn, this);
    }
}
