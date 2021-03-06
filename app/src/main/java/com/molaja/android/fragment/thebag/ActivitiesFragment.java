package com.molaja.android.fragment.thebag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.ActivitiesAdapter;
import com.molaja.android.model.BusEvent.ScrollEvent;
import com.molaja.android.widget.TheBagPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class ActivitiesFragment extends TheBagPagerFragment{
    View fragmentView;
    RecyclerView recyclerView;
    LinearLayoutManager llm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.simple_recycler_view, null);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view);
        initRecyclerView();

        return fragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView.scrollBy(0, -scrolledY);
    }

    private void initRecyclerView() {
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        List<String> list = new ArrayList<>();
        for (int i=0; i<80; i++) {
            list.add("position "+i);
        }
        recyclerView.setAdapter(new ActivitiesAdapter(getActivity(), list, headerHeight));
        Log.d("elf","scrolltoppos");

        recyclerView.addOnScrollListener(onScrollListener);

    }

    @Override
    public void onEvent(ScrollEvent event) {
        super.onEvent(event);
        recyclerView.scrollBy(0, event.scroll - scrolledY);
    }
}
