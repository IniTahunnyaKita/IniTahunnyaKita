package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.DiscoverShops;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Florian on 2/23/2015.
 */
public class DiscoverShopsAdapter extends ArrayAdapter<DiscoverShops>{
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
        if(v == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.child_discover_shops, null);
            viewHolder = new ViewHolder();
            viewHolder.profilePicture = (ImageView) v.findViewById(R.id.profile_picture);
            viewHolder.shopName = (TextView) v.findViewById(R.id.shop_name);
            viewHolder.picture1 = (ImageView) v.findViewById(R.id.shop_image1);
            viewHolder.picture2 = (ImageView) v.findViewById(R.id.shop_image2);
            viewHolder.picture3 = (ImageView) v.findViewById(R.id.shop_image3);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        Picasso.with(mContext)
                .load(items.get(position).profileUrl)
                .fit().centerCrop()
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

        return v;
    }

    static class ViewHolder{
        ImageView profilePicture;
        TextView shopName;
        ImageView picture1;
        ImageView picture2;
        ImageView picture3;
    }

}
