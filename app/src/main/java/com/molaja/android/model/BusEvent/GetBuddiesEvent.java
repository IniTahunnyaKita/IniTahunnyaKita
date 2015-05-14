package com.molaja.android.model.BusEvent;

import com.google.gson.JsonObject;

/**
 * Created by florianhidayat on 15/5/15.
 */
public class GetBuddiesEvent {
    public JsonObject jsonObject;

    public GetBuddiesEvent(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

}
