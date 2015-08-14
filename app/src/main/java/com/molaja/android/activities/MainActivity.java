package com.molaja.android.activities;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.adapter.NotificationPagerAdapter;
import com.molaja.android.fragment.DiscussionFragment;
import com.molaja.android.fragment.discover.DiscoverFragment;
import com.molaja.android.fragment.maintab.FirstTabFragment;
import com.molaja.android.fragment.maintab.MainTabFragment;
import com.molaja.android.fragment.thebag.TheBagFragment;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.model.User;
import com.molaja.android.util.MainTabStack;
import com.molaja.android.widget.ActionBarLayout;
import com.molaja.android.widget.NotificationLayout;
import com.molaja.android.widget.RevealLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String HANG_OUT_TAG = "HANG_OUT";
    public static final String DISCOVER_TAG = "DISCOVER";
    public static final String DISCUSSION_TAG = "DISCUSSION";
    public static final String THE_BAG_TAG = "THE_BAG";

    public static final String TAB_1_TAG = "TAB 1";
    public static final String TAB_2_TAG = "TAB 2";
    public static final String TAB_3_TAG = "TAB 3";
    public static final String TAB_4_TAG = "TAB 4";

    public static final int MODE_ACTIONBAR_DEFAULT = 1;
    public static final int MODE_ACTIONBAR_PROFILE = 2;

    final public static int USER_MODE  = 1;
    final public static int SHOP_MODE  = 2;

    public static final String SHOW_DIALOG = "com.molaja.android.SHOW_DIALOG";
    public static final String DISMISS_DIALOG = "com.molaja.android.DISMISS_DIALOG";

    private static boolean isPollLayoutShown = false;
    private static int mActionBarMode = MODE_ACTIONBAR_DEFAULT;
    public static int mode  = USER_MODE;

    private String TAG = getClass().getSimpleName();
    List pollList = new ArrayList<>();

    //views
    Toolbar mToolbar;
    EditText pollCaption;
    FragmentTabHost mTabHost;
    RelativeLayout holderLayout;
    LinearLayout pollHolder;
    RevealLayout mRevealLayout;
    View rootView;
    NotificationLayout mNotificationLayout;
    ImageView blurredBg;

    boolean isLoggedIn;


    BroadcastReceiver mDialogPopupReceiver = new BroadcastReceiver() {
        ProgressDialog pDialog;

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("MESSAGE");
            boolean indeterminate = intent.getBooleanExtra("INDETERMINATE", true);
            boolean cancelable = intent.getBooleanExtra("CANCELABLE", true);
            if (intent.getAction().equals(SHOW_DIALOG)) {
                pDialog = new ProgressDialog(context);
                pDialog.setIndeterminate(indeterminate);
                pDialog.setCancelable(cancelable);
                pDialog.setMessage(message);
                pDialog.show();
            } else if (intent.getAction().equals(DISMISS_DIALOG)) {
                if (pDialog != null)
                    pDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        //go to login activity if user's not logged in
        isLoggedIn = MolajaApplication.isLoggedIn(this);
        if(!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        //register receivers
        IntentFilter filter = new IntentFilter(SHOW_DIALOG);
        filter.addAction(DISMISS_DIALOG);
        registerReceiver(mDialogPopupReceiver, filter);

        bindViews();

        initActionBar();

        initTabs();

        initPollHolder();

        initNotification();

        //init profile action bar
        User user = User.getCurrentUser(this);
        initProfileActionBar(user.name, user.image);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mDialogPopupReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews() {
        blurredBg = (ImageView) findViewById(R.id.blur_image);
        rootView = findViewById(R.id.root_view);
        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        holderLayout = (RelativeLayout) findViewById(R.id.holder_layout);
        pollHolder = (LinearLayout) findViewById(R.id.poll_holder);
        pollCaption = (EditText) findViewById(R.id.poll_EditText);

    }

    private void initActionBar() {
        ((ActionBarLayout)mToolbar.findViewById(R.id.actionbar_layout)).setHasRevealLayout(true);
        setSupportActionBar(mToolbar);
    }

    /**
     * gets the current mode of the action bar (Default app title or User profile).
     * @return The current mode.
     */
    public int getActionBarMode() {
        return mActionBarMode;
    }

    /**
     * sets the current actionbar mode.
     * @param mode the actionbar mode to set to.
     */
    private void setActionBarMode(int mode) {
        mActionBarMode = mode;
    }

    /**
     * gets the current fragment in the container.
     */
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);
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
        //int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);

        if (currentFragment instanceof MainTabFragment) {
            int backStackEntryCount = currentFragment.getChildFragmentManager().getBackStackEntryCount();

            if (backStackEntryCount > 0) {
                currentFragment.getChildFragmentManager().popBackStack();
                return;
            }
        }

        if (currentFragment instanceof DiscussionFragment)
            if (((DiscussionFragment)currentFragment).onBackPressed())
                return;

        if (currentFragment instanceof DiscoverFragment)
            if (((DiscoverFragment)currentFragment).onBackPressed())
                return;
        handleTabNavigation();
    }

    public void handleTabNavigation() {
        int index = MainTabStack.pop();
        if (index == MainTabStack.SHOULD_FINISH_ACTIVITY) {
            finish();
        } else {
            mTabHost.setCurrentTab(index);
        }
    }

    public void setToolbarDefault() {
        setToolbarColor(getResources().getColor(R.color.Teal), true);
        findViewById(R.id.app_logo).setVisibility(View.VISIBLE);
        findViewById(R.id.action_bar_watermark).setVisibility(View.VISIBLE);
    }

    public void setToolbarTransparent() {
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        //findViewById(R.id.usermode_action_bar_bg).setAlpha(0f);
        //findViewById(R.id.shopmode_action_bar_bg).setAlpha(0f);
        findViewById(R.id.action_bar_watermark).setVisibility(View.GONE);
    }

    public void setToolbarAlpha(float alpha, int color) {
        /*findViewById(R.id.usermode_action_bar_bg).setAlpha(alpha);
        findViewById(R.id.shopmode_action_bar_bg).setAlpha(alpha);*/
        float convertedAlpha = alpha * 255;
        setToolbarColor(Color.argb((int) convertedAlpha, Color.red(color), Color.green(color), Color.blue(color)), false);
    }

    /**
     * set the toolbar's color given the specified color.
     * @param color the color you want to switch to.
     * @param animate enable/disable crossfade animation.
     */
    public void setToolbarColor(int color, boolean animate) {
        if (animate) {
            int currentColor = ((ColorDrawable)mToolbar.getBackground()).getColor();
            ObjectAnimator colorFade = ObjectAnimator.ofObject(mToolbar, "backgroundColor", new ArgbEvaluator(),
                    currentColor, color);
            colorFade.setDuration(300);
            colorFade.start();
        } else {
            mToolbar.setBackgroundColor(color);
        }
        //((ImageView)findViewById(R.id.usermode_action_bar_bg)).setImageDrawable(new ColorDrawable(color));
    }

    public void setToolbarElevation(int elevation) {
        ViewCompat.setElevation(mToolbar, elevation);
    }

    /**
     * Switches between two action bar title modes: default title and profile title.
     * @param b true for profile title, false for default.
     */
    public void switchToProfileActionBar(boolean b) {
        //return if actionbar mode is the same
        if (b == (getActionBarMode() == MODE_ACTIONBAR_PROFILE))
            return;

        if (b) {
            ((ViewSwitcher) findViewById(R.id.actiobar_view_switcher)).showNext();
            setActionBarMode(MODE_ACTIONBAR_PROFILE);
        } else {
            ((ViewSwitcher) findViewById(R.id.actiobar_view_switcher)).showPrevious();
            setActionBarMode(MODE_ACTIONBAR_DEFAULT);
        }
    }

    private void initProfileActionBar(String profileName, String image) {
        ((TextView) findViewById(R.id.action_bar_profile_name)).setText(profileName);
        Picasso.with(this)
                .load(image)
                .fit()
                .into((ImageView) findViewById(R.id.action_bar_profile_picture));
    }

    /**
     *
     * @param className
     * @return
     */
    public Fragment getFragment(Class className) {
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (className.isInstance(f)) {
                return f;
            }
        }
        return null;
    }

    private void initTabs() {
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
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_1_TAG)), FirstTabFragment.class, null);
        mTabHost.addTab(setIndicator(this,mTabHost.newTabSpec(TAB_2_TAG)), DiscoverFragment.class, null);
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_3_TAG)), DiscussionFragment.class, null);
        mTabHost.addTab(setIndicator(this, mTabHost.newTabSpec(TAB_4_TAG)), TheBagFragment.class, null);

        ViewCompat.setElevation(mTabHost, getResources().getDimensionPixelSize(R.dimen.default_elevation));
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item_main, null);
        ImageView tabImg = (ImageView) v.findViewById(R.id.tab_image);
        TextView tabTitle = (TextView) v.findViewById(R.id.tab_title);
        String tag = spec.getTag();
        if(tag.equals(TAB_1_TAG)) {
            tabImg.setImageResource(R.drawable.ic_hangout_tab);
            tabTitle.setText("Hang Out");
        }
        else if(tag.equals(TAB_2_TAG)) {
            tabImg.setImageResource(R.drawable.ic_discover_tab);
            tabTitle.setText("Discover");
        }
        else if(tag.equals(TAB_3_TAG)){
            tabImg.setImageResource(R.drawable.ic_discussion_tab);
            tabTitle.setText("Discussion");

        }
        else if(tag.equals(TAB_4_TAG)){
            tabImg.setImageResource(R.drawable.ic_thebag_tab);
            tabTitle.setText("The Bag");

        }
        return spec.setIndicator(v);
    }

    private void initNotification(){
        mNotificationLayout = (NotificationLayout) findViewById(R.id.notification_layout);
        mNotificationLayout.setVisibility(View.GONE);

        ViewPager notificationPager = (ViewPager) findViewById(R.id.notif_view_pager);
        notificationPager.setAdapter(new NotificationPagerAdapter(getSupportFragmentManager()));
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpager_tab);
        viewPagerTab.setViewPager(notificationPager);

        /*ListView notificationList = (ListView) findViewById(R.id.notif_listview);
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
        notificationList.setAdapter(adapter);*/
    }

    public void initPollHolder(){
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
                MolajaApplication.showKeyboard(MainActivity.this, pollCaption, false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!show) {
                    pollHolder.removeAllViews();
                    pollList.clear();
                    pollCaption.setText("");
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

}
