package com.molaja.android.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.fragment.thebag.TheBagFragment;
import com.molaja.android.model.User;
import com.molaja.android.util.FragmentUtil;
import com.molaja.android.util.Validations;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by florianhidayat on 2/5/15.
 */
public class SearchViewAdapter extends RecyclerView.Adapter {
    public final int DEFAULT_VIEW_TYPE = 1;
    public final int LOADING_VIEW_TYPE = 2;

    List<User> list;
    Context context;

    public SearchViewAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DEFAULT_VIEW_TYPE:
                return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.child_buddies_list, parent, false));
            case LOADING_VIEW_TYPE:
                return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.child_loading_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LoadingViewHolder) {
            //holder.itemView.getLayoutParams().height = MolajaApplication.dpToPx(50);
        } else if (holder instanceof UserViewHolder){
            final UserViewHolder viewHolder = (UserViewHolder) holder;
            viewHolder.name.setText(list.get(position).name);
            viewHolder.username.setText(list.get(position).username);

            String image = Validations.isEmptyOrNull(list.get(position).image) ?
                    "file:///android_asset/default_profile_picture.jpg" : list.get(position).image;
            Picasso.with(context)
                    .load(image)
                    .placeholder(new ColorDrawable(context.getResources().getColor(R.color.LightGrey)))
                    .into(viewHolder.image);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME", list.get(position).username);
                    MolajaApplication.showKeyboard(context, viewHolder.itemView, false);

                    if (context instanceof FragmentActivity)
                        FragmentUtil.switchFragment(((FragmentActivity) context).getSupportFragmentManager(),
                                android.R.id.tabcontent,
                                new TheBagFragment(),
                                bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count =  list.size();

        if (count == 0) {
            return 1;
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return LOADING_VIEW_TYPE;

        return DEFAULT_VIEW_TYPE;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView username;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.buddy_name);
            username = (TextView) itemView.findViewById(R.id.buddy_username);
            image = (ImageView) itemView.findViewById(R.id.buddy_image);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBarIndeterminate progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
