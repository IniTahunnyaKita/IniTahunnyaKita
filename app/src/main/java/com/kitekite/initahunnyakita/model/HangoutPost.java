package com.kitekite.initahunnyakita.model;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Florian on 1/4/2015.
 */
public class HangoutPost {
    String profileUrl;
    String fullname;
    String title;
    String overview;
    String itemUrl;
    int thumbsUp;
    String price;

    public void setProfileUrl(String url){
        profileUrl = url;
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
        this.itemUrl = url;
    }

    public void setThumbsUp(int value){
        this.thumbsUp = value;
    }

    public void setPrice(int value){
        NumberFormat format = NumberFormat.getCurrencyInstance( new Locale("in", "ID"));

        this.price = format.format(value);
        //this.price = price.
    }

    public String getProfileUrl(){
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

    public String getItemUrl(){
        return this.itemUrl;
    }

    public int getThumbsUp(){
        return this.thumbsUp;
    }

    public String getPrice(){
        return this.price;
    }
}
