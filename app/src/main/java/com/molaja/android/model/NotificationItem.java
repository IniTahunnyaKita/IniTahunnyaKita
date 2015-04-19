package com.molaja.android.model;

import android.net.Uri;

/**
 * Created by Florian on 1/30/2015.
 */
public class NotificationItem {
    Uri profileUrl;
    String fullname;
    String action;
    Uri itemUrl;
    String content;
    public final static int ACTION_TYPE_COMMENT = 1;
    public final static int ACTION_TYPE_LIKE = 2;
    public final static int ACTION_TYPE_DELIVERY = 3;

    public void setProfileUrl(String url){
        profileUrl = Uri.parse(url);
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public void setAction(int action){
        switch (action){
            case ACTION_TYPE_COMMENT:
                this.action = "commented on";
                break;
            case ACTION_TYPE_LIKE:
                this.action = "likes your photo.";
                break;
            case ACTION_TYPE_DELIVERY:
                this.action = "";
                break;
        }
    }

    public void setItemUrl(String url){
        this.itemUrl = Uri.parse(url);
    }

    public void setContent(String content){
        this.content = content;
    }

    public Uri getProfileUrl(){
        return this.profileUrl;
    }

    public String getFullname(){
        return this.fullname;
    }

    public String getAction(){
        return action;
    }

    public Uri getItemUrl(){
        return this.itemUrl;
    }

    public String getContent() {
        return content;
    }
}
