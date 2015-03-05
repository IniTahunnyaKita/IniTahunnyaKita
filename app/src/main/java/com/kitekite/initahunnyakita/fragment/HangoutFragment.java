package com.kitekite.initahunnyakita.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.HangoutAdapter;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.HardcodeValues;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * Created by Florian on 1/3/2015.
 */
public class HangoutFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SuperRecyclerView mRecyclerView;
    private Context mContext;
    private SharedPreferences loginCookies;
    private String fullname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_hangout, container, false);
        mContext = container.getContext();
        loginCookies = mContext.getSharedPreferences(Global.login_cookies, 0);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (SuperRecyclerView) view.findViewById(R.id.list);
        initRecyclerView();
    }

    public void initRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<HangoutPost> list = new ArrayList<HangoutPost>();

        for(int i=0;i< HardcodeValues.fullnames.length;i++){
            HangoutPost post = new HangoutPost();
            post.setProfileUrl(HardcodeValues.profileUrls[i]);
            post.setFullname(HardcodeValues.fullnames[i]);
            post.setTitle(HardcodeValues.titles[i]);
            post.setOverview(HardcodeValues.overviews[i]);
            post.setItemUrl(HardcodeValues.itemUrls[i]);
            post.setPrice(HardcodeValues.prices[i]);
            post.setThumbsUp(HardcodeValues.thumbsUps[i]);
            list.add(post);
        }
        HangoutAdapter mAdapter= new HangoutAdapter(getActivity(),list);
        mAdapter.setHasStableIds(true);
        HangoutAdapter.HeaderAdapter headerAdapter= new HangoutAdapter.HeaderAdapter(list);

        StickyHeadersItemDecoration decoration = new StickyHeadersBuilder()
                .setAdapter(mAdapter)
                .setRecyclerView(mRecyclerView.getRecyclerView())
                .setStickyHeadersAdapter(headerAdapter)
                .build();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        int darkRed = R.color.DarkRed;
        mRecyclerView.setRefreshingColorResources(darkRed, darkRed, darkRed, darkRed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        },3000);
    }
}
