package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.ItemDetailActivity;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.HardcodeValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by florian on 2/5/2015.
 */
public class ItemDetailPagerAdapter extends FragmentPagerAdapter {

    private List<Class<? extends Fragment>> mPagesClasses;
    private Context mContext;
    ArrayList<String> imageUrls = new ArrayList<String>(Arrays.asList(HardcodeValues.ItemDetailValues.imageUrls));
    HangoutPost itemInfo = ItemDetailActivity.getItemInfo();

    public ItemDetailPagerAdapter(FragmentManager fm, Context context,List<Class<? extends Fragment>> pages, HangoutPost itemInfo) {
        super(fm);
        mContext = context;
        mPagesClasses = pages;

        if (itemInfo != null) {
            imageUrls.remove(0);
            imageUrls.add(0, itemInfo.getItemUrl());
        }
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("url",imageUrls.get(position));
        if(position==0)
            bundle.putString("transitionName", mContext.getResources().getString(R.string.item_detail_transition));

        return Fragment.instantiate(mContext, mPagesClasses.get(position).getName(),bundle);
    }

    @Override
    public int getCount() {
        return mPagesClasses.size();
    }
}
