package com.molaja.android.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by florianhidayat on 2/5/15.
 */
public class FragmentUtil {

    /**
     * switch to another fragment inside the specified fragment container.
     * @param fm the Fragment Manager to handle transaction.
     * @param containerId container's id.
     * @param fragment the fragment to switch to.
     * @param bundle optional arguments.
     */
    public static void switchFragment(FragmentManager fm, int containerId, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

}
