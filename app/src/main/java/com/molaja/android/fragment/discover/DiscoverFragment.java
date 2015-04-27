package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.molaja.android.R;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.adapter.DiscoverTabAdapter;
import com.molaja.android.widget.BaseFragment;
import com.molaja.android.widget.CustomEditText;
import com.molaja.android.widget.RevealLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Florian on 2/10/2015.
 */
public class DiscoverFragment extends BaseFragment {
    public static final String TAB_1_TAG = "ITEMS";
    public static final String TAB_2_TAG = "SHOPS";
    public static final String TAB_3_TAG = "CATEGORIES";

    //views
    ViewPager viewPager;
    RevealLayout revealLayout;
    RecyclerView searchView;
    CustomEditText searchBox;

    DiscoverTabAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discover, container, false);
        initViews(fragmentView);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d("buaya","current focus?"+getActivity().getCurrentFocus());
        return fragmentView;
    }

    public void initViews(View v) {
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        searchBox = (CustomEditText) v.findViewById(R.id.search_box);
        searchView = (RecyclerView) v.findViewById(R.id.search_view);
        revealLayout = (RevealLayout) v.findViewById(R.id.discover_reveal_layout);

        mAdapter = new DiscoverTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);

        //init search view
        searchView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpager_tab);
        viewPagerTab.setViewPager(viewPager);

        searchBox.setKeyboardListener(new CustomEditText.SoftKeyboardListener() {
            @Override
            public void onHide() {
                Log.d("buaya","onhide");
                //searchBox.clearFocus();
                //searchView.requestFocus();
                //searchBox.clearFocus();
                Log.d("buaya","current focus1?"+getActivity().getCurrentFocus());
                ((MainActivity) getActivity()).setFocusToTabHost();
                searchBox.clearFocus();
                Log.d("buaya", "current focus2?" + getActivity().getCurrentFocus());

            }
        });

        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("buaya","has focus?"+hasFocus);
                if (hasFocus) {
                    revealLayout.next();
                }
            }
        });
    }
}
