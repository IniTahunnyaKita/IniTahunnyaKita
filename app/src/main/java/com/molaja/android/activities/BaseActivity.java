package com.molaja.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by florianhidayat on 29/6/15.
 */
public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * get the specified color based on id
     * @param colorId the id of the color to look for.
     * @return color value in form of 0xAARRGGBB.
     */
    public int getColor(int colorId) {
        return getResources().getColor(colorId);
    }


}
