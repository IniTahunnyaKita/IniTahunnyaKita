package com.molaja.android.widget;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.molaja.android.R;
import com.molaja.android.model.BusEvent.ScrollEvent;
import com.molaja.android.util.Scroller;

import de.greenrobot.event.EventBus;

/**
 * Created by florianhidayat on 23/4/15.
 */
public class TheBagPagerFragment extends Fragment {
    Scroller scroller;

    public double SCROLLER_LIMIT;
    public int scrolledY = 0;
    public int headerHeight = 0;
    boolean isAutoScrolling = false;

    public TheBagPagerFragment setScroller(Scroller scroller) {
        this.scroller = scroller;
        return this;
    }

    public TheBagPagerFragment setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
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
                EventBus.getDefault().post(new ScrollEvent( scrolledY));
                //Synchronizer.getInstance().update(TheBagPagerFragment.this, scrolledY);

            isAutoScrolling = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SCROLLER_LIMIT = 1.2 * (getActionBarHeight() -
                getResources().getDimensionPixelSize(R.dimen.min_profile_header_height));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        //Synchronizer.getInstance().registerSynchronizable(this);
    }

    @Override
    public void onStop() {
        //Synchronizer.getInstance().unregisterSynchronizable(this);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(ScrollEvent event) {
        Log.d("autoscroll", "autoscrolling scrolled y:" + scrolledY + " update:" + event.scroll);
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
