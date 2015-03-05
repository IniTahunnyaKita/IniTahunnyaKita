package com.kitekite.initahunnyakita.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.DiscussionAdapter;
import com.kitekite.initahunnyakita.model.Discussion;
import com.kitekite.initahunnyakita.util.HardcodeValues;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by Florian on 2/25/2015.
 */
public class DiscussionFragment extends Fragment {
    RecyclerView recyclerView;
    DiscussionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discussion, null);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.discussion_list);
        initRecyclerView();
        return fragmentView;
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.getItemAnimator().setAddDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(300);

        List<Discussion> discussions = new ArrayList<>();
        for(int i=0;i< HardcodeValues.Discussions.names.length;i++){
            Discussion discussion = new Discussion();
            discussion.name = HardcodeValues.Discussions.names[i];
            discussion.profile_url = HardcodeValues.Discussions.profileUrls[i];
            discussion.no_of_discussions = HardcodeValues.Discussions.DiscussionChildren[i].length;
            discussion.discussions = HardcodeValues.Discussions.DiscussionChildren[i];
            discussions.add(discussion);
        }for(int i=0;i< HardcodeValues.Discussions.names.length;i++){
            Discussion discussion = new Discussion();
            discussion.name = HardcodeValues.Discussions.names[i]+" gg";
            discussion.profile_url = HardcodeValues.Discussions.profileUrls[i];
            discussion.no_of_discussions = HardcodeValues.Discussions.DiscussionChildren[i].length;
            discussion.discussions = HardcodeValues.Discussions.DiscussionChildren[i];
            discussions.add(discussion);
        }for(int i=0;i< HardcodeValues.Discussions.names.length;i++){
            Discussion discussion = new Discussion();
            discussion.name = HardcodeValues.Discussions.names[i]+" babi";
            discussion.profile_url = HardcodeValues.Discussions.profileUrls[i];
            discussion.no_of_discussions = HardcodeValues.Discussions.DiscussionChildren[i].length;
            discussion.discussions = HardcodeValues.Discussions.DiscussionChildren[i];
            discussions.add(discussion);
        }

        mAdapter = new DiscussionAdapter(recyclerView, getActivity(),discussions);
        recyclerView.setAdapter(mAdapter);
    }

}
