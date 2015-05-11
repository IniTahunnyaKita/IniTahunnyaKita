package com.molaja.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.model.DiscoverShops;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.widget.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Florian on 2/23/2015.
 */
public class DiscoverShopsAdapter extends ArrayAdapter<DiscoverShops> implements View.OnClickListener {
    Context mContext;
    ArrayList<DiscoverShops> items;

    public DiscoverShopsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public DiscoverShopsAdapter(Context context, int resource, ArrayList<DiscoverShops> list) {
        super(context, resource, list);
        this.items = list;
        this.mContext = context;
    }

    public Object getChild(int childPosition) {
        return items.get(childPosition);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        ViewHolder viewHolder;
        if(v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.child_discover_shops, null);
        }

        viewHolder = new ViewHolder();
        viewHolder.profilePicture = (ImageView) v.findViewById(R.id.profile_picture);
        viewHolder.shopName = (TextView) v.findViewById(R.id.shop_name);
        viewHolder.picture1 = (SquareImageView) v.findViewById(R.id.shop_image1);
        viewHolder.picture2 = (SquareImageView) v.findViewById(R.id.shop_image2);
        viewHolder.picture3 = (SquareImageView) v.findViewById(R.id.shop_image3);
        viewHolder.picture1Btn = (ImageButton) v.findViewById(R.id.shop_image1_button);
        viewHolder.picture2Btn = (ImageButton) v.findViewById(R.id.shop_image2_button);
        viewHolder.picture3Btn = (ImageButton) v.findViewById(R.id.shop_image3_button);
            //v.setTag(viewHolder);
        /*} else {
            viewHolder = (ViewHolder) v.getTag();
        }*/
        Picasso.with(mContext)
                .load(items.get(position).profileUrl)
                .fit().centerCrop()
                .placeholder(new ColorDrawable(mContext.getResources().getColor(R.color.LightGrey)))
                .into(viewHolder.profilePicture);

        viewHolder.shopName.setText(items.get(position).shopName);

        Picasso.with(mContext)
                .load(items.get(position).picture1)
                .fit().centerCrop()
                .into(viewHolder.picture1);
        Picasso.with(mContext)
                .load(items.get(position).picture2)
                .fit().centerCrop()
                .into(viewHolder.picture2);
        Picasso.with(mContext)
                .load(items.get(position).picture3)
                .fit().centerCrop()
                .into(viewHolder.picture3);

        //assign implemented click listener
        v.setOnClickListener(this);
        viewHolder.picture1Btn.setOnClickListener(this);
        viewHolder.picture2Btn.setOnClickListener(this);
        viewHolder.picture3Btn.setOnClickListener(this);

        v.setTag(position);
        viewHolder.picture1Btn.setTag(position);
        viewHolder.picture2Btn.setTag(position);
        viewHolder.picture3Btn.setTag(position);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.child_discover_shop_parent:
                break;
            case R.id.shop_image1_button:
                goToItemDetailFragment(v, (Integer) v.getTag(), 1);
                break;
            case R.id.shop_image2_button:
                goToItemDetailFragment(v, (Integer) v.getTag(), 2);
                break;
            case R.id.shop_image3_button:
                goToItemDetailFragment(v, (Integer) v.getTag(), 3);
                break;
        }
    }

    private void goToItemDetailFragment(View v, int position, int whichPicture) {
        String itemUrl;
        switch (whichPicture) {
            case 1:
                itemUrl = items.get(position).picture1;
                break;
            case 2:
                itemUrl = items.get(position).picture2;
                break;
            case 3:
                itemUrl = items.get(position).picture3;
                break;
            default:
                return;
        }
        Gson gson = new Gson();
        HangoutPost hangoutPost = new HangoutPost();
        hangoutPost.setTitle("DiscoverShops");
        hangoutPost.setPrice(Integer.parseInt((100+new Random().nextInt(399))+"000"));
        hangoutPost.setItemUrl(itemUrl);
        hangoutPost.setFullname(items.get(position).shopName);
        hangoutPost.setProfileUrl(items.get(position).profileUrl);
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

    static class ViewHolder{
        ImageView profilePicture;
        TextView shopName;
        SquareImageView picture1;
        SquareImageView picture2;
        SquareImageView picture3;
        ImageButton picture1Btn;
        ImageButton picture2Btn;
        ImageButton picture3Btn;
    }

}
