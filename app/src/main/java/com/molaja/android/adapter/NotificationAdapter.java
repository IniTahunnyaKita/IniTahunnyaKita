package com.molaja.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.model.NotificationItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Florian on 1/30/2015.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<NotificationItem> items;
    Context mContext;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> list) {
        this.items = list;
        this.mContext = context;
        //this.resources = mContext.getResources();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        Picasso.with(mContext)
                .load(items.get(position).getProfileUrl())
                .error(R.drawable.ensa_shop)
                .fit().centerCrop()
                .into(viewHolder.profilePic);
        viewHolder.fullname.setText(items.get(position).getFullname());
        viewHolder.action.setText(items.get(position).getAction());
        Picasso.with(mContext)
                .load(items.get(position).getItemUrl())
                .error(R.drawable.ensa_shop)
                .fit().centerCrop()
                .into(viewHolder.itemPic);
        viewHolder.content.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView fullname;
        TextView action;
        ImageView itemPic;
        TextView content;

        public ViewHolder(View v) {
            super(v);

            profilePic = (CircleImageView) v.findViewById(R.id.notif_item_profile_picture);
            fullname = (TextView) v.findViewById(R.id.notif_item_username);
            action = (TextView) v.findViewById(R.id.notif_item_action);
            itemPic = (ImageView) v.findViewById(R.id.notif_item_item_picture);
            content = (TextView) v.findViewById(R.id.notif_item_content);
        }
    }

}
