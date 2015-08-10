package com.molaja.android.fragment.maintab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.molaja.android.R;
import com.molaja.android.fragment.HangoutFragment;

/**
 * Created by florianhidayat on 6/8/15.
 */
public class FirstTabFragment extends MainTabFragment {

    private static String LOG_TAG = FirstTabFragment.class.getSimpleName();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        /*if (savedFragmentInstance != null) {
            Log.d(LOG_TAG, "has saved fr instance " + savedFragmentInstance.getClass().getSimpleName());

            getChildFragmentManager().beginTransaction()
                    .remove(savedFragmentInstance)
                    .commit();

            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_main_tab_container, savedFragmentInstance)
                    .commit();

            savedFragmentInstance = null;
        }*/ if (!hasHangOutFragment()) {
            Log.d(LOG_TAG, "doesn't have saved fr instance");
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_main_tab_container, new HangoutFragment())
                    //.addToBackStack(HangoutFragment.class.getSimpleName())
                    .commit();
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private boolean hasHangOutFragment() {
        if (getChildFragmentManager().getFragments() == null)
            return false;

        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof HangoutFragment)
                return true;
        }

        return false;
    }

    @Override
    public void onStop() {

        //savedFragmentInstance = getChildFragmentManager().findFragmentById(R.id.fragment_main_tab_container);

        super.onStop();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onsaveinst");

        super.onSaveInstanceState(outState);


    }
}
