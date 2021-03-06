package com.molaja.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

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
        //FontsOverride.setDefaultFont(this, "SERIF", "fonts/roboto/Roboto-Medium.ttf");
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "MyFontAsset2.ttf");
        //FontsOverride.setDefaultFont(this, "SERIF", "MyFontAsset3.ttf");
        //FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/roboto/Roboto-Light.ttf");
    }

    public static SharedPreferences getLoginCookies(Context context) {
        return context.getSharedPreferences(login_cookies, Context.MODE_PRIVATE);
    }

    /**
     * Modifies the login status to the desired value.
     * @param context the context used to edit the shared preferences.
     * @param loggedIn the login status value to be stored.
     */
    public static void changeLoginStatus(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getLoginCookies(context).edit();
        editor.putBoolean(is_logged_in, loggedIn);
        editor.apply();
    }

    /**
     * Hides the status bar (for fullscreen purposes).
     * @param activity The currently active activity.
     */
    public static void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = activity.getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void makeContentAppearBehindStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public static boolean isLoggedIn(Context context) {
        return getLoginCookies(context).getBoolean(is_logged_in, false);
    }

    public static void showKeyboard(Context context, View bindedView, boolean show){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (show)
            imm.showSoftInput(bindedView, 0);
        else
            imm.hideSoftInputFromWindow(bindedView.getWindowToken(), 0);
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int darkenColor (int color, float percentage) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1 - percentage; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

}
