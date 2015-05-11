package com.molaja.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.activities.LoginActivity;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.model.User;

/**
 * Created by florianhidayat on 11/4/15.
 */
public class BackendHelper {
    private final static String LOGIN_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/sessions.json";
    private static final String SEARCH_USER_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/search_user.json";
    private static final String PROFILE_FETCHER_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/profile_fetcher.json";
    private static final String ADD_BUDDY_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/relationships.json";
    private static final String GET_PENDING_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/get_pending.json";
    private final static String TAG = BackendHelper.class.getSimpleName();

    public static void login(final Context context, String username, String password) {
        clearIonCookies(context);

        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        user.addProperty("password", password);
        json.add("user", user);

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
                            if (success && !isTokenNil(result)) {
                                Gson gson = new Gson();
                                User user = gson.fromJson(result.get("data"), User.class);
                                user.save(context);
                                MolajaApplication.changeLoginStatus(context, true);

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
        Gson gson = new Gson();

        JsonObject json = new JsonObject();
        json.add("user", new JsonObject().getAsJsonObject(gson.toJson(User.getCurrentUser(context))));

        //broadcast this to main activity
        Intent intent = new Intent(MainActivity.SHOW_DIALOG);
        intent.putExtra("MESSAGE",context.getString(R.string.log_out));
        intent.putExtra("INDETERMINATE", true);
        intent.putExtra("CANCELABLE", false);
        context.sendBroadcast(intent);

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
                        context.sendBroadcast(new Intent(MainActivity.DISMISS_DIALOG));

                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, result.toString());
                            boolean success = Boolean.parseBoolean(result.get("success").getAsString());
                            if (success) {
                                //clear own shared prefs
                                SharedPreferences.Editor editor = MolajaApplication.getLoginCookies(context).edit();
                                editor.clear();
                                editor.commit();

                                Intent loginIntent = new Intent(context, LoginActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(loginIntent);
                                if (context instanceof Activity)
                                    ((Activity) context).finish();
                            } else {
                                Toast.makeText(context, context.getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public static void searchForUser(Context context, String query, int page, FutureCallback<JsonObject> callback) {
        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("authentication_token", User.getCurrentUser(context).authentication_token);
        json.add("user", user);
        json.addProperty("queried_name", query);
        json.addProperty("page", page);

        Ion.with(context)
                .load("POST", SEARCH_USER_API_ENDPOINT_URL)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(callback);
    }

    /**
     * fetch a user's profile.
     * @param context Context to be passed to Ion's static function.
     * @param username Username of this user.
     * @param callback Callback to handle the response.
     */
    public static void getProfile(Context context, String username, FutureCallback<JsonObject> callback) {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);

        Ion.with(context)
                .load("POST", PROFILE_FETCHER_API_ENDPOINT_URL)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(callback);

    }

    public static void addBuddy(Context context, String id) {
        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("authentication_token", User.getCurrentUser(context).authentication_token);
        json.add("user", user);
        json.addProperty("other_id", id);

        Ion.with(context)
                .load("POST", ADD_BUDDY_API_ENDPOINT_URL)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject();
    }

    public static void getPendingRequests(Context context, int page, FutureCallback<JsonObject> callback) {
        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("authentication_token", User.getCurrentUser(context).authentication_token);
        json.add("user", user);
        json.addProperty("page", page);

        Ion.with(context)
                .load("POST", GET_PENDING_API_ENDPOINT_URL)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(callback);
    }

    public static boolean isTokenNil(JsonObject json) {
        return json.getAsJsonObject("data").get("authentication_token") instanceof JsonNull;
    }

    public static void clearIonCookies(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MolajaApplication.ion_cookies, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
    
}
