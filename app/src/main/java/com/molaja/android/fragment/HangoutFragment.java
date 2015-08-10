package com.molaja.android.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.molaja.android.R;
import com.molaja.android.adapter.HangoutAdapter;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.util.HardcodeValues;

import java.util.ArrayList;


/**
 * Created by Florian on 1/3/2015.
 */
public class HangoutFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    ArrayList<HangoutPost> list;

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_hangout, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        initRecyclerView();
    }

    public void initRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

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
        HangoutAdapter mAdapter= new HangoutAdapter(list);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        },3000);
    }

}
