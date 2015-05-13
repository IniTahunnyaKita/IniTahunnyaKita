package com.molaja.android.fragment.itemdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.adapter.ItemDetailPagerAdapter;
import com.molaja.android.util.HardcodeValues;
import com.molaja.android.util.PageChangedEvent;
import com.molaja.android.widget.SmartViewPager;
import com.molaja.android.widget.ViewPagerIndicator;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Florian on 2/5/2015.
 */
public class OverviewCompositeFragment extends Fragment{
    private SmartViewPager mHorizontalPager;
    private ViewPagerIndicator mViewPagerIndicator;
    private int mCentralPageIndex = 0, totalPage;
    public static int position = 0;
    private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            EventBus.getDefault().post(new PageChangedEvent(mCentralPageIndex == position, position));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    // -----------------------------------------------------------------------
    //
    // Methods
    //
    // -----------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_item_detail_composite, container, false);
        findViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(PageChangedEvent event) {
        position = event.getPosition();

        if(ItemDetailActivity.isInZoomMode){
            OverviewFragment fragment = (OverviewFragment)getChildFragmentManager().getFragments().get(position);
            if(!fragment.canZoom())
                fragment.setZoomEnabled(true);
        }

        mViewPagerIndicator.selectIndicator(position);
    }

    private void findViews(View fragmentView) {
        mHorizontalPager = (SmartViewPager) fragmentView.findViewById(R.id.fragment_composite_central_pager);
        mViewPagerIndicator = (ViewPagerIndicator) fragmentView.findViewById(R.id.viewpager_indicator);
        initViews();
    }

    private void initViews() {
        populateHorizontalPager();
        mHorizontalPager.setCurrentItem(mCentralPageIndex);
        mHorizontalPager.setOnPageChangeListener(mPagerChangeListener);
    }

    private void populateHorizontalPager() {
        ArrayList<Class<? extends Fragment>> pages = new ArrayList<Class<? extends Fragment>>();

        totalPage = HardcodeValues.ItemDetailValues.imageUrls.length;
        for(int i=0;i< totalPage;i++)
            pages.add(OverviewFragment.class);

        //TODO change this
        mHorizontalPager.setAdapter(new ItemDetailPagerAdapter(getChildFragmentManager(), getActivity(), pages, ItemDetailActivity.getItemInfo()));
        mViewPagerIndicator.initIndicators(HardcodeValues.ItemDetailValues.imageUrls.length);
    }

    public void setHorizontalPagingEnabled(boolean enabled){
        mHorizontalPager.setPagingEnabled(enabled);
    }

    public void setViewPagerIndicatorScale(float scale) {
        mViewPagerIndicator.setScaleX(scale);
        mViewPagerIndicator.setScaleY(scale);
    }

}
