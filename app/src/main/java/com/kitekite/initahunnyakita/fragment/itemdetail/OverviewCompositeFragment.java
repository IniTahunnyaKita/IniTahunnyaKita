package com.kitekite.initahunnyakita.fragment.itemdetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.ItemDetailPagerAdapter;
import com.kitekite.initahunnyakita.util.DebugPostValues;
import com.kitekite.initahunnyakita.util.EventBus;
import com.kitekite.initahunnyakita.util.PageChangedEvent;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;

/**
 * Created by Florian on 2/5/2015.
 */
public class OverviewCompositeFragment extends Fragment{
    private ViewPager mHorizontalPager;
    private int mCentralPageIndex = 0;
    private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            EventBus.getInstance().post(new PageChangedEvent(mCentralPageIndex == position));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

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

    private void findViews(View fragmentView) {
        mHorizontalPager = (ViewPager) fragmentView.findViewById(R.id.fragment_composite_central_pager);
        initViews();
    }

    private void initViews() {
        populateHorizontalPager();
        mHorizontalPager.setCurrentItem(mCentralPageIndex);
        mHorizontalPager.setOnPageChangeListener(mPagerChangeListener);
    }

    private void populateHorizontalPager() {
        ArrayList<Class<? extends Fragment>> pages = new ArrayList<Class<? extends Fragment>>();
        for(int i=0;i< DebugPostValues.ItemDetailValues.imageUrls.length;i++)
            pages.add(OverviewFragment.class);
        mHorizontalPager.setAdapter(new ItemDetailPagerAdapter(getChildFragmentManager(), getActivity(), pages));
    }

}
