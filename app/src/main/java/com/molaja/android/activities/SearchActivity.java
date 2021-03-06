package com.molaja.android.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.molaja.android.R;
import com.molaja.android.adapter.SearchViewAdapter;
import com.molaja.android.adapter.TrendingViewAdapter;
import com.molaja.android.util.BackendHelper;
import com.molaja.android.util.SpaceItemDecoration;
import com.molaja.android.util.Validations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by florianhidayat on 6/5/15.
 */
public class SearchActivity extends BaseActivity {
    View emptyView;
    RecyclerView searchView, trendingView;
    SearchViewAdapter searchViewAdapter;

    EditText searchBox;
    ImageView searchIcon;

    List<SearchViewAdapter.SearchResult> list;

    Handler searchHandler;
    Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {

            Log.d("SearchFragment","hit");
            BackendHelper.searchForUser(SearchActivity.this, getQuery(), 1, new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e == null) {
                        Type type = new TypeToken<List<SearchViewAdapter.SearchResult>>() {
                        }.getType();
                        list = new Gson().fromJson(result.get("entries"), type);
                        searchViewAdapter = new SearchViewAdapter(list);
                        searchView.setAdapter(searchViewAdapter);

                        if (list.isEmpty()) {
                            Snackbar snackbar = Snackbar.make(searchView,
                                    R.string.search_did_not_match_anything,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getColor(R.color.DarkTeal));
                            snackbar.show();
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        initToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        setFragmentView(fragmentView);

        mContext = getActivity().getApplicationContext();

        initViews();

        return fragmentView;
    }*/

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        emptyView = findViewById(R.id.empty_view);
        searchBox = (EditText) findViewById(R.id.search_box);
        searchIcon = (ImageView) findViewById(R.id.search_icon);
        searchView = (RecyclerView) findViewById(R.id.search_view);
        trendingView = (RecyclerView) findViewById(R.id.trending_view);

        findViewById(R.id.trending_searches_text).setAlpha(0f);
        findViewById(R.id.trending_searches_text).animate().alpha(1f).setDuration(1000).start();

        //init search view
        searchView.setLayoutManager(new LinearLayoutManager(this));
        searchView.setItemAnimator(new FadeInAnimator());
        searchViewAdapter = new SearchViewAdapter(list = new ArrayList<>());
        searchView.setAdapter(searchViewAdapter);

        //init trending view
        trendingView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        trendingView.addItemDecoration(new SpaceItemDecoration(10,10,0,150).setColumnCount(3));
        List<String> trends = new ArrayList<>();
        trends.add("Jeans");
        trends.add("Cardigan");
        trends.add("Olivia Lazuardy");
        trends.add("OOTD");
        trends.add("Erigo");
        trends.add("Samsung");
        trends.add("Hoodie");
        trends.add("Outfit of the year");
        trends.add("Leather Jacket");
        TrendingViewAdapter trendingViewAdapter = new TrendingViewAdapter(trends,
                new TrendingViewAdapter.TrendClickListener() {
            @Override
            public void onClick(String trend) {
                searchBox.setText(trend);
            }
        });
        trendingView.setAdapter(trendingViewAdapter);

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
                int visibility = Validations.isEmptyOrNull(getQuery()) ? View.VISIBLE : View.GONE;
                emptyView.setVisibility(visibility);

                searchQuery();
            }
        });
    }

    private String getQuery() {
        return searchBox.getText().toString();
    }

    private void searchQuery() {
        if (!Validations.isEmptyOrNull(getQuery())) {
            Log.d("searchfr", "query not empty");
            if (list.isEmpty() || list.get(0).TYPE != SearchViewAdapter.SearchResult.LOADING_TYPE) {
                Log.d("searchfr", "list size:" + list.size());
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
}
