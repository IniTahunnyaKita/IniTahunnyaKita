package com.molaja.android.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class CustomTextView extends TextView {

    //public static final String ROBOTO_REGULAR = "Roboto-Regular.ttf"
    public enum Roboto {
        LIGHT, MEDIUM, REGULAR, THIN
    }

    private static Map<String, Typeface> typefaceMap = new HashMap<>();

    String ttfName;

    String TAG = getClass().getName();

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.i(TAG, attrs.getAttributeName(i));
            /*
             * Read value of custom attributes
             */

            this.ttfName = attrs.getAttributeValue(
                    "http://schemas.android.com/apk/res-auto", "ttf_name");
            //Log.i(TAG, "firstText " + firstText);
            // Log.i(TAG, "lastText "+ lastText);

            init(context);
        }

    }

    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + ttfName + ".ttf");
        setTypeface(font);
    }

    private Typeface getTypeface(String fontName) {
        Typeface typeface = typefaceMap.get(fontName);

        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName + ".ttf");

            //store in map
            typefaceMap.put(fontName, typeface);
        }

        return typeface;

    }

    /**
     * set typeface using the desired roboto font.
     * @param robotoType the desired roboto font. (Light, Medium, Regular, and Thin)
     */
    public void setTypeface(Roboto robotoType) {
        switch (robotoType) {
            case LIGHT:
                setTypeface(getTypeface("roboto/Roboto-Light"));
                break;
            case MEDIUM:
                setTypeface(getTypeface("roboto/Roboto-Medium"));
                break;
            case REGULAR:
                setTypeface(getTypeface("roboto/Roboto-Regular"));
                break;
            case THIN:
                setTypeface(getTypeface("roboto/Roboto-Thin"));
                break;
        }
    }
}