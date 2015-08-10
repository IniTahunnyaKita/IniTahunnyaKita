package com.molaja.android.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.fragment.thebag.TheBagFragment;

/**
 * Created by florianhidayat on 22/4/15.
 */
public class BaseFragment extends Fragment {
    private View fragmentView;

    @Override
    public void onResume() {
        super.onResume();

        Log.d("BaseFragment", "onresume");
        /*for (Fragment f : getFragmentManager().getFragments()) {
            Log.d("BaseFragment",f.getClass().getSimpleName());
        }*/

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

    /**
     * pass arguments to the fragment.
     * @param args the arguments to pass.
     * @return the fragment itself.
     */
    public BaseFragment passArguments(Bundle args) {
        setArguments(args);
        return this;
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
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            int actionBarMode = mainActivity.getActionBarMode();
            if (actionBarMode == mode)
                return;

            ((MainActivity)getActivity()).switchToProfileActionBar(mode == MainActivity.MODE_ACTIONBAR_PROFILE);
        }
    }

}
