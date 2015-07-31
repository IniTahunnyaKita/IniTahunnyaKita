package com.molaja.android.model;

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
        //NumberFormat format = NumberFormat.getCurrencyInstance( new Locale("in", "ID"));
        this.price = Integer.toString(value);

        if (price.length() > 6) {
            price = "Rp " + price.substring(0, price.length() - 6)+"jt";
        } else if (price.length() >3) {
            price = "Rp " + price.substring(0, price.length() - 3)+"k";
        }
        //this.price = format.format(value);
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
