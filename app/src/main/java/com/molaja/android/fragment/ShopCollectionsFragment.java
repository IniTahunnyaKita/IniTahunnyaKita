package com.molaja.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;

/**
 * Created by florianhidayat on 10/8/15.
 */
public class ShopCollectionsFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_shop_collections, container, false);
        setFragmentView(fragmentView);
        initViews();

        return fragmentView;
    }

    private void initViews() {



    }
}
