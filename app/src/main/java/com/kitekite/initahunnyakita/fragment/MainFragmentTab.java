package com.kitekite.initahunnyakita.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.MainActivity;
import com.kitekite.initahunnyakita.fragment.discover.DiscoverFragment;

/**
 * Created by Florian on 1/17/2015.
 */
public class MainFragmentTab extends Fragment {
    public static final String TAB_1_TAG = "TAB 1";
    public static final String TAB_2_TAG = "TAB 2";
    public static final String TAB_3_TAG = "TAB 3";//trending/stories?
    public static final String TAB_4_TAG = "TAB 4";
    public MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        mainActivity = (MainActivity) getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if(getTag().equals(TAB_1_TAG)){
            actionBar.setCustomView(R.layout.custom_actionbar_default);
            if(!getFragmentManager().findFragmentByTag(MainActivity.HANG_OUT_TAG).isVisible()) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new HangoutFragment(),MainActivity.HANG_OUT_TAG)
                        .addToBackStack(MainActivity.HANG_OUT_TAG)
                        .commit();
            }
        }else if(getTag().equals(TAB_2_TAG)){
            //actionBar.setCustomView(R.layout.custom_actionbar_discover);
            DiscoverFragment discoverFragment = (DiscoverFragment) getFragmentManager().findFragmentByTag(MainActivity.DISCOVER_TAG);
            if(discoverFragment==null || (discoverFragment!=null && !discoverFragment.isVisible())) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DiscoverFragment(),MainActivity.DISCOVER_TAG)
                        .addToBackStack(MainActivity.DISCOVER_TAG)
                        .commit();
            }
        } else if(getTag().equals(TAB_3_TAG)){
            DiscussionFragment discussionFragment = (DiscussionFragment) getFragmentManager().findFragmentByTag(MainActivity.DISCUSSION_TAG);
            if(discussionFragment==null || (discussionFragment!=null && !discussionFragment.isVisible())){
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DiscussionFragment(),MainActivity.DISCUSSION_TAG)
                        .addToBackStack(MainActivity.DISCUSSION_TAG)
                        .commit();
            }
        } else if(getTag().equals(TAB_4_TAG)){
            TheBagFragment theBagFragment = (TheBagFragment) getFragmentManager().findFragmentByTag(MainActivity.THE_BAG_TAG);
            if(theBagFragment==null || (theBagFragment!=null && !theBagFragment.isVisible())) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new TheBagFragment(),MainActivity.THE_BAG_TAG)
                        .addToBackStack(MainActivity.THE_BAG_TAG)
                        .commit();
            }
        } else {
            actionBar.setCustomView(R.layout.custom_actionbar_default);
        }
        //setActionBarTitle(this.getTag());
    }

    /*public void popFragment(){
        if (getChildFragmentManager().getBackStackEntryCount()>0) {
            getChildFragmentManager().popBackStack();
        }
    }*/
}