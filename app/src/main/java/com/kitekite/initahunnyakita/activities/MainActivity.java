package com.kitekite.initahunnyakita.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.NotificationAdapter;
import com.kitekite.initahunnyakita.fragment.DiscussionFragment;
import com.kitekite.initahunnyakita.fragment.HangoutFragment;
import com.kitekite.initahunnyakita.fragment.ProfileFragment;
import com.kitekite.initahunnyakita.fragment.TheBagFragment;
import com.kitekite.initahunnyakita.fragment.discover.DiscoverFragment;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.model.NotificationItem;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.HardcodeValues;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.util.MainTabStack;
import com.kitekite.initahunnyakita.widget.ActionBarLayout;
import com.kitekite.initahunnyakita.widget.NotificationLayout;
import com.kitekite.initahunnyakita.widget.RevealLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static final String HANG_OUT_TAG = "HANG_OUT";
    public static final String DISCOVER_TAG = "DISCOVER";
    public static final String DISCUSSION_TAG = "DISCUSSION";
    public static final String THE_BAG_TAG = "THE_BAG";

    public static final String TAB_1_TAG = "TAB 1";
    public static final String TAB_2_TAG = "TAB 2";
    public static final String TAB_3_TAG = "TAB 3";//trending/stories?
    public static final String TAB_4_TAG = "TAB 4";

    private String TAG = "taikodok";
    public int [] iconPos,iconDest = new int[2];
    List pollList = new ArrayList< HangoutPost>();
    EditText pollCaption;
    FragmentTabHost mTabHost;
    RelativeLayout holderLayout;
    LinearLayout pollHolder;
    RevealLayout mRevealLayout;
    View content;
    NotificationLayout mNotificationLayout;
    ImageView blurredBg;
    int contentHeight;
    int contentWidth;
    LayoutInflater inflater;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;
    public Bitmap blurredBitmap;
    boolean isLoggedIn;
    private static boolean isPollLayoutShown = false;
    final public static int USER_MODE  = 1;
    final public static int SHOP_MODE  = 2;
    public static int mode  = USER_MODE;
    Drawable windowBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        //go to login activity if user's not logged in
        isLoggedIn = getSharedPreferences(Global.login_cookies,0).getBoolean(Global.is_logged_in,false);
        if(!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();

        initActionBar();

        initTabs();

        initPollHolder();

        blurredBg = (ImageView) findViewById(R.id.blur_image);
        inflater = LayoutInflater.from(this);
        content = blurredBg.getRootView();
        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        //if(whichFragmentIsShown()==HANG_OUT)
            //showWatermark(R.drawable.hangout_actionbar_watermark,true);

        initNotification();
    }

    public void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHideOnContentScrollEnabled(true);
        mActionBar.setHideOffset(50);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);

        LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        ActionBarLayout mCustomView = (ActionBarLayout) mInflater.inflate(R.layout.custom_actionbar_default, null);
        mCustomView.setHasRevealLayout(true);
        mActionBar.setCustomView(mCustomView, lp);

    }

    @Override
    public void onBackPressed(){
        if(mNotificationLayout.isLayoutExpanded()){
            mNotificationLayout.collapseLayout();
            return;
        }
        if(mode == SHOP_MODE){
            ((ActionBarLayout) findViewById(R.id.actionbar_layout)).switchToUserMode();
            return;
        }
        int backstackCount = getSupportFragmentManager().getBackStackEntryCount();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);
        if (backstackCount>0) {
            if (currentFragment instanceof ProfileFragment) {
                getSupportActionBar().show();
                setActionBarDefault();
            } else if(currentFragment instanceof DiscussionFragment) {
                DiscussionFragment discussionFragment = (DiscussionFragment) currentFragment;
                if(discussionFragment.onBackPressed()){
                    return;
                }
            }
            getSupportFragmentManager().popBackStack();
        } else if (getSupportFragmentManager().findFragmentById(android.R.id.tabcontent) instanceof DiscussionFragment) {
            if (!(((DiscussionFragment) getSupportFragmentManager().findFragmentById(android.R.id.tabcontent)).onBackPressed()))
                handleTabNavigation();
        } else {
            handleTabNavigation();
        }
    }

    public void handleTabNavigation() {
        int index = MainTabStack.pop();
        if (index == MainTabStack.SHOULD_FINISH_ACTIVITY) {
            finish();
        } else {
            mTabHost.setCurrentTab(index);
        }
    }

    public void setActionBarDefault(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHideOnContentScrollEnabled(true);
        actionBar.setCustomView(R.layout.custom_actionbar_default);
        actionBar.getCustomView().findViewById(R.id.usermode_action_bar_bg).setBackgroundColor(getResources().getColor(R.color.DarkTeal));
        actionBar.getCustomView().findViewById(R.id.shopmode_action_bar_bg).setBackgroundColor(getResources().getColor(R.color.CornflowerBlue));
        actionBar.getCustomView().findViewById(R.id.app_logo).setVisibility(View.VISIBLE);
    }

    public Fragment getFragment(Class className) {
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (className.isInstance(f)) {
                return f;
            }
        }
        return null;
    }

    public void initTabs(){
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAB_1_TAG)) {
                    MainTabStack.push(0);
                } else if (tabId.equals(TAB_2_TAG)) {
                    MainTabStack.push(1);
                } else if (tabId.equals(TAB_3_TAG)) {
                    MainTabStack.push(2);
                } else if (tabId.equals(TAB_4_TAG)) {
                    MainTabStack.push(3);
                }
            }
        });

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_1_TAG)), HangoutFragment.class, null);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_2_TAG)), DiscoverFragment.class, null);
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_3_TAG)), DiscussionFragment.class, null);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_4_TAG)), TheBagFragment.class, null);
        /*mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_1_TAG)));
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_2_TAG)));
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_3_TAG)));
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_4_TAG)));*/
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item_main, null);
        //v.setBackgroundResource(R.drawable.tab_indicator);
        ImageView tabImg = (ImageView) v.findViewById(R.id.tab_image);
        TextView tabTitle = (TextView) v.findViewById(R.id.tab_title);
        String tag = spec.getTag();
        if(tag.equals(TAB_1_TAG)) {
            tabImg.setBackgroundResource(R.drawable.ic_hangout_tab_inactive);
            tabTitle.setText("Hang Out");
        }
        else if(tag.equals(TAB_2_TAG)) {
            tabImg.setBackgroundResource(R.drawable.ic_discover_tab_inactive);
            tabTitle.setText("Discover");
        }
        else if(tag.equals(TAB_3_TAG)){
            tabImg.setBackgroundResource(R.drawable.ic_discussion_tab_normal);
            tabTitle.setText("Discussion");

        }
        else if(tag.equals(TAB_4_TAG)){
            //v.setBackgroundColor(getResources().getColor(R.color.Chocolate));
            tabImg.setBackgroundResource(R.drawable.ic_thebag_tab_inactive);
            tabTitle.setText("The Bag");

        }
        return spec.setIndicator(v);
    }

    private void initNotification(){
        mNotificationLayout = (NotificationLayout) findViewById(R.id.notification_layout);
        ListView notificationList = (ListView) findViewById(R.id.notif_listview);
        ArrayList<NotificationItem> list = new ArrayList<>();
        for (int i=0;i< HardcodeValues.NotificationItems.fullnames.length;i++){
            NotificationItem item = new NotificationItem();
            item.setFullname(HardcodeValues.NotificationItems.fullnames[i]);
            item.setAction(NotificationItem.ACTION_TYPE_COMMENT);
            item.setContent(HardcodeValues.NotificationItems.contents[i]);
            item.setProfileUrl(HardcodeValues.NotificationItems.profileUrls[i]);
            item.setItemUrl(HardcodeValues.NotificationItems.itemUrls[i]);
            list.add(item);
        }
        NotificationAdapter adapter = new NotificationAdapter(this, 0, list);
        notificationList.setAdapter(adapter);
    }

    private void setBlurredBackground(int height){
        blurredBitmap = ImageUtil.drawViewToBitmap(
                blurredBitmap,content,contentWidth,height,10,windowBackground);
        blurredBitmap = ImageUtil.BlurBitmap(this, blurredBitmap, 5);
        blurredBg.setVisibility(View.VISIBLE);
        blurredBg.setImageBitmap(blurredBitmap);

    }

    private void blurBackground(final View slidingLayout){
        runOnUiThread(new Runnable() {
            int yPos = -1;
            int[] pos = new int[2];

            @Override
            public void run() {
                slidingLayout.getLocationInWindow(pos);
                while (yPos != pos[1]) {
                    yPos = pos[1];
                    setBlurredBackground(yPos + contentHeight);
                    slidingLayout.getLocationInWindow(pos);
                }
            }
        });
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
        lp.setMargins(15, 0, 0, 0);
        lp.gravity = Gravity.CENTER_VERTICAL;
        ImageView item = new ImageView(this);
        //item.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(this)
                .load(post.getItemUrl())
                .fit().centerCrop()
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
