package com.molaja.android.widget;

import android.support.v4.app.Fragment;

import com.molaja.android.activities.MainActivity;
import com.molaja.android.fragment.thebag.TheBagFragment;

/**
 * Created by florianhidayat on 22/4/15.
 */
public class BaseFragment extends Fragment {
    public static final int MODE_ACTIONBAR_DEFAULT = 1;
    public static final int MODE_ACTIONBAR_PROFILE = 2;

    public int actionBarMode = MODE_ACTIONBAR_DEFAULT;

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

    public void setActionBarColor(int color) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setActionBarColor(color);
        }
    }

    public void setTitleAlpha(float alpha) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setActionBarAlpha(alpha);
        }

    }

    public void switchActionBarMode(int mode) {
        if (actionBarMode == mode)
            return;

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).switchToProfileActionBar(mode == MODE_ACTIONBAR_PROFILE);
            actionBarMode = mode;
        }
    }

}
