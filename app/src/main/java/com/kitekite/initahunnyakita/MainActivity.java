package com.kitekite.initahunnyakita;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kitekite.initahunnyakita.fragment.HangoutFragment;
import com.kitekite.initahunnyakita.fragment.ProfileFragment;
import com.kitekite.initahunnyakita.widget.RoundedImageView;
import com.squareup.picasso.Picasso;


public class MainActivity extends ActionBarActivity {
    public static final int HANG_OUT = 1;
    public static final int DISCOVER = 2;
    public static final int MY_SHOP = 3;
    public static final int MY_BAG = 4;
    public static MainActivity mainActivity;
    public int [] iconPos = new int[2];
    public int [] iconDest = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        initActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new HangoutFragment())
                    .commit();
        }

    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }



    public void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        //mActionBar.setBackgroundDrawable(new ColorDrawable(R.color.DarkRed));
        LayoutInflater mInflater = LayoutInflater.from(this);

        LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mActionBar.setCustomView(mCustomView, lp);

    }

    private void selectItem(int position) {
        Bundle args;
        FragmentManager fragmentManager;
        switch(position){

        }
    }

    @Override
    public void onBackPressed(){
        int backstackCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backstackCount>0){
            getSupportFragmentManager().popBackStack();
            if(backstackCount==1)
                setActionBarDefault();
        }
        else
            super.onBackPressed();
    }

    public void setActionBarDefault(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.getCustomView().setBackgroundColor(getResources().getColor(R.color.DarkRed));
        actionBar.getCustomView().findViewById(R.id.app_logo).setVisibility(View.VISIBLE);
        actionBar.getCustomView().findViewById(R.id.page_type_title).setVisibility(View.VISIBLE);
    }

    public void doTranslateAnimation(String imgUrl){
        Log.d("taikodok","kepanggil kok");
        final RoundedImageView duplicateImg = new RoundedImageView(this);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.profile_pic_size);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(iconSize,iconSize);
        //lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.leftMargin = iconPos[0];
        lp.topMargin = iconPos[1];
        Picasso.with(this)
                .load(imgUrl)
                .into(duplicateImg);
        //duplicateImg.setImageResource(R.drawable.ensa_shop);
        final RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root_view);
        rootView.addView(duplicateImg,lp);
        //rootView.setBackgroundColor(getResources().getColor(R.color.Bisque));
        duplicateImg.bringToFront();

        //execute animation

        int headerLogoSize = getResources().getDimensionPixelSize(R.dimen.profile_header_logo_size);
        float ratio = ((float) headerLogoSize/iconSize);
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(false);
        animSet.setDuration(600);
        TranslateAnimation translateAnim = new TranslateAnimation(0, ((float)(iconDest[0] - iconPos[0])/ratio), 0, ((float)(iconDest[1] - iconPos[1])/ratio));
        animSet.addAnimation(translateAnim);
        ScaleAnimation scaleAnim = new ScaleAnimation(1f,ratio,1f,ratio, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        animSet.addAnimation(scaleAnim);
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("PROFILE");
                if(profileFragment!=null){
                    profileFragment.mHeaderLogo.setVisibility(View.VISIBLE);
                }
                rootView.removeView(duplicateImg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //anim.setDuration(500);
        //anim.setFillAfter(true);
        duplicateImg.startAnimation(animSet);
    }

}
