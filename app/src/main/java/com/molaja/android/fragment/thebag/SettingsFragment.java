package com.molaja.android.fragment.thebag;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.SettingsAdapter;
import com.molaja.android.model.BusEvent.ScrollEvent;
import com.molaja.android.widget.TheBagPagerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class SettingsFragment extends TheBagPagerFragment {
    View fragmentView;
    RecyclerView recyclerView;

    @Override
    public void onPause() {
        super.onPause();
        recyclerView.scrollBy(0, -scrolledY);
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

        List<String> list = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.settings_options)));
        //for dummy view
        list.add(0,"");

        recyclerView.setAdapter(new SettingsAdapter(getActivity(), list, headerHeight));
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onEvent(ScrollEvent event) {
        super.onEvent(event);
        recyclerView.scrollBy(0, event.scroll - scrolledY);
    }
}
