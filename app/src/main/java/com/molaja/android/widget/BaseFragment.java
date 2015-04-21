package com.molaja.android.widget;

import android.support.v4.app.Fragment;

import com.molaja.android.activities.MainActivity;
import com.molaja.android.fragment.thebag.TheBagFragment;

/**
 * Created by florianhidayat on 22/4/15.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (this instanceof TheBagFragment) {
                mainActivity.setActionBarTransparent();
            } else {
                mainActivity.setActionBarDefault();
                setTitleAlpha(1f);
            }
        }
    }

    public void setTitleAlpha(float alpha) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setActionBarAlpha(alpha);
        }

    }

}
