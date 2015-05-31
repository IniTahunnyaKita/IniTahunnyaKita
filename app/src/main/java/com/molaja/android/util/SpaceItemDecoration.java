package com.molaja.android.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by florianhidayat on 30/5/15.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int left, right, top, bottom;
    private int columnCount;

    public SpaceItemDecoration(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public SpaceItemDecoration setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;

        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildAdapterPosition(view) > columnCount)
            outRect.top = top;
    }
}