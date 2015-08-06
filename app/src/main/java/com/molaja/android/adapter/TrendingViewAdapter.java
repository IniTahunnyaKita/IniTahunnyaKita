package com.molaja.android.adapter;

import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.widget.CustomTextView;

import java.util.List;

/**
 * Created by florianhidayat on 30/5/15.
 */
public class TrendingViewAdapter extends RecyclerView.Adapter<TrendingViewAdapter.ViewHolder> {
    protected final int ANIMATION_DELAY = 150;
    List<String> list;
    TrendClickListener clickListener;

    public interface TrendClickListener {
        void onClick(String trend);
    }

    public TrendingViewAdapter(List<String> list, TrendClickListener clickListener) {
        this.list = list;
        this.clickListener = clickListener;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CustomTextView textView = new CustomTextView(parent.getContext());
        textView.setTextColor(parent.getContext().getResources().getColor(R.color.Teal));
        textView.setTextSize(20f);
        textView.setTypeface(CustomTextView.Roboto.LIGHT);
        textView.setGravity(Gravity.CENTER);

        //setbackground
        int [] attrs = new int[] {R.attr.selectableItemBackgroundBorderless};
        TypedArray ta = parent.getContext().obtainStyledAttributes(attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            textView.setBackground(ta.getDrawable(0));
        else
            textView.setBackgroundDrawable(ta.getDrawable(0));
        ta.recycle();

        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ((TextView)holder.itemView).setText(list.get(position));

        //create alpha animation
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1).setStartDelay(position * ANIMATION_DELAY).start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
