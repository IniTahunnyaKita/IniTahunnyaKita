package com.molaja.android.fragment.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.adapter.NotificationAdapter;
import com.molaja.android.model.NotificationItem;
import com.molaja.android.util.HardcodeValues;

import java.util.ArrayList;

/**
 * Created by florianhidayat on 8/5/15.
 */
public class NotificationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.simple_recycler_view, null);
        initViews(fragmentView);
        return fragmentView;
    }

    private void initViews(View v) {
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        ArrayList<NotificationItem> list = new ArrayList<>();
        for (int i=0;i< HardcodeValues.NotificationItems.fullnames.length;i++){
            NotificationItem item = new NotificationItem();
            item.setFullname(HardcodeValues.NotificationItems.fullnames[i]);
            item.setAction(NotificationItem.ACTION_TYPE_COMMENT);
            item.setContent(HardcodeValues.NotificationItems.contents[i]);
            item.setProfileUrl(HardcodeValues.NotificationItems.profileUrls[i]);
            item.setItemUrl(HardcodeValues.NotificationItems.itemUrls[i]);
            list.add(item);
        }
        NotificationAdapter adapter = new NotificationAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
    }
}
