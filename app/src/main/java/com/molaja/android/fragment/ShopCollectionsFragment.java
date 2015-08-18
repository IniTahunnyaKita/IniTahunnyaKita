package com.molaja.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.molaja.android.R;
import com.molaja.android.activities.ShopActivity;
import com.molaja.android.adapter.ShopCollectionsAdapter;

/**
 * Created by florianhidayat on 10/8/15.
 */
public class ShopCollectionsFragment extends BaseFragment {

    private static final String LOG_TAG = ShopCollectionsFragment.class.getSimpleName();
    private boolean mSearchBarHidden = false;

    RecyclerView mRecyclerView;
    EditText searchBar;

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        final int offset = 100;
        int displacement = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            displacement += dy;

            Log.d(LOG_TAG, "hidden?"+mSearchBarHidden+"displacement:" + displacement + " dy:" + dy);

            if (0 < displacement) {
                if (displacement < 200 && !mSearchBarHidden)
                    ((CardView) searchBar.getParent()).animate().y(-displacement).setDuration(0).start();
                else
                    mSearchBarHidden = true;
            } else if (displacement < 0) {
                if (-200 < displacement && mSearchBarHidden)
                    ((CardView) searchBar.getParent()).animate().y(-displacement).setDuration(0).start();
                else
                    mSearchBarHidden = false;
            }

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                displacement = 0;

            Log.d(LOG_TAG, "scrollstate:" + newState);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_shop_collections, container, false);
        setFragmentView(fragmentView);
        initViews();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    private void initViews() {
        //find views
        mRecyclerView = (RecyclerView) findViewById(R.id.collections_view);
        searchBar = (EditText) findViewById(R.id.search_bar);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecyclerView.setAdapter(new ShopCollectionsAdapter(ShopActivity.convertArgsToShop(getArguments())));

    }
}
