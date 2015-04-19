package com.molaja.android.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextView extends TextView {

    Context context;
    String ttfName;

    String TAG = getClass().getName();

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.i(TAG, attrs.getAttributeName(i));
            /*
             * Read value of custom attributes
             */

            this.ttfName = attrs.getAttributeValue(
                    "http://schemas.android.com/apk/res-auto", "ttf_name");
            //Log.i(TAG, "firstText " + firstText);
            // Log.i(TAG, "lastText "+ lastText);

            init();
        }

    }

    private void init() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/"+ttfName);
        setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf) {

        // TODO Auto-generated method stub
        super.setTypeface(tf);
    }
}