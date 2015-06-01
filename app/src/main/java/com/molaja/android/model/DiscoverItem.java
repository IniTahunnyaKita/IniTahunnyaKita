package com.molaja.android.model;

/**
 * Created by florianhidayat on 1/6/15.
 */
public class DiscoverItem {
    public String image;

    public static class Category extends DiscoverItem {
        public String category_title;
        public String category_icon;
        public String category_bg;
    }
}
