package com.molaja.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.util.HardcodeValues;
import com.molaja.android.widget.SquareImageView;
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
                Gson gson = new Gson();
                HangoutPost hangoutPost = new HangoutPost();
                hangoutPost.setTitle("DiscoverItem");
                hangoutPost.setPrice(690000);
                hangoutPost.setItemUrl(HardcodeValues.discoverItems[position]);
                Intent intent = new Intent(mContext, ItemDetailActivity.class);
                intent.putExtra("ITEM_INFO", gson.toJson(hangoutPost));

                if (mContext instanceof Activity) {
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                                    v,   // The view which starts the transition
                                    mContext.getString(R.string.item_detail_transition)    // The transitionName of the view we’re transitioning to
                            );
                    ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
                } else {
                    mContext.startActivity(intent);
                }
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
