package com.molaja.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.koushikdutta.ion.Ion;
import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.model.User;
import com.molaja.android.util.BackendHelper;
import com.molaja.android.util.PanningViewAttacher;
import com.molaja.android.widget.CustomTextView;
import com.molaja.android.widget.PanningView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Callback;

public class LoginActivity extends AppCompatActivity {
    public CustomTextView appLogo;
    ShimmerTextView logInTv;
    EditText usernameBox;
    EditText passwordBox;

    Shimmer shimmer;

    public Animation fadeIn;
    Animation fadeOut;
    Animation logoAnimation;
    Animation slideUp;
    Animation slideDown;

    String [] drawablePaths;
    int [] drawables;
    int backgroundIndex;
    Callback backgroundCallback;
    ViewSwitcher viewSwitcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        Ion.getDefault(this).configure().setLogging("IonLogs", Log.DEBUG);

        setupAnimations();

        findViewById(R.id.form_container).setVisibility(View.GONE);

		appLogo = (CustomTextView) findViewById(R.id.app_logo);
        logInTv = (ShimmerTextView) findViewById(R.id.log_in_text);
		appLogo.startAnimation(logoAnimation);
		
		usernameBox = (EditText) findViewById(R.id.usernameBox);
		passwordBox = (EditText) findViewById(R.id.passwordBox);
		
		Button loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (usernameBox.getText().toString().contentEquals("admin") && passwordBox.getText().toString().contentEquals("admin")) {
                    MolajaApplication.changeLoginStatus(LoginActivity.this, true);
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
				}
                else if (usernameBox.getText().toString().isEmpty() || passwordBox.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this,R.string.login_form_empty, Toast.LENGTH_SHORT).show();
                } else {
                    startShimmerAnimation();
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

    }

    private void setupAnimations() {
        logoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.app_logo_anim_start);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.app_logo_anim_slide_down);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.app_logo_anim_slide_up);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        logoAnimation.setStartOffset(1000);

        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeIn(findViewById(R.id.form_container));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        logoAnimation.setAnimationListener(listener);
        slideUp.setAnimationListener(listener);

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeOut(findViewById(R.id.form_container));
                BackendHelper.login(LoginActivity.this, usernameBox.getText().toString(), passwordBox.getText().toString());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void doAfterLoginAnimation() {
        final View welcomeBg = findViewById(R.id.welcome_bg);
        TextView welcomeText = (TextView) findViewById(R.id.welcome_user);
        welcomeText.setText(User.getCurrentUser(this).name);
        fadeIn(findViewById(R.id.welcome_layout));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this,
                                Pair.create(welcomeBg, getString(R.string.actionbar_background_transition)),
                                Pair.create((View) appLogo, getString(R.string.app_logo_transition))
                        );
                ActivityCompat.startActivity(LoginActivity.this, new Intent(LoginActivity.this, MainActivity.class),
                        options.toBundle());
                finish();
            }
        },2000);
    }

    public void fadeIn(final View v) {
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(fadeIn);
    }

    public void fadeOut(final View v) {
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(fadeOut);
    }

    public void startShimmerAnimation() {
        appLogo.startAnimation(slideDown);
        logInTv.setVisibility(View.VISIBLE);
        fadeIn(logInTv);
        shimmer = new Shimmer();
        shimmer.start(logInTv);
    }

    public void stopShimmerAnimation() {
        appLogo.startAnimation(slideUp);
        fadeOut(logInTv);
        shimmer.cancel();
    }
}
