package com.kitekite.initahunnyakita.fragment.itemdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.ItemDetailPagerAdapter;
import com.kitekite.initahunnyakita.util.EventBus;
import com.kitekite.initahunnyakita.util.PageChangedEvent;

import java.util.ArrayList;

/**
 * Created by tinklabs on 2/5/2015.
 */
public class ItemDetailFragment extends Fragment {

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

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        mHorizontalPager = (ViewPager) rootView.findViewById(R.id.fragment_composite_central_pager);
        mContext = container.getContext();
        populateHozizontalPager();
        mHorizontalPager.setCurrentItem(mCentralPageIndex);
        mHorizontalPager.setOnPageChangeListener(mPagerChangeListener);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    private void populateHozizontalPager() {
        ArrayList<Class<? extends Fragment>> pages = new ArrayList<Class<? extends Fragment>>();
        pages.add(OverviewFragment.class);
        pages.add(OverviewFragment.class);
        pages.add(OverviewFragment.class);
        mCentralPageIndex = pages.indexOf(OverviewFragment.class);
        mHorizontalPager.setAdapter(new ItemDetailPagerAdapter(getChildFragmentManager(), getActivity(), pages));
    }

}
