package com.kitekite.initahunnyakita.fragment.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitekite.initahunnyakita.R;

/**
 * Created by florianhidayat on 9/4/15.
 */
public class MainSignupFragment extends Fragment {
    View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_main_signup, null);
        return fragmentView;
    }
}
