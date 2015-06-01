package com.molaja.android.fragment.discover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverItemsAdapter;
import com.molaja.android.model.DiscoverItem;
import com.molaja.android.util.HardcodeValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverItemsFragment extends Fragment{
    private static final int SPAN_COUNT = 3;
    private Context mContext;
    private List<DiscoverItem> list;

    //TODO delete later
    String [] categoryNames = new String[]{"Fashion","Beauty","Games & Toys","Bags","Kids","Home","Gadgets","Electronics","Sports","Books"};
    String [] backgrounds = new String[]{"file:///android_asset/categories/fashion.jpg","file:///android_asset/categories/beauty.jpg","file:///android_asset/categories/toys.jpg",
            "file:///android_asset/categories/bags.jpg","file:///android_asset/categories/kids.jpg","file:///android_asset/categories/home.jpg","file:///android_asset/categories/gadgets.jpg",
            "file:///android_asset/categories/electronics.jpg","file:///android_asset/categories/sports.jpg","file:///android_asset/categories/books.jpg"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.simple_recycler_view,container,false);
        mContext = getActivity().getApplicationContext();

        initGridView(fragment);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initGridView(View v){

        //init list
        list = new ArrayList<>();
        for(int i=0; i<categoryNames.length; i++){
            DiscoverItem.Category category = new DiscoverItem.Category();
            category.category_title = categoryNames[i];
            category.category_bg = backgrounds[i];
            list.add(category);

            for (String item : HardcodeValues.discoverItems) {
                DiscoverItem discoverItem = new DiscoverItem();
                discoverItem.image = item;
                list.add(discoverItem);
            }
        }

        RecyclerView gridView = (RecyclerView) v.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (list.get(position) instanceof DiscoverItem.Category ||
                        position != 0 && list.get(position - 1) instanceof DiscoverItem.Category)
                    return 3;

                return 1;
            }
        });
        layoutManager.setStackFromEnd(false);
        gridView.setLayoutManager(layoutManager);

        gridView.setAdapter(new DiscoverItemsAdapter(list));

        /*GridView gridView = (GridView) v.findViewById(R.id.gridview);
        mAdapter = new DiscoverItemsAdapter(getActivity());
        gridView.setAdapter(mAdapter);*/
    }
}
