package com.molaja.android.fragment.maintab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.molaja.android.R;
import com.molaja.android.fragment.BaseFragment;

/**
 * Created by florianhidayat on 6/8/15.
 */
public class MainTabFragment extends BaseFragment {

    protected enum TabTags {
        FIRST, SECOND, THIRD, FOURTH
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab, container, false);
    }

}
