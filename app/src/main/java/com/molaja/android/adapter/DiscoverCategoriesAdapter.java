package com.molaja.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Florian on 2/12/2015.
 */
public class DiscoverCategoriesAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<Category> categories = new ArrayList<>();
    String [] categoryNames = new String[]{"Fashion","Beauty","Games & Toys","Bags","Kids","Home","Gadgets","Electronics","Sports","Books"};
    String [] backgrounds = new String[]{"file:///android_asset/categories/fashion.jpg","file:///android_asset/categories/beauty.jpg","file:///android_asset/categories/toys.jpg",
            "file:///android_asset/categories/bags.jpg","file:///android_asset/categories/kids.jpg","file:///android_asset/categories/home.jpg","file:///android_asset/categories/gadgets.jpg",
            "file:///android_asset/categories/electronics.jpg","file:///android_asset/categories/sports.jpg","file:///android_asset/categories/books.jpg"};

    public DiscoverCategoriesAdapter(Context c){
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
        View v = convertView;
        ViewHolder viewHolder;
        if(v == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.child_discover_categories,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) v.findViewById(R.id.category_title);
            viewHolder.background = (ImageView)v.findViewById(R.id.category_bg);
            viewHolder.icon = (ImageView) v.findViewById(R.id.category_icon);
            viewHolder.parent = (RelativeLayout) v.findViewById(R.id.category_icon_parent);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)v.getTag();
        }
        Picasso.with(mContext)
                .load(categories.get(position).image)
                .fit().centerCrop()
                .into(viewHolder.background);

        String title = categories.get(position).title;
        viewHolder.title.setText(title);

        if(title.equals("Fashion")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_fashion)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Beauty")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_beauty)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Games & Toys")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_toys)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Bags")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_bags)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Kids")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_kids)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Home")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_home)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Gadgets")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_gadgets)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Electronics")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_electronics)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Sports")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_sports)
                    .fit()
                    .into(viewHolder.icon);
        } else if(title.equals("Books")){
            Picasso.with(mContext)
                    .load(R.drawable.icon_books)
                    .fit()
                    .into(viewHolder.icon);
        }

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO onclick
            }
        });


        return v;
    }

    static class ViewHolder {
        RelativeLayout parent;
        TextView title;
        ImageView background;
        ImageView icon;
    }
}
