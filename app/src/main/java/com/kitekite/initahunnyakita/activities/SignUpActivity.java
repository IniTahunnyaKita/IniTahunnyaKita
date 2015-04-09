package com.kitekite.initahunnyakita.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.facebook.FacebookSdk;
import com.grantlandchew.view.VerticalPager;
import com.kitekite.initahunnyakita.R;

/**
 * Created by florianhidayat on 9/4/15.
 */
public class SignUpActivity extends ActionBarActivity {
    View emailSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        bindViews();
    }


    private void bindViews() {
        emailSignupBtn = findViewById(R.id.email_signup_btn);
        emailSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VerticalPager)findViewById(R.id.main_vertical_pager)).snapToPage(1);
            }
        });
    }
}
