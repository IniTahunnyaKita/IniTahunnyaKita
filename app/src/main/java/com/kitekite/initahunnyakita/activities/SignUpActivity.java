package com.kitekite.initahunnyakita.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.kitekite.initahunnyakita.R;

/**
 * Created by florianhidayat on 9/4/15.
 */
public class SignUpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
    }
}
