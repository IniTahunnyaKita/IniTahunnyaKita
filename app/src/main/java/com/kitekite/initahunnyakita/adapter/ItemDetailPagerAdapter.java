package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by florian on 2/5/2015.
 */
public class ItemDetailPagerAdapter extends FragmentPagerAdapter {

    private List<Class<? extends Fragment>> mPagesClasses;
    private Context mContext;


    public ItemDetailPagerAdapter(FragmentManager fm, Context context,List<Class<? extends Fragment>> pages) {
        super(fm);
        mContext = context;
        mPagesClasses = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(mContext, mPagesClasses.get(position).getName());
    }

    @Override
    public int getCount() {
        return mPagesClasses.size();
    }
}
