package com.kitekite.initahunnyakita.model;

import android.net.Uri;

/**
 * Created by Florian on 1/4/2015.
 */
public class HangoutPost {
    Uri profileUrl;
    String fullname;
    String title;
    String overview;
    Uri itemUrl;
    int thumbsUp;
    String price;

    public void setProfileUrl(String url){
        profileUrl = Uri.parse(url);
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setOverview(String overview){
        this.overview = overview;
    }

    public void setItemUrl(String url){
        this.itemUrl = Uri.parse(url);
    }

    public void setThumbsUp(int value){
        this.thumbsUp = value;
    }

    public void setPrice(String currency, int value){
        this.price = currency+". "+value;
        //this.price = price.
    }

    public Uri getProfileUrl(){
        return this.profileUrl;
    }

    public String getFullname(){
        return this.fullname;
    }

    public String getTitle(){
        return this.title;
    }

    public String getOverview(){
        return this.overview;
    }

    public Uri getItemUrl(){
        return this.itemUrl;
    }

    public int getThumbsUp(){
        return this.thumbsUp;
    }

    public String getPrice(){
        return this.price;
    }
}
