package com.kitekite.initahunnyakita.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.HangoutAdapter;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.util.DebugPostValues;
import com.kitekite.initahunnyakita.widget.ProfileItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * Created by Florian on 1/3/2015.
 */
public class HangoutFragment extends Fragment {
    private StickyListHeadersListView mListView;
    private Context mContext;
    private SharedPreferences loginCookies;
    private String fullname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.list, container, false);
        mContext = container.getContext();
        loginCookies = mContext.getSharedPreferences(Global.login_cookies, 0);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListView = (StickyListHeadersListView) view.findViewById(android.R.id.list);
        initListView();
    }

    public void initListView(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //final View footer = inflater.inflate(R.layout.list_footer, mListView, false);
        //mListView.addFooterView(footer);
        mListView.setDividerHeight(0);
        ArrayList<HangoutPost> list = new ArrayList<HangoutPost>();

        for(int i=0;i< DebugPostValues.fullnames.length;i++){
            HangoutPost post = new HangoutPost();
            post.setProfileUrl(DebugPostValues.profileUrls[i]);
            post.setFullname(DebugPostValues.fullnames[i]);
            post.setTitle(DebugPostValues.titles[i]);
            post.setOverview(DebugPostValues.overviews[i]);
            post.setItemUrl(DebugPostValues.itemUrls[i]);
            post.setPrice(DebugPostValues.prices[i]);
            post.setThumbsUp(DebugPostValues.thumbsUps[i]);
            list.add(post);
        }
        HangoutAdapter mAdapter= new HangoutAdapter(getActivity(),R.layout.list,list);
        mListView.setAdapter(mAdapter);
    }

}
