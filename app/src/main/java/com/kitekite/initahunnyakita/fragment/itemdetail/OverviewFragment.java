package com.kitekite.initahunnyakita.fragment.itemdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kitekite.initahunnyakita.R;
import com.squareup.picasso.Picasso;

/**
 * Created by tinklabs on 2/5/2015.
 */
public class OverviewFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_item_detail_overview, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getActivity())
                .load(getArguments().getString("url"))
                .into((ImageView) view.findViewById(R.id.overview_img));
    }
}
