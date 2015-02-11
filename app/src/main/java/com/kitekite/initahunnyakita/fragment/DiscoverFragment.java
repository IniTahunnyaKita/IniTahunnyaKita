package com.kitekite.initahunnyakita.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;

/**
 * Created by Florian on 2/10/2015.
 */
public class DiscoverFragment extends Fragment{
    public static final String TAB_1_TAG = "ITEMS";
    public static final String TAB_2_TAG = "SHOPS";
    public static final String TAB_3_TAG = "CATEGORIES";
    FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_discover, container, false);
        initTabs(fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void initTabs(View v){
        mTabHost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_1_TAG)), DiscoverFragmentTab.class, null);
        mTabHost.addTab(setIndicator(getActivity(),mTabHost.newTabSpec(TAB_2_TAG)), DiscoverFragmentTab.class, null);
        mTabHost.addTab(setIndicator(getActivity(), mTabHost.newTabSpec(TAB_3_TAG)), DiscoverFragmentTab.class, null);
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item_discover, null);
        //ImageView tabImg = (ImageView) v.findViewById(R.id.tab_background);
        TextView tabTitle = (TextView) v.findViewById(R.id.tab_title);
        String tag = spec.getTag();
        if(tag.equals(TAB_1_TAG)) {
            //tabImg.setBackgroundResource(R.drawable.ic_hangout_tab);
            tabTitle.setText("Items");
        }
        else if(tag.equals(TAB_2_TAG)) {
            //tabImg.setBackgroundResource(R.drawable.ic_discover_tab);
            tabTitle.setText("Shops");
        }
        else if(tag.equals(TAB_3_TAG)){
            //tabImg.setBackgroundResource(R.drawable.ic_discussion_tab);
            tabTitle.setText("Categories");

        }
        return spec.setIndicator(v);
    }
}
