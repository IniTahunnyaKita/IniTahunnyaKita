package com.molaja.android.widget;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;

import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.fragment.thebag.TheBagFragment;

/**
 * Created by florianhidayat on 22/4/15.
 */
public class BaseFragment extends Fragment {
    public static final int MODE_ACTIONBAR_DEFAULT = 1;
    public static final int MODE_ACTIONBAR_PROFILE = 2;

    public int actionBarMode = MODE_ACTIONBAR_DEFAULT;

    private View fragmentView;

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            updateToolbarElevation(mainActivity);
            if (this instanceof TheBagFragment) {
                mainActivity.setToolbarTransparent();
            } else {
                mainActivity.setToolbarDefault();
                //setTitleAlpha(1f, getResources().getColor(R.color.Teal));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.DarkTeal));
            }
        }
    }

    public void setFragmentView(View fragmentView) {
        this.fragmentView = fragmentView;
    }

    /**
     * Look for a view inside this fragment.
     * @param id id of the view to look for.
     * @return the view that has the given id in the hierarchy or null.
     */
    public View findViewById(int id) {
        return fragmentView.findViewById(id);
    }

    /**
     * Look for a child with the given tag in the fragment.
     * @param tag tag of the view to look for.
     * @return the view that has the given tag in the hierarchy or null.
     */
    public View findViewWithTag(Object tag) {
        return fragmentView.findViewWithTag(tag);
    }

    /**
     * get the specified color based on id
     * @param colorId the id of the color to look for.
     * @return color value in form of 0xAARRGGBB.
     */
    public int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    private void updateToolbarElevation(MainActivity mainActivity) {
        if (this instanceof TheBagFragment) {
            mainActivity.setToolbarElevation(0);
        } else {
            mainActivity.setToolbarElevation(getResources().getDimensionPixelSize(R.dimen.default_elevation));
        }
    }

    public void setActionBarColor(int color) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarColor(color, false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(MolajaApplication.darkenColor(color, 0.3f));
            }

        }
    }

    public void setTitleAlpha(float alpha, int color) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolbarAlpha(alpha, color);
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
