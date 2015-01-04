package com.kitekite.initahunnyakita;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.kitekite.initahunnyakita.adapter.TimelineAdapter;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.BlurImage;
import com.kitekite.initahunnyakita.util.DebugPostValues;

import java.util.ArrayList;


/**
 * Created by Florian on 1/3/2015.
 */
public class HangoutFragment extends Fragment {
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(android.R.id.list);
        initListView();
    }

    public void initListView(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_profile, mListView,
                false);
        Bitmap blurredImg = BlurImage.BlurBitmap(getActivity(),
                BitmapFactory.decodeResource(getResources(), R.drawable.prof_pic), 20);
        ((ImageView) header.findViewById(R.id.list_bg)).setImageBitmap(blurredImg);
        mListView.addHeaderView(header, null, false);
        mListView.setDividerHeight(0);
        ArrayList<HangoutPost> list = new ArrayList<HangoutPost>();

        for(int i=0;i< DebugPostValues.fullnames.length;i++){
            HangoutPost post = new HangoutPost();
            post.setProfileUrl(DebugPostValues.profileUrls[i]);
            post.setFullname(DebugPostValues.fullnames[i]);
            post.setTitle(DebugPostValues.titles[i]);
            post.setOverview(DebugPostValues.overviews[i]);
            post.setItemUrl(DebugPostValues.itemUrls[i]);
            post.setPrice("Rp",DebugPostValues.prices[i]);
            post.setThumbsUp(DebugPostValues.thumbsUps[i]);
            list.add(post);
        }
        TimelineAdapter mAdapter= new TimelineAdapter(getActivity(),R.layout.list,list);
        mListView.setAdapter(mAdapter);
    }

}
