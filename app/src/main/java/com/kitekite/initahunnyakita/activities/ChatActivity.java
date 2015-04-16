package com.kitekite.initahunnyakita.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.ItemDetailPagerAdapter;
import com.kitekite.initahunnyakita.fragment.itemdetail.OverviewFragment;
import com.kitekite.initahunnyakita.util.EventBus;
import com.kitekite.initahunnyakita.util.HardcodeValues;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.util.PageChangedEvent;
import com.kitekite.initahunnyakita.util.PicassoBlurRequestHandler;
import com.kitekite.initahunnyakita.widget.ViewPagerIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by tinklabs on 4/16/2015.
 */
public class ChatActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, Target {
    SlidingUpPanelLayout slidingLayout;
    private ViewPagerIndicator mViewPagerIndicator;
    ImageView profileBarBg;
    View profileBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);

        setupViews();
    }

    private void setupViews() {
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        profileBar = findViewById(R.id.profile_bar);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.viewpager_indicator);
        profileBarBg = (ImageView) findViewById(R.id.profile_bar_background);
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        TextView name = (TextView) findViewById(R.id.name);

        Picasso.with(this)
                .load(getIntent().getStringExtra("PROFILE_PICTURE"))
                .fit()
                .into(profilePicture);

        Picasso.with(this)
                .load(getIntent().getStringExtra("PROFILE_PICTURE"))
                .into(this);

        name.setText(getIntent().getStringExtra("NAME"));

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        ArrayList<Class<? extends Fragment>> pages = new ArrayList<Class<? extends Fragment>>();

        int totalPage = HardcodeValues.ItemDetailValues.imageUrls.length;
        for(int i=0;i< totalPage;i++)
            pages.add(OverviewFragment.class);

        viewPager.setAdapter(new ItemDetailPagerAdapter(getSupportFragmentManager(), this, pages, null));
        viewPager.setOnPageChangeListener(this);

        slidingLayout.setDragView(profileBar);

        mViewPagerIndicator.initIndicators(HardcodeValues.ItemDetailValues.imageUrls.length);

        //open chat layout after 1s delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                profileBar.performClick();
            }
        }, 1000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mViewPagerIndicator.selectIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        profileBarBg.setImageBitmap(ImageUtil.BlurBitmap(this, bitmap, 20));
        profileBarBg.setAlpha(0.5f);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
