package com.molaja.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.util.BackendHelper;

import java.util.List;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    protected final int FAKE_HEADER = 0;
    protected final int SETTINGS_TITLE = 1;
    protected final int SETTINGS_OPTION = 2;

    Context context;
    List<String> list;
    String [] settings;

    public SettingsAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        settings = context.getResources().getStringArray(R.array.settings_options);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("buaya","viewtype:"+viewType);
        if (viewType == FAKE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.fake_profile_header, parent, false);
            return new DummyViewHolder(view);
        } else if (viewType == SETTINGS_TITLE){
            view = LayoutInflater.from(context).inflate(R.layout.child_settings_title, parent, false);
            return new TitleViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.child_settings_option, parent, false);
            return new OptionViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return FAKE_HEADER;
        else if (list.get(position).contains("Title:"))
            return SETTINGS_TITLE;
        else
            return SETTINGS_OPTION;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            viewHolder.title.setText(list.get(position).replace("Title:",""));
        } else if (holder instanceof OptionViewHolder) {
            OptionViewHolder viewHolder = (OptionViewHolder) holder;
            viewHolder.option.setText(list.get(position));
            viewHolder.itemView.setTag(list.get(position));
            viewHolder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        Log.d("onclick","tag"+view.getTag());
        if (view.getTag().equals(settings[settings.length - 1])) {//log out
            BackendHelper.logOut(context);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.settings_title);
        }
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        protected TextView option;

        public OptionViewHolder(View itemView) {
            super(itemView);
            option = (TextView) itemView.findViewById(R.id.settings_option);
        }
    }

    static class DummyViewHolder extends RecyclerView.ViewHolder {

        public DummyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
