package com.kitekite.initahunnyakita.fragment.discover;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.DiscoverTabAdapter;

/**
 * Created by Florian on 2/10/2015.
 */
public class DiscoverFragment extends Fragment{
    public static final String TAB_1_TAG = "ITEMS";
    public static final String TAB_2_TAG = "SHOPS";
    public static final String TAB_3_TAG = "CATEGORIES";
    FragmentTabHost mTabHost;
    ViewPager viewPager;
    DiscoverTabAdapter mAdapter;
    PagerTabStrip pagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discover, container, false);
        initPager(fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initPager(View v){
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        mAdapter = new DiscoverTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tabstrip);
        //pagerTabStrip.setBackgroundResource(R.drawable.tab_indicator);
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f);
        pagerTabStrip.setTabIndicatorColorResource(R.color.DarkRed);
    }
}
