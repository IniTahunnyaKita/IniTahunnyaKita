package com.kitekite.initahunnyakita.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.edmodo.cropper.CropImageView;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.kitekite.initahunnyakita.R;

import java.io.IOException;

/**
 * Created by florianhidayat on 12/4/15.
 */
public class UploadImageActivity extends ActionBarActivity implements View.OnClickListener {
    final int ANIMATION_DELAY = 1000;
    View cancelBtn, rotateLeftBtn, rotateRightBtn, finishBtn;
    CropImageView cropImageView;
    OnSpringFinished onSpringFinished;

    private interface OnSpringFinished {
        public void onSpringFinished(View v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_upload_image);

        bindViews();

        //resize cropimageview
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        cropImageView.getLayoutParams().height = displayMetrics.widthPixels;

        try {
            if(getIntent().getParcelableExtra("URI") != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), (android.net.Uri) getIntent().getParcelableExtra("URI"));
                cropImageView.setImageBitmap(bitmap);
            } else {
                cropImageView.setImageBitmap((Bitmap) getIntent().getExtras().get("data"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        onSpringFinished = new OnSpringFinished() {
            @Override
            public void onSpringFinished(View v) {
                switch (v.getId()) {
                    case R.id.cancel_btn:
                        playAnimation(rotateLeftBtn);
                        break;
                    case R.id.rotate_left_btn:
                        playAnimation(rotateRightBtn);
                        break;
                    case R.id.rotate_right_btn:
                        playAnimation(finishBtn);
                        break;
                }
            }
        };

        initAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playAnimation(cancelBtn);
            }
        }, ANIMATION_DELAY);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.rotate_left_btn:
                cropImageView.rotateImage(-90);
                break;
            case R.id.rotate_right_btn:
                cropImageView.rotateImage(90);
                break;
            case R.id.finish_btn:
                cropImageView.setImageBitmap(cropImageView.getCroppedImage());
                break;
        }
    }

    private void initAnimation() {
        cancelBtn.setScaleX(0);
        cancelBtn.setScaleY(0);
        rotateLeftBtn.setScaleX(0);
        rotateLeftBtn.setScaleY(0);
        rotateRightBtn.setScaleX(0);
        rotateRightBtn.setScaleY(0);
        finishBtn.setScaleX(0);
        finishBtn.setScaleY(0);
    }

    private void bindViews() {
        cancelBtn = findViewById(R.id.cancel_btn);
        rotateLeftBtn = findViewById(R.id.rotate_left_btn);
        rotateRightBtn = findViewById(R.id.rotate_right_btn);
        finishBtn = findViewById(R.id.finish_btn);
        cropImageView = (CropImageView) findViewById(R.id.crop_image_view);

        cancelBtn.setOnClickListener(this);
        rotateLeftBtn.setOnClickListener(this);
        rotateRightBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
    }

    private void playAnimation(final View v) {
        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.addListener(new SimpleSpringListener() {
            boolean proceed = false;

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                value = value * value * value;

                if(!proceed && value > 0.5f) {
                    proceed = true;
                    onSpringFinished.onSpringFinished(v);
                }

                v.setScaleX(value);
                v.setScaleY(value);
            }
        });
        spring.setEndValue(1);

    }
}
