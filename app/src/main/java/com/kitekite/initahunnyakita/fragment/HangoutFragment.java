package com.kitekite.initahunnyakita.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.OnHeaderClickListener;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.HangoutAdapter;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.HardcodeValues;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Florian on 1/3/2015.
 */
public class HangoutFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnHeaderClickListener {
    private SuperRecyclerView mRecyclerView;
    private Context mContext;
    private SharedPreferences loginCookies;
    private String fullname;
    ArrayList<HangoutPost> list;

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
        HangoutAdapter mAdapter= new HangoutAdapter(getActivity(),list, this);
        mAdapter.setHasStableIds(true);
        HangoutAdapter.HeaderAdapter headerAdapter= new HangoutAdapter.HeaderAdapter(list);
        StickyHeadersItemDecoration decoration = new StickyHeadersBuilder()
                .setAdapter(mAdapter)
                .setRecyclerView(mRecyclerView.getRecyclerView())
                .setStickyHeadersAdapter(headerAdapter)
                .setOnHeaderClickListener(this)
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


    @Override
    public void onHeaderClick(View view, long l) {
        CircleImageView profilePic = (CircleImageView) view.getTag();
        String transitionTag = getResources().getString(R.string.profile_transition);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.profileUrl = list.get((int)l).getProfileUrl();


        if(profilePic instanceof ImageView){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("taikodok", "tralalala" + transitionTag);
                profilePic.setTransitionName(transitionTag);
                setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
                //setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
                profileFragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
                //profileFragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.slide_bottom));
            }
        }
        ((ActionBarActivity) mContext).getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.tabcontent, profileFragment, "PROFILE")
                .addToBackStack("PROFILE")
                .addSharedElement(profilePic, transitionTag)
                .commit();
    }
}
