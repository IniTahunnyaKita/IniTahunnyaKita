package com.molaja.android.widget;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.molaja.android.R;
import com.molaja.android.util.Scroller;
import com.molaja.android.util.Synchronizer;

/**
 * Created by florianhidayat on 23/4/15.
 */
public class TheBagPagerFragment extends Fragment implements Synchronizer.Synchronizable {
    Scroller scroller;

    public double SCROLLER_LIMIT;
    public int scrolledY = 0;
    boolean isAutoScrolling = false;

    public TheBagPagerFragment setScroller(Scroller scroller) {
        this.scroller = scroller;
        return this;
    }

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrolledY += dy;
            Log.d("onscrolled2"," y:"+scrolledY);

            if (scroller != null && !isAutoScrolling)
                scroller.onYScroll(scrolledY);

            //send to synchronizer
            if (!isAutoScrolling && scrolledY < 600)
                Synchronizer.getInstance().update(TheBagPagerFragment.this, scrolledY);

            isAutoScrolling = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Synchronizer.getInstance().registerSynchronizable(this);
        SCROLLER_LIMIT = 1.2 * (getActionBarHeight() -
                getResources().getDimensionPixelSize(R.dimen.min_profile_header_height));
    }

    @Override
    public void onUpdate(int update) {
        Log.d("autoscroll", "autoscrolling scrolled y:" + scrolledY + " update:" + update);
        isAutoScrolling = true;
    }

    private int getActionBarHeight() {
        final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }
}
