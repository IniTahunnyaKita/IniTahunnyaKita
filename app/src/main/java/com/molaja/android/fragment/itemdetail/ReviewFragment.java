package com.molaja.android.fragment.itemdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;

/**
 * Created by florian on 2/5/2015.
 */
public class ReviewFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_item_detail_review, container, false);
        return fragmentView;
    }

}
