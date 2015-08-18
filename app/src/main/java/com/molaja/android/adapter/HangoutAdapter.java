package com.molaja.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.activities.ShareActivity;
import com.molaja.android.activities.ShopActivity;
import com.molaja.android.fragment.ShopProfileFragment;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.util.FragmentUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HangoutAdapter extends RecyclerView.Adapter<HangoutAdapter.PostViewHolder> implements View.OnClickListener{
	public final static int TYPE_PROFILE = 0;
	public final static int TYPE_POST = 1;
	public final static int TYPE_DISCUSS = 2;
	private ArrayList<HangoutPost> group;
    private CustomGestureDetector gestureDetector;

	public HangoutAdapter(ArrayList<HangoutPost> list) {
        this.group = list;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, null);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder postHolder, int position) {
        final Context context = postHolder.itemView.getContext();
        
        if (gestureDetector == null)
            gestureDetector = new CustomGestureDetector(context, new DoubleTapListener());
        
        
        Picasso.with(context)
                .load(group.get(position).getProfileUrl())
                .fit().centerCrop()
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Palette.from(source).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                postHolder.title.setTextColor(palette.getDarkVibrantColor(Color.BLACK));
                            }
                        });
                        return source;
                    }

                    @Override
                    public String key() {
                        return "palette";
                    }
                })
                .into(postHolder.profilePic);

        postHolder.fullName.setText(group.get(position).getFullname());
        postHolder.title.setText(group.get(position).getTitle());
        postHolder.profilePic.getRootView().setTag(postHolder.profilePic);
        postHolder.overview.setText(group.get(position).getOverview());

        postHolder.thumbsUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalThumbsUp = group.get(postHolder.getAdapterPosition()).getThumbsUp();

                if (!postHolder.thumbsUpState) {
                    totalThumbsUp++;
                    group.get(postHolder.getAdapterPosition()).setThumbsUp(totalThumbsUp);
                    postHolder.thumbsUpState = true;
                    postHolder.thumbsUpIv.setImageResource(R.mipmap.ic_thumbs_up_enabled);
                    postHolder.thumbsUp.setText(Integer.toString(totalThumbsUp));
                } else {
                    totalThumbsUp--;
                    group.get(postHolder.getAdapterPosition()).setThumbsUp(totalThumbsUp);
                    postHolder.thumbsUpState = false;
                    postHolder.thumbsUpIv.setImageResource(R.mipmap.ic_thumbs_up_default);
                    postHolder.thumbsUp.setText(Integer.toString(totalThumbsUp));
                }
            }
        });

        postHolder.thumbsUp.setText(Integer.toString(group.get(position).getThumbsUp()));
        postHolder.price.setText(group.get(position).getPrice());

        Picasso.with(context)
                .load(group.get(position).getItemUrl())
                .fit().centerCrop()
                .into(postHolder.postImg);
        postHolder.postImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.setTargetView(v, postHolder.getAdapterPosition());
                return gestureDetector.onTouchEvent(event);
            }
        });
        postHolder.pollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity)
                    ((MainActivity) context).addPollItem(group.get(postHolder.getAdapterPosition()));
            }
        });

        postHolder.fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = postHolder.getAdapterPosition();

                if (position % 2 == 1) {
                    MainActivity mainActivity = (MainActivity) v.getContext();

                    Bundle bundle = new Bundle();
                    bundle.putString(ShopActivity.SHOP_NAME, group.get(position).getFullname());
                    bundle.putString(ShopActivity.SHOP_PICTURE,
                            group.get(position).getProfileUrl());

                    FragmentManager fm = mainActivity.getCurrentFragment().getChildFragmentManager();
                    FragmentUtil.switchFragment(fm, R.id.fragment_main_tab_container, new ShopProfileFragment(), bundle);
                } else {
                    Intent intent = new Intent(v.getContext(), ShopActivity.class);
                    intent.putExtra(ShopActivity.SHOP_NAME, group.get(position).getFullname());
                    intent.putExtra(ShopActivity.SHOP_PICTURE,
                            group.get(position).getProfileUrl());
                    v.getContext().startActivity(intent);
                }

            }
        });
        postHolder.shareBtn.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return group.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn:
                Context context = v.getContext();
                Intent intent = new Intent(context, ShareActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView fullName, title, overview, thumbsUp, price;
        ImageView postImg, thumbsUpIv;
        View thumbsUpBtn, pollBtn, shareBtn;

        boolean thumbsUpState;

        public PostViewHolder(View v) {
            super(v);
            profilePic = (CircleImageView) v.findViewById(R.id.profile_picture);
            fullName = (TextView) v.findViewById(R.id.full_name);
            title = (TextView) v.findViewById(R.id.post_title);
            overview = (TextView) v.findViewById(R.id.post_overview);
            postImg = (ImageView) v.findViewById(R.id.post_image);
            shareBtn = v.findViewById(R.id.share_btn);
            thumbsUpBtn = v.findViewById(R.id.thumbs_up_btn);
            thumbsUpIv = (ImageView) v.findViewById(R.id.thumbs_up_img);
            thumbsUpState = false;
            thumbsUp = (TextView) v.findViewById(R.id.post_thumbs_up);
            pollBtn = v.findViewById(R.id.post_poll_btn);
            price = (TextView) v.findViewById(R.id.post_price);
        }
    }

    private class CustomGestureDetector extends GestureDetector {

        private OnGestureListener listener;

        public CustomGestureDetector(Context context, OnGestureListener listener) {
            super(context, listener);

            this.listener = listener;
        }

        /**
         * sets target view for custom double-tap handling.
         * @param v the target view itself.
         * @param pos the target view position in the list.
         */
        protected void setTargetView(View v, int pos) {
            if (listener instanceof DoubleTapListener)
                ((DoubleTapListener) listener).setTargetView(v, pos);
        }
    }

    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        
        private View mTargetView;
        private int mTargetViewPos;

        protected void setTargetView(View v, int pos) {
            mTargetView = v;
            mTargetViewPos = pos;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("kodok","doubletap");
            if (mTargetView == null)
                return true;
            
            Context context = mTargetView.getContext();

            Gson gson = new Gson();
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("ITEM_INFO", gson.toJson(group.get(mTargetViewPos)));

            if (context instanceof Activity) {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                mTargetView,   // The view which starts the transition
                                context.getString(R.string.item_detail_transition)    // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }

            mTargetView = null;

            return true;
        }
    }

}
