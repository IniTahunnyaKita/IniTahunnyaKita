package com.molaja.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.molaja.android.util.FontsOverride;

/**
 * Created by Florian on 3/10/2015.
 */
public class MolajaApplication extends Application {
    public final static String login_cookies = "login_cookies";
    public final static String ion_cookies = "ion-cookies";
    public final static String is_logged_in = "is_logged_in";

    @Override
    public void onCreate() {
        super.onCreate();

        //override default fonts
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/roboto/Roboto-Medium.ttf");
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "MyFontAsset2.ttf");
        //FontsOverride.setDefaultFont(this, "SERIF", "MyFontAsset3.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/roboto/Roboto-Light.ttf");
    }

    public static SharedPreferences getLoginCookies(Context context) {
        return context.getSharedPreferences(login_cookies, Context.MODE_PRIVATE);
    }

    public static void changeLoginStatus(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getLoginCookies(context).edit();
        editor.putBoolean(is_logged_in, loggedIn);
        editor.commit();
    }

    public static boolean isLoggedIn(Context context) {
        return getLoginCookies(context).getBoolean(is_logged_in, false);
    }

}