package com.kitekite.initahunnyakita.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kitekite.initahunnyakita.MainActivity;
import com.kitekite.initahunnyakita.R;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverFragmentTab extends Fragment{


    public static final String TAB_1_TAG = "ITEMS";
    public static final String TAB_2_TAG = "SHOPS";
    public static final String TAB_3_TAG = "CATEGORIES";
    public static MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        /*mainActivity = MainActivity.getMainActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.hide();
        YoYo.with(Techniques.SlideOutDown)
                .duration(800)
                .playOn(mainActivity.findViewById(android.R.id.tabhost).getRootView());*/
    }

    public void setActionBarTitle(String tagName){
        Log.d("taikodok", "fragmenttab tag:" + getTag());
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
