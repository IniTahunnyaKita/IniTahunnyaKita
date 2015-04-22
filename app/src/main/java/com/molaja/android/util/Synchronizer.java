package com.molaja.android.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florianhidayat on 22/4/15.
 */
public class Synchronizer {

    private static Synchronizer instance;

    private List<Synchronizable> synchronizableList;

    private Synchronizer() {}

    public static Synchronizer getInstance() {
        if (instance == null)
            instance = new Synchronizer();
        return instance;
    }

    public void registerSynchronizable(Synchronizable synchronizable) {
        if(synchronizableList == null)
            synchronizableList = new ArrayList<>();

        synchronizableList.add(synchronizable);
    }

    public void update(Synchronizable sender, int update) {
        //if(update < 0) update = 0;

        for(Synchronizable s : synchronizableList) {
            if(s != sender)
                s.onUpdate(update);
        }
    }

    public interface Synchronizable {
        public void onUpdate(int update);
    }
}