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
public class FragmentTab extends Fragment {
    public static final String TAB_1_TAG = "HANG_OUT";
    public static final String TAB_2_TAG = "DISCOVER";
    public static final String TAB_3_TAG = "TRENDING";//trending/stories?
    public static final String TAB_4_TAG = "THE BAG";
    public static MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        setActionBarTitle(this.getTag());
    }

    public void setActionBarTitle(String tagName){
        Log.d("taikodok","fragmenttab tag:"+getTag());
        mainActivity = MainActivity.getMainActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title);
        if(tagName.equals(TAB_1_TAG))
            title.setText("Hang Out");
        else if(tagName.equals(TAB_2_TAG))
            title.setText("Discover");
        else if(tagName.equals(TAB_3_TAG))
            title.setText("Trending");
        else if(tagName.equals(TAB_4_TAG))
            title.setText("The Bag");
    }
}