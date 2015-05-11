package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverTabAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by florianhidayat on 6/5/15.
 */
public class DiscoverCompositeFragment extends Fragment {
    View fragmentView;
    ViewPager viewPager;
    DiscoverTabAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_discover_composite, container, false);

        initViews();
        return fragmentView;
    }

    private void initViews() {
        viewPager = (ViewPager) fragmentView.findViewById(R.id.pager);
        mAdapter = new DiscoverTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);
        SmartTabLayout viewPagerTab = (SmartTabLayout) fragmentView.findViewById(R.id.viewpager_tab);
        viewPagerTab.setViewPager(viewPager);
    }
}
