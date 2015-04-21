package com.molaja.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.molaja.android.R;

import java.util.List;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final int FAKE_HEADER = 0;
    protected final int REAL_CONTENT = 1;

    Context context;
    List<String> list;

    public ActivitiesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("buaya","viewtype:"+viewType);
        if (viewType == FAKE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.fake_profile_header, parent, false);
            return new DummyViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.simple_roboto_textview, parent, false);
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
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.Black));
            viewHolder.tv.setText(list.get(position));
            viewHolder.tv.setTextSize(26);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    static class DummyViewHolder extends RecyclerView.ViewHolder {

        public DummyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
