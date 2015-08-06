package com.molaja.android.fragment.thebag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.molaja.android.R;
import com.molaja.android.adapter.ShowBuddiesAdapter;
import com.molaja.android.model.BusEvent.GetBuddiesEvent;
import com.molaja.android.model.BusEvent.ScrollEvent;
import com.molaja.android.model.User;
import com.molaja.android.widget.TheBagPagerFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florianhidayat on 20/4/15.
 */
public class ShowBuddiesFragment extends TheBagPagerFragment {
    View fragmentView;
    RecyclerView recyclerView;

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
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        //recyclerView.setAdapter(new ShowBuddiesAdapter(getActivity(), list, headerHeight));

        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onEvent(ScrollEvent event) {
        super.onEvent(event);
        recyclerView.scrollBy(0, event.scroll - scrolledY);
    }

    public void onEvent(GetBuddiesEvent event) {
        Type type = new TypeToken<List<User>>() {}.getType();
        List<User> list = new Gson().fromJson(event.jsonObject.get("entries"), type);

        //for dummy view
        if (list != null)
            list.add(0, new User());
        else
            list = new ArrayList<>();

        recyclerView.setAdapter(new ShowBuddiesAdapter(list, headerHeight));
    }
}
