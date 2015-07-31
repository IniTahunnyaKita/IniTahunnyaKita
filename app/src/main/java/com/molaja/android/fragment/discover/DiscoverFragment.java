package com.molaja.android.fragment.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.molaja.android.R;
import com.molaja.android.activities.SearchActivity;
import com.molaja.android.adapter.DiscoverTabAdapter;
import com.molaja.android.fragment.BaseFragment;
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

    public DiscoverFragment() {
        Log.d("tab constructor", "isadded?"+isAdded());

    }

    @Override
    public void onResume() {
        Log.d("tab onresume", "isadded?"+isAdded());
        super.onResume();
    }

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentUtil.switchFragment(getFragmentManager(), android.R.id.tabcontent,
                        new SearchActivity(), null);*/
                startActivity(new Intent(v.getContext(), SearchActivity.class));

                if (getActivity() != null)
                    getActivity().overridePendingTransition(0, 0);
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
