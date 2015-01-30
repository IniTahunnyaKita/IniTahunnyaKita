package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.NotificationItem;
import com.kitekite.initahunnyakita.widget.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Florian on 1/30/2015.
 */
public class NotificationAdapter extends ArrayAdapter<NotificationItem> {
    ArrayList<NotificationItem> items;
    Context mContext;

    public NotificationAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    public NotificationAdapter(Context context, int resource, ArrayList<NotificationItem> list) {
        super(context, resource, list);
        this.items = list;
        this.mContext = context;
        //this.resources = mContext.getResources();
    }

    public Object getChild(int childPosition) {
        // TODO Auto-generated method stub
        return items.get(childPosition);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        View v = convertView;
        if(v==null){
            viewHolder = new ViewHolder();
            LayoutInflater vi= LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.notification_item, null);
            viewHolder.profilePic = (RoundedImageView) v.findViewById(R.id.notif_item_profile_picture);
            viewHolder.fullname = (TextView) v.findViewById(R.id.notif_item_username);
            viewHolder.action = (TextView) v.findViewById(R.id.notif_item_action);
            viewHolder.itemPic = (ImageView) v.findViewById(R.id.notif_item_item_picture);
            viewHolder.content = (TextView) v.findViewById(R.id.notif_item_content);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        Picasso.with(mContext)
                .load(items.get(position).getProfileUrl())
                .error(R.drawable.ensa_shop)
                .tag(mContext)
                .into(viewHolder.profilePic);
        viewHolder.fullname.setText(items.get(position).getFullname());
        viewHolder.action.setText(items.get(position).getAction());
        Picasso.with(mContext)
                .load(items.get(position).getItemUrl())
                .error(R.drawable.ensa_shop)
                .tag(mContext)
                .into(viewHolder.itemPic);
        viewHolder.content.setText(items.get(position).getContent());
        return v;
    }

    static class ViewHolder{
        RoundedImageView profilePic;
        TextView fullname;
        TextView action;
        ImageView itemPic;
        TextView content;
    }

}
