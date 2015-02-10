package com.kitekite.initahunnyakita.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.MainActivity;
import com.kitekite.initahunnyakita.R;

/**
 * Created by Florian on 1/17/2015.
 */
public class MainFragmentTab extends Fragment {
    public static final String TAB_1_TAG = "HANG_OUT";
    public static final String TAB_2_TAG = "DISCOVER";
    public static final String TAB_3_TAG = "DISCUSSION";
    public static final String TAB_4_TAG = "THE BAG";
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
            if(!getFragmentManager().findFragmentByTag(TAB_1_TAG).isVisible()) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new HangoutFragment(),TAB_1_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        }
        if(getTag().equals(TAB_2_TAG)){
            actionBar.setCustomView(R.layout.custom_actionbar_discover);
            if(!getFragmentManager().findFragmentByTag(TAB_2_TAG).isVisible()) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DiscoverFragment(),TAB_2_TAG)
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
}