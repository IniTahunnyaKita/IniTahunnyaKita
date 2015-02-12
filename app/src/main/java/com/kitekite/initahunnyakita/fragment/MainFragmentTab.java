package com.kitekite.initahunnyakita.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.kitekite.initahunnyakita.MainActivity;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.fragment.discover.DiscoverFragment;
import com.kitekite.initahunnyakita.fragment.itemdetail.TheBagFragment;

/**
 * Created by Florian on 1/17/2015.
 */
public class MainFragmentTab extends Fragment {
    public static final String TAB_1_TAG = "TAB 1";
    public static final String TAB_2_TAG = "TAB 2";
    public static final String TAB_3_TAG = "TAB 3";//trending/stories?
    public static final String TAB_4_TAG = "TAB 4";
    public static MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        mainActivity = MainActivity.getMainActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if(getTag().equals(TAB_1_TAG)){
            actionBar.setCustomView(R.layout.custom_actionbar_default);
            if(!getFragmentManager().findFragmentByTag(mainActivity.HANG_OUT_TAG).isVisible()) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new HangoutFragment(),MainActivity.HANG_OUT_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        }else if(getTag().equals(TAB_2_TAG)){
            //actionBar.setCustomView(R.layout.custom_actionbar_discover);
            DiscoverFragment discoverFragment = (DiscoverFragment) getFragmentManager().findFragmentByTag(mainActivity.DISCOVER_TAG);
            if(discoverFragment==null || (discoverFragment!=null && !discoverFragment.isVisible())) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DiscoverFragment(),MainActivity.DISCOVER_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        } else if(getTag().equals(TAB_4_TAG)){
            TheBagFragment theBagFragment = (TheBagFragment) getFragmentManager().findFragmentByTag(mainActivity.THE_BAG_TAG);
            if(theBagFragment==null || (theBagFragment!=null && !theBagFragment.isVisible())) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new TheBagFragment(),MainActivity.THE_BAG_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            actionBar.setCustomView(R.layout.custom_actionbar_default);
        }
        //setActionBarTitle(this.getTag());
    }

    public void setActionBarTitle(String tagName){
        Log.d("taikodok","fragmenttab tag:"+getTag());
        mainActivity = MainActivity.getMainActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        /*TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title);
        if(tagName.equals(TAB_1_TAG))
            title.setText("Hang Out");
        else if(tagName.equals(TAB_2_TAG))
            title.setText("Discover");
        else if(tagName.equals(TAB_3_TAG))
            title.setText("Trending");
        else if(tagName.equals(TAB_4_TAG))
            title.setText("The Bag");*/
    }

    /*public void popFragment(){
        if (getChildFragmentManager().getBackStackEntryCount()>0) {
            getChildFragmentManager().popBackStack();
        }
    }*/
}