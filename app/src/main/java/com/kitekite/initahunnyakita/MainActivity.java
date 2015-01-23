package com.kitekite.initahunnyakita;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.dobmob.dobsliding.DobSlidingMenu;
import com.dobmob.dobsliding.events.OnCollapsedListener;
import com.dobmob.dobsliding.events.OnExpandedListener;
import com.dobmob.dobsliding.exceptions.NoActionBarException;
import com.dobmob.dobsliding.models.SlidingItem;
import com.kitekite.initahunnyakita.fragment.FragmentTab;
import com.kitekite.initahunnyakita.fragment.HangoutFragment;
import com.kitekite.initahunnyakita.fragment.ProfileFragment;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.widget.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {
    public static final int HANG_OUT = 1;
    public static final int DISCOVER = 2;
    public static final int MY_SHOP = 3;
    public static final int MY_BAG = 4;

    public static final String TAB_1_TAG = "HANG_OUT";
    public static final String TAB_2_TAG = "DISCOVER";
    public static final String TAB_3_TAG = "TRENDING";//trending/stories?
    public static final String TAB_4_TAG = "THE BAG";

    private String TAG = "taikodok";
    public static MainActivity mainActivity;
    public int [] iconPos = new int[2];
    public int [] iconDest = new int[2];
    FragmentTabHost mTabHost;
    DobSlidingMenu vSlidingMenu;
    View slidingView;
    ImageView blurredBg;
    View content;
    int contentHeight;
    int contentWidth;
    LayoutInflater inflater;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;
    public Bitmap blurredBitmap;
    boolean isLoggedIn;
    private static boolean isExpanded = false;
    Drawable windowBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //go to login activity if user's not logged in
        isLoggedIn = getSharedPreferences(Global.login_cookies,0).getBoolean(Global.is_logged_in,false);
        if(!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mainActivity = this;

        initActionBar();

        initTabs();

        blurredBg = (ImageView)mainActivity.findViewById(R.id.blur_image);
        inflater = LayoutInflater.from(mainActivity);
        content = blurredBg.getRootView();

        if (savedInstanceState == null &&isLoggedIn ) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new HangoutFragment())
                    .commit();
        }
        if(whichFragmentIsShown()==HANG_OUT)
            showWatermark(R.drawable.hangout_actionbar_watermark,true);

        initNotification();
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
        LayoutInflater mInflater = LayoutInflater.from(this);

        LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mActionBar.setCustomView(mCustomView, lp);
        //set toggle for notification
        View.OnClickListener notificationToggle = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentHeight==0)
                    contentHeight = content.getMeasuredHeight();
                int [] pos = new int[2];
                slidingView.getLocationInWindow(pos);
                Log.d(TAG,"onexpand"+"xpos:" + pos[0]+"ypos:"+pos[1]);
                setBlurredBackground(contentHeight);
                vSlidingMenu.expand();
                //blurBackground(slidingView);
            }
        };
        mCustomView.findViewById(R.id.action_bar_title).setOnClickListener(notificationToggle);
        mCustomView.findViewById(R.id.action_bar_watermark).setOnClickListener(notificationToggle);
        mCustomView.findViewById(R.id.action_bar_bg).setOnClickListener(notificationToggle);
        mCustomView.findViewById(R.id.app_logo).setOnClickListener(notificationToggle);

    }

    private void selectFragment(int position) {
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
            if(backstackCount==1) {
                setActionBarDefault();
                showWatermark(R.drawable.hangout_actionbar_watermark,true);
            }
        }
        else
            super.onBackPressed();
    }

    public void setActionBarDefault(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.getCustomView().findViewById(R.id.action_bar_bg).setBackgroundColor(getResources().getColor(R.color.DarkRed));
        actionBar.getCustomView().findViewById(R.id.app_logo).setVisibility(View.VISIBLE);
        actionBar.getCustomView().findViewById(R.id.action_bar_title).setVisibility(View.VISIBLE);
    }

    public void doTranslateAnimation(String imgUrl){

        final RoundedImageView duplicateImg = new RoundedImageView(this);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.profile_pic_size);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(iconSize,iconSize);
        //lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.leftMargin = iconPos[0];
        lp.topMargin = iconPos[1];
        Picasso.with(this)
                .load(imgUrl)
                .into(duplicateImg);
        final RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root_view);
        rootView.addView(duplicateImg,lp);
        duplicateImg.bringToFront();

        //execute animation

        int headerLogoSize = getResources().getDimensionPixelSize(R.dimen.profile_header_logo_size);
        float ratio = ((float) headerLogoSize/iconSize);
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(800);
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
                if(profileFragment!=null)
                    profileFragment.mHeaderLogo.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rootView.removeView(duplicateImg);
                    }
                }, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animSet.setInterpolator(mSmoothInterpolator);
        duplicateImg.startAnimation(animSet);
    }

    public void initTabs(){
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_1_TAG)), FragmentTab.class, null);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_2_TAG)), FragmentTab.class, null);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_3_TAG)), FragmentTab.class, null);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_4_TAG)), FragmentTab.class, null);
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
        //v.setBackgroundResource(R.drawable.tab_indicator);
        ImageView tabImg = (ImageView) v.findViewById(R.id.tab_image);
        String tag = spec.getTag();
        if(tag.equals(TAB_1_TAG))
            tabImg.setBackgroundResource(R.drawable.ic_hangout_tab);
        else if(tag.equals(TAB_2_TAG))
            tabImg.setBackgroundResource(R.drawable.ic_discover_tab);
        else if(tag.equals(TAB_3_TAG))
            tabImg.setBackgroundResource(R.drawable.ic_trending_tab);
        else if(tag.equals(TAB_4_TAG))
            tabImg.setBackgroundResource(R.drawable.ic_thebag_tab);
        return spec.setIndicator(v);
    }

    private void showWatermark(int resId, boolean show){
        ActionBar actionBar = getSupportActionBar();
        if(show) {
            actionBar.getCustomView().findViewById(R.id.action_bar_watermark).setBackgroundResource(resId);
            actionBar.getCustomView().findViewById(R.id.action_bar_watermark).setVisibility(View.VISIBLE);
        } else
            actionBar.getCustomView().findViewById(R.id.action_bar_watermark).setVisibility(View.GONE);
    }

    private int whichFragmentIsShown(){
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("PROFILE");
        if(profileFragment==null ||!profileFragment.isVisible()){
            return HANG_OUT;
        }
        return -1;
    }

    private void initNotification(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        contentWidth = dm.widthPixels;
        contentHeight = content.getMeasuredHeight();
        Log.d(TAG,"width:"+contentWidth+"height:"+contentHeight);

        TypedValue outValue = new TypedValue();
        int[] attrs = { android.R.attr.windowBackground };
        mainActivity.getTheme().resolveAttribute(android.R.attr.windowBackground, outValue, true);
        TypedArray style = mainActivity.getTheme().obtainStyledAttributes(outValue.resourceId, attrs);
        windowBackground = style.getDrawable(0);
        style.recycle();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        try {
            // Instance of this activity as a parameter
            vSlidingMenu = new DobSlidingMenu(mainActivity);

            // Sliding type can be sizing or moving
            vSlidingMenu.setSlidingType(SlidingItem.SlidingType.MOVE);

            // The view that will be in sliding menu
            // We can assign XML layout or view
            vSlidingMenu.setSlidingView(R.layout.notification_layout);

            // This sentence is for handle that will be shown
            // in the middle of ActionBar,
            // default value is true
            vSlidingMenu.setUseHandle(true);

            vSlidingMenu.setMaxDuration(1000);

            // To access views in sliding menu
            slidingView = vSlidingMenu.getSlidingView();
            slidingView.findViewById(R.id.clickMe).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // Anything

                            // sliding menu finish method to
                            // collapse the sliding menu
                            vSlidingMenu.finish();
                        }
                    });

            // Callback after collapsing
            vSlidingMenu.setOnCollapsedListener(new OnCollapsedListener() {

                @Override
                public void onCollapsed() {
                    Log.i(TAG, "onCollapsed");
                    blurredBg.setImageBitmap(null);
                    blurredBg.setVisibility(View.GONE);
                    setIsExpanded(false);
                }
            });
            //slidingView.setOn

            // Callback after expanding
            vSlidingMenu.setOnExpandedListener(new OnExpandedListener() {

                @Override
                public void onExpanded() {
                    Log.i(TAG, "onExpanded");
                    //setBlurredBackground();
                    int [] pos = new int[2];
                    slidingView.getLocationInWindow(pos);
                    Log.d(TAG,"xpos:" + pos[0]+"ypos:"+pos[1]);
                    setIsExpanded(true);
                }
            });
        } catch (NoActionBarException e) {
            e.printStackTrace();
        }
    }

    private void setBlurredBackground(int height){
        //if(contentHeight==0)
            //contentHeight = content.getMeasuredHeight();
        blurredBitmap = ImageUtil.drawViewToBitmap(
                blurredBitmap,content,contentWidth,height,10,windowBackground);
        blurredBitmap = ImageUtil.BlurBitmap(mainActivity, blurredBitmap, 5);
        blurredBg.setVisibility(View.VISIBLE);
        blurredBg.setImageBitmap(blurredBitmap);

    }

    private void blurBackground(final View slidingLayout){
        runOnUiThread(new Runnable() {
            int yPos = -1;
            int [] pos = new int[2];
            @Override
            public void run() {
                slidingLayout.getLocationInWindow(pos);
                while(yPos!=pos[1]){
                    yPos = pos[1];
                    setBlurredBackground(yPos+contentHeight);
                    slidingLayout.getLocationInWindow(pos);
                }
            }
        });
    }

    private static void setIsExpanded(boolean expanded){
        isExpanded = expanded;
    }


}
