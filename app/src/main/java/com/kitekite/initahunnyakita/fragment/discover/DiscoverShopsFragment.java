package com.kitekite.initahunnyakita.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.adapter.DiscoverShopsAdapter;
import com.kitekite.initahunnyakita.model.DiscoverShops;
import com.kitekite.initahunnyakita.util.HardcodeValues;

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
        ArrayList<DiscoverShops> shops = new ArrayList<>();
        for(int i=0; i< HardcodeValues.Discovershops.shopNames.length; i++){
            DiscoverShops shop = new DiscoverShops();
            shop.shopName = HardcodeValues.Discovershops.shopNames[i];
            shop.profileUrl = HardcodeValues.Discovershops.profilePictures[i];
            shop.picture1 = HardcodeValues.Discovershops.picture1[i];
            shop.picture2 = HardcodeValues.Discovershops.picture2[i];
            shop.picture3 = HardcodeValues.Discovershops.picture3[i];
            shops.add(shop);
        }
        DiscoverShopsAdapter adapter = new DiscoverShopsAdapter(getActivity(), 0, shops);
        listView.setAdapter(adapter);
    }
}
