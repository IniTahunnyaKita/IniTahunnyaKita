package com.molaja.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.molaja.android.fragment.discover.DiscoverCategoriesFragment;
import com.molaja.android.fragment.discover.DiscoverItemsFragment;
import com.molaja.android.fragment.discover.DiscoverShopsFragment;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverTabAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "CATEGORIES", "ITEMS", "SHOPS" };

    public DiscoverTabAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DiscoverCategoriesFragment();
            case 1:
                return new DiscoverItemsFragment();
            case 2:
                return new DiscoverShopsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
