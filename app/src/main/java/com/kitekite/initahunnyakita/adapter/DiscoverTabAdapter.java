package com.kitekite.initahunnyakita.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kitekite.initahunnyakita.fragment.discover.DiscoverCategoriesFragment;
import com.kitekite.initahunnyakita.fragment.discover.DiscoverItemsFragment;
import com.kitekite.initahunnyakita.fragment.discover.DiscoverShopsFragment;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverTabAdapter extends FragmentPagerAdapter {

    private String tabtitles[] = new String[] { "Items", "Shops", "Categories" };

    public DiscoverTabAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DiscoverItemsFragment();
            case 1:
                return new DiscoverShopsFragment();
            case 2:
                return new DiscoverCategoriesFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
