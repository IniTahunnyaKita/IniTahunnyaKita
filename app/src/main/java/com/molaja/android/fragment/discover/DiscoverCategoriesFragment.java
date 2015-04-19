package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverCategoriesAdapter;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverCategoriesFragment extends Fragment {
    private GridView gridView;
    private DiscoverCategoriesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment =  inflater.inflate(R.layout.fragment_discover_categories,container,false);
        initGridView(fragment);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initGridView(View v){
        gridView = (GridView) v.findViewById(R.id.gridview);
        mAdapter = new DiscoverCategoriesAdapter(getActivity());
        gridView.setAdapter(mAdapter);
    }

}
