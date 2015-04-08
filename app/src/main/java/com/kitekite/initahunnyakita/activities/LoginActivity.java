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
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.LoginData;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.PanningViewAttacher;
import com.kitekite.initahunnyakita.widget.CustomTextView;
import com.kitekite.initahunnyakita.widget.PanningView;
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
                    new LoginTask().execute();
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
        editor.putString(Global.first_name,loginData.first_name);
        editor.putString(Global.last_name,loginData.last_name);
        editor.putString(Global.email,loginData.email);
        editor.putString(Global.photo_url,loginData.photo_url);
        editor.commit();
    }

    private class LoginTask extends AsyncTask<Void,Void,Void>{
        ProgressDialog pDialog;
        LoginData loginData;
        String strResponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging you in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.123.120/SocialNetworkProject/index.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("user_login", usernameBox.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("password_login", passwordBox.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("login","Sign in"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity e = response.getEntity();
                strResponse = EntityUtils.toString(e);
                Gson gson = new Gson();
                loginData = gson.fromJson(strResponse,LoginData.class);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            if(loginData!=null){
                saveLoginData(loginData);SharedPreferences.Editor editor = getSharedPreferences(Global.login_cookies, 0).edit();
                editor.putBoolean(Global.is_logged_in, true);
                editor.commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }
    }
	
}
