package com.molaja.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.molaja.android.fragment.thebag.ActivitiesFragment;
import com.molaja.android.fragment.thebag.SettingsFragment;
import com.molaja.android.fragment.thebag.ShowBuddiesFragment;
import com.molaja.android.util.Scroller;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class TheBagTabAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "ACTIVITIES", "BUDDIES", "SETTINGS" };
    Scroller scroller;
    int headerHeight;
    boolean isCurrentUser;

    public TheBagTabAdapter(FragmentManager fm, Scroller scroller, int headerHeight, boolean isCurrentUser) {
        super(fm);
        this.scroller = scroller;
        this.headerHeight = headerHeight;
        this.isCurrentUser = isCurrentUser;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ActivitiesFragment().setScroller(scroller).setHeaderHeight(headerHeight);
            case 1:
                return new ShowBuddiesFragment().setScroller(scroller).setHeaderHeight(headerHeight);
            case 2:
                return new SettingsFragment().setScroller(scroller).setHeaderHeight(headerHeight);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        if (isCurrentUser)
            return tabTitles.length;
        else
            return tabTitles.length - 1;
    }
}
