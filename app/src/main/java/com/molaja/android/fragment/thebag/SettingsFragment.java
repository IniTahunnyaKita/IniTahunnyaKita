package com.molaja.android.fragment.thebag;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.ActivitiesAdapter;
import com.molaja.android.util.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class SettingsFragment extends Fragment {
    View fragmentView;
    RecyclerView recyclerView;
    Scroller scroller;

    public SettingsFragment setScroller(Scroller scroller) {
        this.scroller = scroller;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.simple_recycler_view, null);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view);
        initRecyclerView();

        return fragmentView;
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        List<String> list = new ArrayList<>();
        for (int i=0;i<20;i++) {
            list.add("position "+i);
        }
        recyclerView.setAdapter(new ActivitiesAdapter(getActivity(), list));

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrolledY = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledY += dy;
                Log.d("onscrolled"," y:"+scrolledY);

                if (scroller != null)
                    scroller.onYScroll(scrolledY);
            }
        });
    }


}
