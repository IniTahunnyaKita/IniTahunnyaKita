package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.molaja.android.R;
import com.molaja.android.adapter.SearchViewAdapter;
import com.molaja.android.util.BackendHelper;
import com.molaja.android.util.Validations;
import com.molaja.android.widget.BaseFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by florianhidayat on 6/5/15.
 */
public class SearchFragment extends BaseFragment {
    View fragmentView;
    RecyclerView searchView;
    SearchViewAdapter searchViewAdapter;

    EditText searchBox;
    ImageView searchIcon;

    String query;
    List<SearchViewAdapter.SearchResult> list;
    Handler searchHandler;
    Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && !Validations.isEmptyOrNull(query)) {
                Log.d("SearchFragment","hit");
                BackendHelper.searchForUser(getActivity(), query, 1, new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            Type type = new TypeToken<List<SearchViewAdapter.SearchResult>>() { }.getType();
                            list = new Gson().fromJson(result.get("entries"), type);
                            searchViewAdapter = new SearchViewAdapter(list, getActivity());
                            searchView.setAdapter(searchViewAdapter);
                        }
                    }
                });
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        initViews();
        return fragmentView;
    }

    private void initViews() {
        searchBox = (EditText) fragmentView.findViewById(R.id.search_box);
        searchIcon = (ImageView) fragmentView.findViewById(R.id.search_icon);
        searchView = (RecyclerView) fragmentView.findViewById(R.id.search_view);
        //init search view
        searchView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView.setItemAnimator(new FadeInAnimator());
        searchViewAdapter = new SearchViewAdapter(list = new ArrayList<>(), getActivity());
        searchView.setAdapter(searchViewAdapter);

        searchHandler = new Handler();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = searchBox.getText().toString();

                int visibility = Validations.isEmptyOrNull(query)? View.VISIBLE : View.GONE;
                searchIcon.setVisibility(visibility);

                if (!Validations.isEmptyOrNull(query)) {
                    Log.d("searchfr", "query not empty");
                    if (list.isEmpty() || list.get(0).TYPE != SearchViewAdapter.SearchResult.LOADING_TYPE) {
                        Log.d("searchfr", "list size:"+list.size());
                        list.add(0, new SearchViewAdapter.SearchResult(SearchViewAdapter.SearchResult.LOADING_TYPE));
                        searchViewAdapter.notifyItemInserted(0);
                    }
                } else if (!list.isEmpty() && list.get(0).TYPE == SearchViewAdapter.SearchResult.LOADING_TYPE) {
                    list.remove(0);
                    searchViewAdapter.notifyItemRemoved(0);
                }
                searchHandler.removeCallbacks(searchRunnable);
                searchHandler.postDelayed(searchRunnable, 1000);
            }
        });
    }
}
