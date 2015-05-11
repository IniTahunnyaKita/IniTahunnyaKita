package com.molaja.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.molaja.android.R;

import java.util.List;
import java.util.Random;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class ShowBuddiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final int FAKE_HEADER = 0;
    protected final int REAL_CONTENT = 1;

    Context context;
    List<String> list;
    int headerHeight;

    public ShowBuddiesAdapter(Context context, List<String> list, int headerHeight) {
        this.context = context;
        this.list = list;
        this.headerHeight = headerHeight;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("buaya","viewtype:"+viewType);
        if (viewType == FAKE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.fake_profile_header, parent, false);
            view.getLayoutParams().height = headerHeight;
            return new DummyViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.child_buddies_list, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return FAKE_HEADER;
        else
            return REAL_CONTENT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.buddyName.setText("Buddy "+list.get(position));
            viewHolder.buddyUsername.setText("username" + list.get(position));

            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            int randomColor = Color.rgb(r, g, b);

            viewHolder.buddyImage.setImageDrawable(new ColorDrawable(randomColor));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView buddyName;
        protected TextView buddyUsername;
        protected ImageView buddyImage;

        public ViewHolder(View itemView) {
            super(itemView);
            buddyName = (TextView) itemView.findViewById(R.id.buddy_name);
            buddyUsername = (TextView) itemView.findViewById(R.id.buddy_username);
            buddyImage = (ImageView) itemView.findViewById(R.id.buddy_image);
        }
    }

    static class DummyViewHolder extends RecyclerView.ViewHolder {

        public DummyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
