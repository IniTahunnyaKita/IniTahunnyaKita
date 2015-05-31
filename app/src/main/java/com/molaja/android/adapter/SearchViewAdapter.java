package com.molaja.android.adapter;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    List<SearchResult> list;

    public SearchViewAdapter(List<SearchResult> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DEFAULT_VIEW_TYPE:
                return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.child_buddies_list, parent, false));
            case LOADING_VIEW_TYPE:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.child_loading_view, parent, false));
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
            Picasso.with(holder.itemView.getContext())
                    .load(image)
                    .placeholder(new ColorDrawable(holder.itemView.getContext().getResources().getColor(R.color.LightGrey)))
                    .into(viewHolder.image);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME", list.get(position).username);
                    MolajaApplication.showKeyboard(v.getContext(), viewHolder.itemView, false);

                    if (v.getContext() instanceof FragmentActivity)
                        FragmentUtil.switchFragment(((FragmentActivity) v.getContext()).getSupportFragmentManager(),
                                android.R.id.tabcontent,
                                new TheBagFragment(),
                                bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).TYPE == SearchResult.LOADING_TYPE)
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
        View progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class SearchResult extends User {
        public static int DEFAULT_TYPE = 1;
        public static int LOADING_TYPE = 2;

        public int TYPE;

        public SearchResult() {
            TYPE = DEFAULT_TYPE;
        }

        public SearchResult(int type) {
            TYPE = type;
        }
    }
}
