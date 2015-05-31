package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverTabAdapter;
import com.molaja.android.util.FragmentUtil;
import com.molaja.android.widget.BaseFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Florian on 2/10/2015.
 */
public class DiscoverFragment extends BaseFragment {
    final int DEFAULT_MODE = 1;
    final int SEARCH_MODE = 2;

    //views
    FloatingActionButton searchButton;
    ViewPager viewPager;

    DiscoverTabAdapter mAdapter;

    int mode = DEFAULT_MODE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discover, container, false);
        initViews(fragmentView);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d("buaya", "current focus?" + getActivity().getCurrentFocus());
        return fragmentView;
    }

    public void initViews(View v) {
        searchButton = (FloatingActionButton) v.findViewById(R.id.search_btn);
        viewPager = (ViewPager) v.findViewById(R.id.pager);

        mAdapter = new DiscoverTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);
        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpager_tab);
        viewPagerTab.setViewPager(viewPager);

        //ViewCompat.setElevation(searchButton, getResources().getDimensionPixelSize(R.dimen.default_elevation));
        searchButton.setRippleColor(getColor(R.color.DarkTeal));

        /*searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("buaya","has focus?"+hasFocus);
                if (hasFocus && mode == DEFAULT_MODE) {
                    mode = SEARCH_MODE;
                    FragmentUtil.switchFragment(getChildFragmentManager(), R.id.frame_container,
                            searchFragment = new SearchFragment(), null);
                }
            }
        });*/

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.switchFragment(getFragmentManager(), android.R.id.tabcontent,
                        new SearchFragment(), null);
            }
        });

    }

    public boolean onBackPressed() {
        /*if (mode == SEARCH_MODE && getChildFragmentManager().getBackStackEntryCount() > 0) {
            mode = DEFAULT_MODE;
            getChildFragmentManager().popBackStack();
            return true;
        }*/
        return false;
    }
}
