package com.molaja.android.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.molaja.android.R;
import com.molaja.android.util.PicassoBlurRequestHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by florianhidayat on 12/4/15.
 */
public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    final int ANIMATION_DELAY = 1000;

    View cancelBtn, rotateLeftBtn, rotateRightBtn, finishBtn;
    ImageView blurredBg;
    CropImageView cropImageView;
    OnSpringFinished onSpringFinished;

    Uri imageUri;

    private interface OnSpringFinished {
        void onSpringFinished(View v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_image);

        imageUri = getIntent().getParcelableExtra("URI");

        bindViews();

        //resize cropimageview
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        cropImageView.getLayoutParams().height = displayMetrics.widthPixels;

        try {
            if(imageUri != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), imageUri);
                cropImageView.setImageBitmap(bitmap);
            } else {
                cropImageView.setImageBitmap((Bitmap) getIntent().getExtras().get("data"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBlurredBackground();

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

    private void setBlurredBackground() {
        Picasso picasso  = new Picasso.Builder(this).addRequestHandler(new PicassoBlurRequestHandler(this)).build();
        picasso.load(imageUri).fit().centerCrop().into(blurredBg, new Callback() {
            @Override
            public void onSuccess() {
                blurredBg.setColorFilter(Color.argb(180, 0, 0, 0));
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.rotate_left_btn:
                cropImageView.rotateImage(-90);
                break;
            case R.id.rotate_right_btn:
                cropImageView.rotateImage(90);
                break;
            case R.id.finish_btn:
                Intent intent = new Intent();
                try {
                    intent.putExtra("image_path",saveBitmapToFile(cropImageView.getCroppedImage()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
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
        blurredBg = (ImageView) findViewById(R.id.blurred_bg);
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

    private String saveBitmapToFile(Bitmap bitmap) throws IOException {
        String imageFileName = "profile_picture";
        File picturesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File storageDir = new File(picturesDir.getAbsolutePath() + "/Molaja");

        if (!storageDir.exists())
            storageDir.mkdir();

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        FileOutputStream fOut = new FileOutputStream(image);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
        fOut.flush();
        fOut.close();

        return image.getAbsolutePath();
    }
}
