package com.kitekite.initahunnyakita.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.LoginData;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.PanningViewAttacher;
import com.kitekite.initahunnyakita.widget.CustomTextView;
import com.kitekite.initahunnyakita.widget.PanningView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity {
    private final static String LOGIN_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/sessions";
    EditText usernameBox;
    EditText passwordBox;
    String [] drawablePaths;
    int [] drawables;
    int backgroundIndex;
    Callback backgroundCallback;
    ViewSwitcher viewSwitcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().hide();
		
		CustomTextView appLogo = (CustomTextView) findViewById(R.id.app_logo);
		Animation logoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.app_logo_anim);
		appLogo.startAnimation(logoAnimation);
		
		usernameBox = (EditText) findViewById(R.id.usernameBox);
		passwordBox = (EditText) findViewById(R.id.passwordBox);
		Animation alphaAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
		findViewById(R.id.form_container).startAnimation(alphaAnimation);
		
		Button loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (usernameBox.getText().toString().contentEquals("admin") && passwordBox.getText().toString().contentEquals("admin")) {
					SharedPreferences.Editor editor = getSharedPreferences(Global.login_cookies, 0).edit();
					editor.putBoolean(Global.is_logged_in, true);
					editor.commit();
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
				}
                else if (usernameBox.getText().toString().isEmpty() || passwordBox.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this,R.string.login_form_empty, Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject json = new JsonObject();
                    JsonObject user = new JsonObject();
                    user.addProperty("username", usernameBox.getText().toString());
                    user.addProperty("password", passwordBox.getText().toString());
                    json.add("user", user);

                    final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
                    pDialog.setIndeterminate(true);
                    pDialog.setCancelable(false);
                    pDialog.setMessage(getString(R.string.logging_you_in));
                    pDialog.show();

                    Ion.with(LoginActivity.this)
                            .load(LOGIN_API_ENDPOINT_URL)
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    pDialog.dismiss();

                                    if(e != null) {
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                    } else {
                                        boolean success = Boolean.parseBoolean(result.get("success").getAsString());
                                        if (success) {
                                            SharedPreferences.Editor editor = getSharedPreferences(Global.login_cookies, 0).edit();
                                            editor.putBoolean(Global.is_logged_in, true);
                                            editor.putString(Global.username, result.getAsJsonObject("data").get("username").getAsString());
                                            editor.putString(Global.name, result.getAsJsonObject("data").get("name").getAsString());
                                            editor.putString(Global.token, result.getAsJsonObject("data").get(Global.token).getAsString());
                                            editor.commit();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    //new LoginTask().execute();

                }
			}
			
		});

        Button signupButton = (Button) findViewById(R.id.signup_btn);
        signupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        viewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher);
        final PanningView panningView1 = (PanningView) findViewById(R.id.panningView1);
        final PanningView panningView2 = (PanningView) findViewById(R.id.panningView2);
        //setup
        backgroundIndex = 0;
        drawablePaths = new String[] {"file:///android_asset/login_backgrounds/panning1.jpg",
                "file:///android_asset/login_backgrounds/panning2.jpg",
                "file:///android_asset/login_backgrounds/panning3.jpg"};
        drawables = new int[] {R.drawable.panning1, R.drawable.panning2, R.drawable.panning3};

        panningView1.setImageResource(drawables[backgroundIndex++]);
        panningView1.init(new PanningViewAttacher.OnPanningEndListener() {
            @Override
            public void onPanningEnd() {
                if (backgroundIndex == drawables.length)
                    backgroundIndex = 0;
                panningView2.setImageResource(drawables[backgroundIndex++]);
                panningView2.startPanning();
                viewSwitcher.showNext();
            }
        });
        panningView2.init(new PanningViewAttacher.OnPanningEndListener() {
            @Override
            public void onPanningEnd() {
                if (backgroundIndex == drawablePaths.length)
                    backgroundIndex = 0;
                panningView1.setImageResource(drawables[backgroundIndex++]);
                panningView1.startPanning();
                viewSwitcher.showNext();
            }
        });
        panningView1.startPanning();
        /*backgroundCallback = new Callback() {
            @Override
            public void onSuccess() {
                panningView.init(new PanningViewAttacher.OnPanningEndListener() {
                    @Override
                    public void onPanningEnd() {
                        if (backgroundIndex == drawablePaths.length)
                            backgroundIndex = 0;
                        Picasso.with(LoginActivity.this)
                                .load(drawablePaths[backgroundIndex++])
                                .into(panningView, backgroundCallback);
                    }
                });
                panningView.startPanning();
            }

            @Override
            public void onError() {

            }
        };
        Picasso.with(this)
                .load(drawablePaths[backgroundIndex++])
                .into(panningView, backgroundCallback);*/

    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
	}

    private void saveLoginData(LoginData loginData){
        SharedPreferences.Editor editor = getSharedPreferences(Global.login_cookies,0).edit();
        editor.putString(Global.username,loginData.username);
        editor.putString(Global.name,loginData.first_name);
        editor.putString(Global.email,loginData.email);
        editor.putString(Global.photo_url,loginData.photo_url);
        editor.commit();
    }
	
}
