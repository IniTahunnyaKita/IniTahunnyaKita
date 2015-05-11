package com.molaja.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.molaja.android.fragment.notification.NotificationFragment;
import com.molaja.android.fragment.notification.RequestsFragment;

/**
 * Created by florianhidayat on 8/5/15.
 */
public class NotificationPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "NOTIFICATIONS", "REQUESTS"};

    public NotificationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NotificationFragment();
            case 1:
                return new RequestsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
