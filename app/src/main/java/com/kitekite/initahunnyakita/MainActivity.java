package com.kitekite.initahunnyakita;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kitekite.initahunnyakita.adapter.NotificationAdapter;
import com.kitekite.initahunnyakita.fragment.FragmentTab;
import com.kitekite.initahunnyakita.fragment.HangoutFragment;
import com.kitekite.initahunnyakita.fragment.ProfileFragment;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.model.NotificationItem;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.widget.RevealLayout;
import com.kitekite.initahunnyakita.widget.RoundedImageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


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
    public int [] iconPos,iconDest = new int[2];
    List pollList = new ArrayList< HangoutPost>();
    EditText pollCaption;
    FragmentTabHost mTabHost;
    RelativeLayout holderLayout;
    LinearLayout pollHolder;
    RevealLayout mRevealLayout;
    View  content;
    ImageView blurredBg;
    int contentHeight;
    int contentWidth;
    LayoutInflater inflater;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;
    public Bitmap blurredBitmap;
    boolean isLoggedIn;
    private static boolean isExpanded,isPollLayoutShown = false;
    final private static int USER_MODE  = 1;
    final private static int SHOP_MODE  = 2;
    private static int mode  = USER_MODE;
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

        initPollHolder();

        blurredBg = (ImageView)mainActivity.findViewById(R.id.blur_image);
        inflater = LayoutInflater.from(mainActivity);
        content = blurredBg.getRootView();
        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);

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
                mRevealLayout.next();
                if(mode==USER_MODE) {
                    mode = SHOP_MODE;
                    YoYo.with(Techniques.SlideOutRight)
                            .duration(500)
                            .playOn(findViewById(R.id.usermode_action_bar_bg));
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(500)
                            .playOn(findViewById(R.id.shopmode_action_bar_bg));
                }else {
                    mode = USER_MODE;
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(500)
                            .playOn(findViewById(R.id.usermode_action_bar_bg));
                    YoYo.with(Techniques.SlideOutRight)
                            .duration(500)
                            .playOn(findViewById(R.id.shopmode_action_bar_bg));
                }
            }
        };
        mCustomView.findViewById(R.id.action_bar_title).setOnClickListener(notificationToggle);
        mCustomView.findViewById(R.id.action_bar_watermark).setOnClickListener(notificationToggle);
        mCustomView.findViewById(R.id.usermode_action_bar_bg).setOnClickListener(notificationToggle);
        mCustomView.findViewById(R.id.app_logo).setOnClickListener(notificationToggle);
        mCustomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    findViewById(R.id.notif_toggle).requestFocus();

                }
                return false;
            }
        });
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
        actionBar.getCustomView().findViewById(R.id.usermode_action_bar_bg).setBackgroundColor(getResources().getColor(R.color.DarkRed));
        actionBar.getCustomView().findViewById(R.id.shopmode_action_bar_bg).setBackgroundColor(getResources().getColor(R.color.CornflowerBlue));
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
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_3_TAG)), FragmentTab.class, null);
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
        ListView notificationList = (ListView) findViewById(R.id.notif_listview);
        ArrayList<NotificationItem> list = new ArrayList<NotificationItem>();
        NotificationItem item1 = new NotificationItem();
        NotificationItem item2 = new NotificationItem();
        NotificationItem item3 = new NotificationItem();
        item1.setFullname("Jethro Satya");
        item1.setAction(NotificationItem.ACTION_TYPE_COMMENT);
        item1.setContent("gila ini gaul bangetttttttttttt");
        item1.setProfileUrl("http://i58.tinypic.com/1zeum54.jpg");
        item1.setItemUrl("file:///android_asset/jerseynesia_item1.jpg");
        list.add(item1);
        item2.setFullname("Leviero Leviero");
        item2.setAction(NotificationItem.ACTION_TYPE_COMMENT);
        item2.setContent("bro jadi beli komputer baru? ini aja bro lenovo FTW");
        item2.setProfileUrl("http://i59.tinypic.com/2vxftiw.jpg");
        item2.setItemUrl("http://www.lenovo.com/images/gallery/1060x596/lenovo-laptop-convertible-thinkpad-yoga-silver-front-1.jpg");
        list.add(item2);
        item3.setFullname("Jethro Satya");
        item3.setAction(NotificationItem.ACTION_TYPE_COMMENT);
        item3.setContent("spam super hahahahahahahahhahhahaha");
        item3.setProfileUrl("http://i58.tinypic.com/1zeum54.jpg");
        item3.setItemUrl("file:///android_asset/ensa_shop_item1.jpg");
        list.add(item3);
        NotificationAdapter adapter = new NotificationAdapter(this, 0, list);
        notificationList.setAdapter(adapter);
    }

    private void setBlurredBackground(int height){
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

    public void initPollHolder(){
        holderLayout = (RelativeLayout) findViewById(R.id.holder_layout);
        pollHolder = (LinearLayout) findViewById(R.id.poll_holder);
        pollCaption = (EditText) findViewById(R.id.poll_EditText);
        ImageView cancelBtn = (ImageView) holderLayout.findViewById(R.id.poll_cancel_btn);
        Button pollBtn = (Button) holderLayout.findViewById(R.id.poll_btn);
        View.OnClickListener closeHolder = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPollHolder(false);
            }
        };
        cancelBtn.setOnClickListener(closeHolder);
        pollBtn.setOnClickListener(closeHolder);
    }

    public void showPollHolder(final boolean show){
        isPollLayoutShown = show;
        int holderHeight = holderLayout.getMeasuredHeight();
        ObjectAnimator mover = ObjectAnimator.ofFloat(holderLayout,"translationY", 0f, holderHeight);
        if(!show)
            mover = ObjectAnimator.ofFloat(holderLayout, "translationY", holderHeight, 0f);
        mover.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!show) {
                    pollHolder.removeAllViews();
                    pollList.clear();
                    pollCaption.setText("");
                    hideKeyboard(pollCaption);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mover.setDuration(800);
        mover.start();
    }

    public void addPollItem(HangoutPost post){
        if(pollList.size()==5){
            Toast.makeText(this,"Can't add more options",Toast.LENGTH_SHORT).show();
            return;
        }
        final int size = getResources().getDimensionPixelSize(R.dimen.poll_holder_item_size);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        lp.setMargins(15,0,0,0);
        lp.gravity = Gravity.CENTER_VERTICAL;
        ImageView item = new ImageView(this);
        item.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(this)
                .load(post.getItemUrl())
                .into(item);
        pollHolder.addView(item,lp);
        pollList.add(post);
        if(!isPollLayoutShown)
            showPollHolder(true);
    }

    public void hideKeyboard(View bindedView){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(bindedView.getWindowToken(), 0);
    }

}
