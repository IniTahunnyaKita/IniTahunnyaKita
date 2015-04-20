package com.molaja.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.molaja.android.fragment.thebag.ActivitiesFragment;
import com.molaja.android.fragment.thebag.SettingsFragment;
import com.molaja.android.fragment.thebag.ShowBuddiesFragment;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class TheBagTabAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "ACTIVITIES", "BUDDIES", "SETTINGS" };

    public TheBagTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ActivitiesFragment();
            case 1:
                return new ShowBuddiesFragment();
            case 2:
                return new SettingsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
