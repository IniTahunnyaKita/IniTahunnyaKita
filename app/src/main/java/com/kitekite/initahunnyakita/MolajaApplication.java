package com.kitekite.initahunnyakita;

import android.app.Application;

import com.kitekite.initahunnyakita.util.FontsOverride;

/**
 * Created by Florian on 3/10/2015.
 */
public class MolajaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //override default fonts
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/roboto/Roboto-Medium.ttf");
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "MyFontAsset2.ttf");
        //FontsOverride.setDefaultFont(this, "SERIF", "MyFontAsset3.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/roboto/Roboto-Light.ttf");
    }

}
