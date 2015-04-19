package com.molaja.android.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.molaja.android.R;
import com.molaja.android.adapter.DiscoverShopsAdapter;
import com.molaja.android.util.HardcodeValues;

import java.util.ArrayList;

/**
 * Created by Florian on 2/11/2015.
 */
public class DiscoverShopsFragment extends Fragment{
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_discover_shops,container,false);
        listView = (ListView) fragmentView.findViewById(android.R.id.list);
        initListView();
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initListView(){
        ArrayList<com.molaja.android.model.DiscoverShops> shops = new ArrayList<>();
        for(int i=0; i< HardcodeValues.DiscoverShops.shopNames.length; i++){
            com.molaja.android.model.DiscoverShops shop = new com.molaja.android.model.DiscoverShops();
            shop.shopName = HardcodeValues.DiscoverShops.shopNames[i];
            shop.profileUrl = HardcodeValues.DiscoverShops.profilePictures[i];
            shop.picture1 = HardcodeValues.DiscoverShops.picture1[i];
            shop.picture2 = HardcodeValues.DiscoverShops.picture2[i];
            shop.picture3 = HardcodeValues.DiscoverShops.picture3[i];
            shops.add(shop);
        }
        DiscoverShopsAdapter adapter = new DiscoverShopsAdapter(getActivity(), 0, shops);
        listView.setAdapter(adapter);
    }
}
