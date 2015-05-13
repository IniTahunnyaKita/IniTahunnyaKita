package com.molaja.android.model.BusEvent;

/**
 * Created by florianhidayat on 13/5/15.
 */
public abstract class BusEvent {
    public String eventName;

    public BusEvent(String eventName) {
        this.eventName = eventName;
    }
}
