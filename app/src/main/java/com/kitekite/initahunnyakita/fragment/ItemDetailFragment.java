package com.kitekite.initahunnyakita.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitekite.initahunnyakita.R;

/**
 * Created by Florian on 2/4/2015.
 */
public class ItemDetailFragment extends Fragment{

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        mContext = container.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

}
