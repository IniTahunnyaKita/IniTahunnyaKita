package com.kitekite.initahunnyakita.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.LoginActivity;
import com.kitekite.initahunnyakita.activities.MainActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by florianhidayat on 11/4/15.
 */
public class BackendHelper {
    private final static String LOGIN_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/sessions.json";
    private final static String TAG = BackendHelper.class.getSimpleName();
    
    public static void login(final Context context, String username, String password) {
        clearIonCookies(context);

        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        user.addProperty("password", password);
        json.add("user", user);

        /*final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage(context.getString(R.string.logging_you_in));
        pDialog.show();*/

        Ion.with(context)
                .load(LOGIN_API_ENDPOINT_URL)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e != null) {
                            e.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        } else if (result.has("error")) {
                            Toast.makeText(context, result.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                            //stop animation
                            if (context instanceof LoginActivity)
                                ((LoginActivity)context).stopShimmerAnimation();
                        } else {
                            Log.i(TAG, result.toString());
                            boolean success = result.get("success").getAsBoolean();
                            if (success) {
                                SharedPreferences.Editor editor = context.getSharedPreferences(Global.login_cookies, 0).edit();
                                editor.putBoolean(Global.is_logged_in, true);
                                editor.putString(Global.username, result.getAsJsonObject("data").get("username").getAsString());
                                editor.putString(Global.name, result.getAsJsonObject("data").get("name").getAsString());
                                try {
                                    editor.putString(Global.token, result.getAsJsonObject("data").get(Global.token).getAsString());
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();

                                    //stop animation
                                    if (context instanceof LoginActivity)
                                        ((LoginActivity)context).stopShimmerAnimation();
                                    return;
                                }
                                editor.apply();

                                if (context instanceof LoginActivity) {
                                    final LoginActivity loginActivity = (LoginActivity) context;
                                    loginActivity.doAfterLoginAnimation();
                                } else {
                                    context.startActivity(new Intent(context, MainActivity.class));
                                }
                            } else {
                                Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                //stop animation
                                if (context instanceof LoginActivity)
                                    ((LoginActivity)context).stopShimmerAnimation();
                            }
                        }
                    }
                });
    }

    public static void logOut(final Context context) {
        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("username", context.getSharedPreferences(Global.login_cookies, 0).getString(Global.username, ""));
        json.add("user", user);

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage(context.getString(R.string.log_out));
        pDialog.show();

        Ion.with(context)
                .load("DELETE", LOGIN_API_ENDPOINT_URL)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.dismiss();

                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, result.toString());
                            boolean success = Boolean.parseBoolean(result.get("success").getAsString());
                            if (success) {
                                //clear own shared prefs
                                SharedPreferences.Editor editor = context.getSharedPreferences(Global.login_cookies, Context.MODE_PRIVATE).edit();
                                editor.clear();
                                editor.commit();
                                context.startActivity(new Intent(context, LoginActivity.class));
                                if (context instanceof Activity)
                                    ((Activity) context).finish();
                            } else {
                                Toast.makeText(context, context.getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public static void clearIonCookies(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Global.ion_cookies, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
    
}
