package com.kitekite.initahunnyakita.fragment.itemdetail;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.grantlandchew.view.VerticalPager;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.DebugPostValues;
import com.kitekite.initahunnyakita.util.EventBus;
import com.kitekite.initahunnyakita.util.PageChangedEvent;
import com.nineoldandroids.animation.Animator;
import com.squareup.otto.Subscribe;

/**
 * Created by tinklabs on 2/5/2015.
 */
public class ItemDetailFragment extends Fragment {

    private static final int CENTRAL_PAGE_INDEX = 0;

    // -----------------------------------------------------------------------
    //
    // Fields
    //
    // -----------------------------------------------------------------------
    private VerticalPager mVerticalPager;
    private TextView priceOverlay;
    private static HangoutPost itemInfo;
    public static boolean isOverlayShown = true;

    // -----------------------------------------------------------------------
    //
    // Methods
    //
    // -----------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Gson gson = new Gson();
        itemInfo = gson.fromJson(getArguments().getString("ITEM_INFO"),HangoutPost.class);
        View fragmentView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        findViews(fragmentView);
        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
        YoYo.with(Techniques.SlideOutDown)
                .duration(800)
                .playOn(container.getRootView().findViewById(android.R.id.tabhost));
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        mVerticalPager = (VerticalPager) fragmentView.findViewById(R.id.main_vertical_pager);
        priceOverlay = (TextView) fragmentView.getRootView().findViewById(R.id.overlay_price);
        priceOverlay.setText(itemInfo.getPrice());
        initViews();
    }

    private void initViews() {
        snapPageWhenLayoutIsReady(mVerticalPager, CENTRAL_PAGE_INDEX);
        mVerticalPager.setOnTapListener(new VerticalPager.OnTapListener() {
            @Override
            public void onTap() {
                final View overlay = mVerticalPager.getRootView().findViewById(R.id.short_detail_overlay);
                if (isOverlayShown) {
                    YoYo.with(Techniques.FadeOut)
                            .duration(200)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
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
                    isOverlayShown = false;
                } else {
                    overlay.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn)
                            .duration(200)
                            .playOn(overlay);
                    isOverlayShown = true;
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

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getInstance().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onLocationChanged(PageChangedEvent event) {
        mVerticalPager.setPagingEnabled(event.hasVerticalNeighbors());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleOverlay = (TextView) view.findViewById(R.id.overlay_title);
        titleOverlay.setText(itemInfo.getTitle());
        hideOverlay();
    }

    public static HangoutPost getItemInfo(){
        return itemInfo;
    }

    public void hideOverlay(){
        final View overlay = getActivity().findViewById(R.id.short_detail_overlay);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOverlayShown){
                    YoYo.with(Techniques.FadeOut)
                            .duration(200)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
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
                    isOverlayShown = false;
                }
            }
        },2000);
    }
}
