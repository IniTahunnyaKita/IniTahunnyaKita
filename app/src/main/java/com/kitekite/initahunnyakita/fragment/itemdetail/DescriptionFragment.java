package com.kitekite.initahunnyakita.fragment.itemdetail;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.ItemDetailActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by florian on 2/5/2015.
 */
public class DescriptionFragment extends Fragment {
    Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_item_detail_description, container, false);
        ImageView shopPicture = (ImageView) fragmentView.findViewById(R.id.description_shop_pic);
        TextView shopName = (TextView) fragmentView.findViewById(R.id.description_shop_name);

        if (ItemDetailActivity.itemInfo != null) {
            Picasso.with(activity)
                    .load(ItemDetailActivity.itemInfo.getProfileUrl())
                    .into(shopPicture);
            shopName.setText(ItemDetailActivity.itemInfo.getFullname());
        }

        return fragmentView;
    }
}
