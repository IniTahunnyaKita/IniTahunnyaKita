package com.molaja.android.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.gson.Gson;
import com.grantlandchew.view.VerticalPager;
import com.molaja.android.R;
import com.molaja.android.fragment.itemdetail.DescriptionFragment;
import com.molaja.android.fragment.itemdetail.OverviewCompositeFragment;
import com.molaja.android.fragment.itemdetail.OverviewFragment;
import com.molaja.android.fragment.itemdetail.ReviewFragment;
import com.molaja.android.model.HangoutPost;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Florian on 4/4/2015.
 */
public class ItemDetailActivity extends AppCompatActivity implements VerticalPager.OnPullToZoomListener {

    private static final int CENTRAL_PAGE_INDEX = 0;

    // -----------------------------------------------------------------------
    //
    // Fields
    //
    // -----------------------------------------------------------------------
    private VerticalPager mVerticalPager;
    private View overlay;
    private TextView priceOverlay, titleOverlay;
    private ImageView zoomIcon, zoomInBtn, zoomOutBtn;
    public static HangoutPost itemInfo;
    private boolean isZoomIconShown = false;
    public static boolean isOverlayShown = true, isInZoomMode = false;

    private boolean onPause = false;
    private Vibrator haptic;
    private SpringSystem springSystem;


    private OverviewCompositeFragment overviewCompositeFragment;

    // -----------------------------------------------------------------------
    //
    // Methods
    //
    // -----------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        itemInfo = gson.fromJson(getIntent().getStringExtra("ITEM_INFO"), HangoutPost.class);
        setContentView(R.layout.fragment_item_detail);
        findViews();

        haptic = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        springSystem = SpringSystem.create();
    }

    private void findViews() {
        mVerticalPager = (VerticalPager) findViewById(R.id.main_vertical_pager);

        zoomIcon = (ImageView) findViewById(R.id.zoom_icon);
        zoomOutBtn = (ImageView) findViewById(R.id.zoom_out_btn);
        zoomInBtn = (ImageView) findViewById(R.id.zoom_in_btn);

        overlay = findViewById(R.id.short_detail_overlay);
        priceOverlay = (TextView) findViewById(R.id.overlay_price);
        priceOverlay.setText(itemInfo.getPrice());
        titleOverlay = (TextView) findViewById(R.id.overlay_title);
        initViews();
    }

    public void setBackgroundColor(int backgroundColor) {
        findViewById(R.id.root_view).setBackgroundColor(backgroundColor);
    }

    private void initViews() {
        titleOverlay.setText(itemInfo.getTitle());

        overviewCompositeFragment = new OverviewCompositeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_overview_fragment, overviewCompositeFragment);

        Fragment descriptionFragment = new DescriptionFragment();
        ft.replace(R.id.main_description_fragment, descriptionFragment);

        Fragment reviewFragment = new ReviewFragment();
        ft.replace(R.id.main_review_fragment, reviewFragment);
        ft.commit();

        //hide zoom icon
        ObjectAnimator hide = ObjectAnimator.ofFloat(zoomIcon, "translationY", 0f, -zoomIcon.getMeasuredHeight()*2);
        hide.setDuration(0);
        hide.start();

        snapPageWhenLayoutIsReady(mVerticalPager, CENTRAL_PAGE_INDEX);
        mVerticalPager.setOnPullToZoomListener(this);
        mVerticalPager.addOnScrollListener(new VerticalPager.OnScrollListener() {

            @Override
            public void onScroll(int scrollX) {
                //create parallax effect
                ViewCompat.setTranslationY(findViewById(R.id.main_overview_fragment), - scrollX / 4);
            }

            @Override
            public void onViewScrollFinished(int currentPage) {

            }
        });
        mVerticalPager.setOnTapListener(new VerticalPager.OnTapListener() {
            @Override
            public void onTap() {
                //disable overlay if in zoom mode
                if(isInZoomMode)
                    return;

                if (isOverlayShown) {
                    YoYo.with(Techniques.FadeOut)
                            .duration(200)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {}

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    isOverlayShown = false;
                                    overlay.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {}

                                @Override
                                public void onAnimationRepeat(Animator animation) {}
                            })
                            .playOn(overlay);
                } else {
                    YoYo.with(Techniques.FadeIn)
                            .duration(200)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    overlay.setVisibility(View.VISIBLE);
                                    isOverlayShown = true;
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            })
                            .playOn(overlay);
                }
            }
        });
    }

    private void snapPageWhenLayoutIsReady(final View pageView, final int page) {
		/*
		 * VerticalPager is not fully initialized at the moment, so we want to snap to the central page only when it
		 * layout and measure all its pages.
		 */
        pageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mVerticalPager.snapToPage(page, VerticalPager.PAGE_SNAP_DURATION_INSTANT);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    // recommended removeOnGlobalLayoutListener method is available since API 16 only
                    pageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    removeGlobalOnLayoutListenerForJellyBean(pageView);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            private void removeGlobalOnLayoutListenerForJellyBean(final View pageView) {
                pageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public static HangoutPost getItemInfo(){
        return itemInfo;
    }

    public void hideOverlay(){
        final View overlay = findViewById(R.id.short_detail_overlay);
        YoYo.with(Techniques.FadeOut)
                .duration(200)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isOverlayShown = false;
                        overlay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(overlay);
    }

    @Override
    public void onProgress(float progress) {
        if(!isZoomIconShown)
            showZoomIcon();

        if(progress>1f){
            if(!isInZoomMode) {
                goToZoomMode();
                haptic.vibrate(50);
            }

            return;
        }

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(zoomIcon, "scaleX", 1f+progress);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(zoomIcon, "scaleY", 1f+progress);

        scaleUpX.setDuration(0);
        scaleUpY.setDuration(0);

        AnimatorSet scaleUp = new AnimatorSet();

        scaleUp.play(scaleUpX).with(scaleUpY);
        scaleUp.start();
        Log.d("vpager", "progress:" + progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overlay.setVisibility(View.VISIBLE);
                isOverlayShown = true;
                YoYo.with(Techniques.FadeIn)
                        .duration(200)
                        .playOn(overlay);
            }
        },1000);
    }

    @Override
    public void onCancel() {
        if(!isInZoomMode)
            removeZoomIcon();
    }

    public void showZoomIcon(){
        isZoomIconShown = true;
        isInZoomMode = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(zoomIcon, "translationY", -zoomIcon.getMeasuredHeight()*2, 0f);
        animator.setDuration(200);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                zoomIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void removeZoomIcon(){
        isZoomIconShown = false;

        //remove click listener
        zoomIcon.setOnClickListener(null);

        //spring animation to pager indicator and zoom icons
        Spring spring = springSystem.createSpring();
        spring.addListener(new SimpleSpringListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - value;

                zoomOutBtn.setScaleX(scale);
                zoomOutBtn.setScaleY(scale);

                zoomInBtn.setScaleX(scale);
                zoomInBtn.setScaleY(scale);

                overviewCompositeFragment.setViewPagerIndicatorScale(value);
            }
        });
        //spring.setRestSpeedThreshold()
        spring.setEndValue(1);

        ObjectAnimator hide = ObjectAnimator.ofFloat(zoomIcon, "translationY", 0f, -zoomIcon.getMeasuredHeight()*2);
        hide.setDuration(200);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(zoomIcon, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(zoomIcon, "scaleY", 1f);

        scaleDownX.setDuration(200);
        scaleDownY.setDuration(200);
        hide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                zoomIcon.setImageResource(R.mipmap.ic_zoom);
                zoomIcon.setVisibility(View.GONE);

                zoomOutBtn.setVisibility(View.GONE);
                zoomInBtn.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleDownX).with(scaleDownY).with(hide);
        animatorSet.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        onPause = true;
    }

    private void goToZoomMode(){
        isInZoomMode = true;

        mVerticalPager.setPagingEnabled(false);
        overviewCompositeFragment.setHorizontalPagingEnabled(false);

        if(isOverlayShown)
            hideOverlay();

        //enable zoom
        //Log.d("test","size:"+overviewCompositeFragment.getChildFragmentManager().getFragments().size());
        for(int i=0; i<overviewCompositeFragment.getChildFragmentManager().getFragments().size(); i++){
            //Log.d("test",overviewCompositeFragment.getChildFragmentManager().getFragments().get(i).getClass().getSimpleName());
            ((OverviewFragment)overviewCompositeFragment.getChildFragmentManager().getFragments().get(i)).setZoomEnabled(true);
        }

        //flip zoom icon
        final Animation flipStart = AnimationUtils.loadAnimation(this, R.anim.flip_start);
        final Animation flipEnd = AnimationUtils.loadAnimation(this, R.anim.flip_end);
        Animation.AnimationListener animationListener = new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                zoomIcon.setImageResource(R.mipmap.ic_zoom_cancel);
                zoomIcon.clearAnimation();
                zoomIcon.startAnimation(flipEnd);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        flipStart.setAnimationListener(animationListener);
        zoomIcon.startAnimation(flipStart);

        zoomIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInZoomMode = false;

                //disable zoom
                for(int i=0; i<overviewCompositeFragment.getChildFragmentManager().getFragments().size(); i++){
                    ((OverviewFragment)overviewCompositeFragment.getChildFragmentManager().getFragments().get(i)).setZoomEnabled(false);
                }

                overviewCompositeFragment.setHorizontalPagingEnabled(true);
                mVerticalPager.setPagingEnabled(true);
                removeZoomIcon();
            }
        });

        //set visibility and set on click listener
        zoomOutBtn.setVisibility(View.VISIBLE);
        zoomInBtn.setVisibility(View.VISIBLE);
        View.OnClickListener zoomListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == zoomInBtn.getId()){
                    Log.d("test","zoominnnn");
                    ((OverviewFragment)overviewCompositeFragment.getChildFragmentManager().getFragments().get(OverviewCompositeFragment.position)).zoomIn();
                } else if(v.getId() == zoomOutBtn.getId()){
                    Log.d("test","zoomoutttt");
                    ((OverviewFragment)overviewCompositeFragment.getChildFragmentManager().getFragments().get(OverviewCompositeFragment.position)).zoomOut();
                }
            }
        };
        zoomOutBtn.setOnClickListener(zoomListener);
        zoomInBtn.setOnClickListener(zoomListener);

        //spring animation to zoom buttons
        Spring spring = springSystem.createSpring();

        spring.addListener(new SimpleSpringListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - value;
                value = value * value * value;

                zoomOutBtn.setScaleX(value);
                zoomOutBtn.setScaleY(value);

                zoomInBtn.setScaleX(value);
                zoomInBtn.setScaleY(value);

                overviewCompositeFragment.setViewPagerIndicatorScale(scale);
            }
        });
        //spring.setRestSpeedThreshold()
        spring.setEndValue(1);
    }

}
