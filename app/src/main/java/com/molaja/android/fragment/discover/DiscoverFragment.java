package com.molaja.android.fragment.discover;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverTabAdapter;
import com.molaja.android.widget.RevealLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Florian on 2/10/2015.
 */
public class DiscoverFragment extends Fragment {
    public static final String TAB_1_TAG = "ITEMS";
    public static final String TAB_2_TAG = "SHOPS";
    public static final String TAB_3_TAG = "CATEGORIES";

    //views
    ViewPager viewPager;
    RevealLayout revealLayout;
    RecyclerView searchView;
    EditText searchBox;

    DiscoverTabAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discover, container, false);
        initViews(fragmentView);
        return fragmentView;
    }

    public void initViews(View v) {
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        searchBox = (EditText) v.findViewById(R.id.search_box);
        searchView = (RecyclerView) v.findViewById(R.id.search_view);
        revealLayout = (RevealLayout) v.findViewById(R.id.discover_reveal_layout);

        mAdapter = new DiscoverTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);

        //init search view
        searchView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpager_tab);
        viewPagerTab.setViewPager(viewPager);

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout.next();
            }
        });
    }
}
