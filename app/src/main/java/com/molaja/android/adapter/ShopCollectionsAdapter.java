package com.molaja.android.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.model.Shop;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Created by florianhidayat on 14/8/15.
 */
public class ShopCollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Shop shop;

    public ShopCollectionsAdapter(Shop shop) {
        this.shop = shop;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.header_shop_collections:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
            case R.layout.child_shop_collections_list:
                return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));

            default:
                return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListItemViewHolder) {
            ListItemViewHolder viewHolder = (ListItemViewHolder) holder;

            Picasso.with(viewHolder.image.getContext())
                    .load("http://static-origin.zalora.co.id/p/mango-9005-590809-1-1493124180.jpg")
                    .fit().centerCrop()
                    .into(viewHolder.image);

            ((ViewGroup) viewHolder.image.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    HangoutPost hangoutPost = new HangoutPost();
                    hangoutPost.setTitle("DiscoverShops");
                    hangoutPost.setPrice(Integer.parseInt((100+new Random().nextInt(399))+"000"));
                    hangoutPost.setItemUrl("http://static-origin.zalora.co.id/p/mango-9005-590809-1-1493124180.jpg");
                    hangoutPost.setFullname(shop.name);
                    hangoutPost.setProfileUrl(shop.image);
                    Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
                    intent.putExtra("ITEM_INFO", gson.toJson(hangoutPost));

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.header_shop_collections;
        }

        return R.layout.child_shop_collections_list;
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    protected static class HeaderViewHolder extends RecyclerView.ViewHolder {
        protected ImageView background;
        protected TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            background = (ImageView) itemView.findViewById(R.id.collections_header_bg);
            title = (TextView) itemView.findViewById(R.id.collections_header_title);

        }
    }

    protected static class ListItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView title;
        protected TextView price;
        protected Button shareBtn;
        protected Button addCartBtn;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.collection_image);
            title = (TextView) itemView.findViewById(R.id.collection_title);
            price = (TextView) itemView.findViewById(R.id.collection_price);
            shareBtn = (Button) itemView.findViewById(R.id.share_btn);
            addCartBtn = (Button) itemView.findViewById(R.id.add_cart_btn);
        }
    }

}
