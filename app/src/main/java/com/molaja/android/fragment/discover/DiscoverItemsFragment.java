package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverItemsAdapter;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverItemsFragment extends Fragment{
    DiscoverItemsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_discover_items,container,false);
        initGridView(fragment);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initGridView(View v){
        GridView gridView = (GridView) v.findViewById(R.id.gridview);
        mAdapter = new DiscoverItemsAdapter(getActivity());
        gridView.setAdapter(mAdapter);
    }
}
