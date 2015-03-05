package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.fragment.itemdetail.ItemDetailFragment;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.util.HardcodeValues;
import com.kitekite.initahunnyakita.widget.SquareImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Florian on 2/15/2015.
 */
public class DiscoverItemsAdapter extends BaseAdapter {
    private Context mContext;

    public DiscoverItemsAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return HardcodeValues.discoverItems.length;
    }

    @Override
    public Object getItem(int position) {
        return HardcodeValues.discoverItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        SquareImageView image;

        if(v == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.child_discover_items,null);
            image = (SquareImageView) v.findViewById(R.id.image);
            v.setTag(image);
        } else {
            image = (SquareImageView) v.getTag();
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                HangoutPost hangoutPost = new HangoutPost();
                hangoutPost.setTitle("DiscoverItem");
                hangoutPost.setPrice(690000);
                hangoutPost.setItemUrl(HardcodeValues.discoverItems[position]);
                bundle.putString("ITEM_INFO", gson.toJson(hangoutPost));
                itemDetailFragment.setArguments(bundle);
                ((ActionBarActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, itemDetailFragment, "ITEM_DETAIL")
                        .addToBackStack("ITEM_DETAIL")
                        .commit();
            }
        });

        image.setFilteringEnabled(true);

        Picasso.with(mContext)
                .load(HardcodeValues.discoverItems[position])
                .fit().centerCrop()
                .into(image);
        return v;
    }
}
