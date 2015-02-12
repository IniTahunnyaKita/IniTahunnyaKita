package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.Category;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by Florian on 2/12/2015.
 */
public class CategoriesAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<Category> categories = new ArrayList<>();
    String [] categoryNames = new String[]{"Fashion","Beauty","Games & Toys","Bags","Kids","Home","Gadgets","Electronics","Sports","Books"};
    String [] backgrounds = new String[]{"file:///android_asset/categories/fashion.jpg","file:///android_asset/categories/beauty.jpg","file:///android_asset/categories/toys.jpg",
            "file:///android_asset/categories/bags.jpg","file:///android_asset/categories/kids.jpg","file:///android_asset/categories/home.jpg","file:///android_asset/categories/gadgets.jpg",
            "file:///android_asset/categories/electronics.jpg","file:///android_asset/categories/sports.jpg","file:///android_asset/categories/books.jpg"};

    public CategoriesAdapter(Context c){
        mContext = c;
        for(int i=0; i<categoryNames.length; i++){
            Category category = new Category();
            category.title = categoryNames[i];
            category.image = backgrounds[i];
            categories.add(category);
        }
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        final ImageView background;
        if(convertView==null){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_categories,null);
            background = (ImageView)v.findViewById(R.id.category_bg);
        } else {
            v = convertView;
            background = (ImageView)v.findViewById(R.id.category_bg);
        }
        Picasso.with(mContext)
                .load(categories.get(position).image)
                .into(background);
        ((TextView)v.findViewById(R.id.category_title)).setText(categories.get(position).title);
        return v;
    }
}
