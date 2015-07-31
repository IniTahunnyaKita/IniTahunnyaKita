package com.molaja.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.molaja.android.R;
import com.molaja.android.activities.CategoryActivity;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.model.DiscoverItem;
import com.molaja.android.model.HangoutPost;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Florian on 2/15/2015.
 */

public class DiscoverItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_CATEGORY = 1;
    public static final int VIEW_TYPE_IMAGE = 2;
    List<DiscoverItem> list;

    public DiscoverItemsAdapter(List<DiscoverItem> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.child_discover_items_category, parent, false));
            case VIEW_TYPE_IMAGE:
            return new ImageViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.child_discover_items, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof DiscoverItem.Category)
            return VIEW_TYPE_CATEGORY;

        //default
        return VIEW_TYPE_IMAGE;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ImageViewHolder) {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    HangoutPost hangoutPost = new HangoutPost();
                    hangoutPost.setTitle("DiscoverItem");
                    hangoutPost.setPrice(690000);
                    hangoutPost.setItemUrl(list.get(holder.getAdapterPosition()).image);
                    Intent intent = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
                    intent.putExtra("ITEM_INFO", gson.toJson(hangoutPost));

                    if (holder.itemView.getContext() instanceof Activity) {
                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(),
                                        v,   // The view which starts the transition
                                        holder.itemView.getContext().getString(R.string.item_detail_transition)    // The transitionName of the view we’re transitioning to
                                );
                        ActivityCompat.startActivity((Activity) holder.itemView.getContext(), intent, options.toBundle());
                    } else {
                        holder.itemView.getContext().startActivity(intent);
                    }
                }
            });

            Picasso.with(holder.itemView.getContext())
                    .load(list.get(position).image)
                    .fit().centerCrop()
                    .into(viewHolder.image);
        } else if (holder instanceof CategoryViewHolder) {
            final CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
            final DiscoverItem.Category category = (DiscoverItem.Category) list.get(position);

            viewHolder.categoryTitle.setText(category.category_title);

            Picasso.with(holder.itemView.getContext())
                    .load(category.category_bg)
                    .fit().centerCrop()
                    .into(viewHolder.categoryBg);
            viewHolder.categoryBg.setColorFilter(Color.parseColor("#88000000"));

            if(category.category_title.equals("Fashion")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_fashion)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Beauty")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_beauty)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Games & Toys")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_toys)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Bags")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_bags)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Kids")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_kids)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Home")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_home)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Gadgets")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_gadgets)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Electronics")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_electronics)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Sports")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_sports)
                        .fit()
                        .into(viewHolder.categoryIcon);
            } else if(category.category_title.equals("Books")){
                Picasso.with(holder.itemView.getContext())
                        .load(R.drawable.icon_books)
                        .fit()
                        .into(viewHolder.categoryIcon);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String transitionName = viewHolder.itemView.getContext().getString(R.string.category_icon_transition);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        viewHolder.categoryIcon.setTransitionName(transitionName);

                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)v.getContext(),
                                    viewHolder.categoryIcon,
                                    transitionName
                            );

                    Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                    intent.putExtra(CategoryActivity.CATEGORY_TITLE_EXTRA, category.category_title);
                    intent.putExtra(CategoryActivity.CATEGORY_ICON_EXTRA, category.category_icon);
                    intent.putExtra(CategoryActivity.CATEGORY_BG_EXTRA, category.category_bg);

                    ActivityCompat.startActivity((Activity)v.getContext(), intent,
                            options.toBundle());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    
    
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        ImageView categoryIcon;
        ImageView categoryBg;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            //bind views
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
            categoryBg = (ImageView) itemView.findViewById(R.id.category_bg);
        }
    }
}
