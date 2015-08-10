package com.molaja.android.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.molaja.android.fragment.ShopProfileFragment;
import com.molaja.android.fragment.discover.DiscoverItemsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florianhidayat on 7/8/15.
 */
public class ShopPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "PROFILE", "ITEMS" };

    private List<Bundle> bundleList = new ArrayList<>();

    public ShopPagerAdapter(FragmentManager fm){
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
                return new ShopProfileFragment().passArguments(bundleList.get(0));
            case 1:
                return new DiscoverItemsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    /**
     * Set arguments to the selected fragment.
     * @param args the arguments
     * @param fragmentPosition the position of the desired fragment to pass the arguments into.
     */
    public void setArguments(Bundle args, int fragmentPosition) {
        bundleList.add(fragmentPosition, args);
    }

}
