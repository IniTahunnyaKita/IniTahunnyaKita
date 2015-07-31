package com.molaja.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscussionAdapter;
import com.molaja.android.model.Discussion;
import com.molaja.android.util.HardcodeValues;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by Florian on 2/25/2015.
 */
public class DiscussionFragment extends BaseFragment {
    Toolbar toolbar;
    RecyclerView recyclerView;
    DiscussionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discussion, null);
        setFragmentView(fragmentView);

        bindViews();

        initToolbar();

        initRecyclerView();

        return fragmentView;
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.menu_discussion);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    
                }
                return false;
            }
        });
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.discussion_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.discussion_list);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.getItemAnimator().setAddDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(300);

        List<Discussion> discussions = new ArrayList<>();
        for (int j=0;j<3;j++) {
            String suffix = "";
            switch (j) {
                case 0:
                    suffix = "";
                    break;
                case 1:
                    suffix = " gg";
                    break;
                case 2:
                    suffix = " cc";
            }
            for (int i = 0; i < HardcodeValues.Discussions.names.length; i++) {
                Discussion discussion = new Discussion();
                discussion.name = HardcodeValues.Discussions.names[i] + suffix;
                discussion.profile_url = HardcodeValues.Discussions.profileUrls[i];
                discussion.no_of_discussions = HardcodeValues.Discussions.CONVERSATIONS[i].length;
                discussion.discussions = HardcodeValues.Discussions.CONVERSATIONS[i];
                discussions.add(discussion);
            }
        }

        mAdapter = new DiscussionAdapter(discussions);
        recyclerView.setAdapter(mAdapter);
    }

    public boolean onBackPressed(){
        //do nothing if recycler view is animating
        if (mAdapter.isAnimating())
            return true;

        if(mAdapter.isAnItemSelected()){
            mAdapter.restoreItemsCompat();
            return true;
        }
        return false;
    }

}
