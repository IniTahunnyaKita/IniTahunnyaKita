package com.molaja.android.fragment.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.molaja.android.R;
import com.molaja.android.adapter.RequestsAdapter;
import com.molaja.android.util.BackendHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by florianhidayat on 8/5/15.
 */
public class RequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    View loadingView, emptyView;
    RequestsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_notification_requests, container, false);
        initViews(fragmentView);
        return fragmentView;
    }

    private void initViews(View v) {
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        loadingView = v.findViewById(R.id.loading_view);
        emptyView = v.findViewById(R.id.empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setItemAnimator(new FadeInAnimator());

        refreshLayout.setOnRefreshListener(this);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        BackendHelper.getPendingRequests(getActivity(), 1, new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                loadingView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);

                if (e == null) {
                    Type type = new TypeToken<List<RequestsAdapter.Request>>() {}.getType();
                    List<RequestsAdapter.Request> list = new Gson().fromJson(result.get("entries"), type);

                    //if list is null,
                    list = list==null? new ArrayList<RequestsAdapter.Request>() : list;

                    mAdapter = new RequestsAdapter(getActivity(), emptyView, list);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

}
