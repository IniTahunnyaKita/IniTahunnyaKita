package com.molaja.android.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.molaja.android.MolajaApplication;

/**
 * Created by florianhidayat on 18/4/15.
 */
public class User {
    public String id;
    public String username;
    public String name;
    public String email;
    public String authentication_token;
    public String image;

    public static User getCurrentUser(Context context) {
        SharedPreferences cookie = context.getSharedPreferences(MolajaApplication.login_cookies, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        return gson.fromJson(cookie.getString("user",""), User.class);
    }

    public void save(Context context) {
        Gson gson = new Gson();
        String user = gson.toJson(this);

        //save to shared prefs
        SharedPreferences.Editor editor = MolajaApplication.getLoginCookies(context).edit();
        editor.putString("user",user);
        editor.commit();
    }

}
