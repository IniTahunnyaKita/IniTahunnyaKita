package com.molaja.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.google.gson.Gson;
import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.fragment.HangoutFragment;
import com.molaja.android.model.HangoutPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HangoutAdapter extends RecyclerView.Adapter<HangoutAdapter.PostViewHolder> {
	public final static int TYPE_PROFILE = 0;
	public final static int TYPE_POST = 1;
	public final static int TYPE_DISCUSS = 2;
	private ArrayList<HangoutPost> group;
    private static Context mContext;
    private static Resources resources;
    private GestureDetector gestureDetector;
    private int doubleTappedPos;
    private View doubleTappedView;
    private String transitionTag;
    private HangoutFragment hangoutFragment;

	public HangoutAdapter(Context context, ArrayList<HangoutPost> list, HangoutFragment hangoutFragment) {
        this.group = list;
        this.mContext = context;
        this.hangoutFragment = hangoutFragment;
        this.resources = mContext.getResources();
        gestureDetector = new GestureDetector(mContext, new DoubleTapListener());
        transitionTag = resources.getString(R.string.item_detail_transition);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_post, null);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder postHolder, final int position) {
        postHolder.overview.setText(group.get(position).getOverview());
        postHolder.thumbsUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalThumbsUp = group.get(position).getThumbsUp();
                if (!postHolder.thumbsUpState) {
                    totalThumbsUp++;
                    group.get(position).setThumbsUp(totalThumbsUp);
                    postHolder.thumbsUpState = true;
                    postHolder.thumbsUpIv.setImageResource(R.drawable.ic_thumbs_up_enabled);
                    postHolder.thumbsUp.setText(Integer.toString(totalThumbsUp));
                } else {
                    totalThumbsUp--;
                    group.get(position).setThumbsUp(totalThumbsUp);
                    postHolder.thumbsUpState = false;
                    postHolder.thumbsUpIv.setImageResource(R.drawable.ic_thumbs_up_default);
                    postHolder.thumbsUp.setText(Integer.toString(totalThumbsUp));
                }
            }
        });
        postHolder.thumbsUp.setText(Integer.toString(group.get(position).getThumbsUp()));
        postHolder.price.setText(group.get(position).getPrice());
        Picasso.with(mContext)
                .load(group.get(position).getItemUrl())
                .error(R.drawable.ensa_shop)
                .fit().centerCrop()
                .into(postHolder.postImg);
        postHolder.postImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                doubleTappedPos = position;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    doubleTappedView = v;
                }
                return gestureDetector.onTouchEvent(event);
            }
        });
        postHolder.pollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).addPollItem(group.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return group.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView overview;
        ImageView postImg;
        RelativeLayout thumbsUpBtn;
        ImageView thumbsUpIv;
        boolean thumbsUpState;
        TextView thumbsUp;
        LinearLayout pollBtn;
        TextView price;

        public PostViewHolder(View v) {
            super(v);
            overview = (TextView) v.findViewById(R.id.post_overview);
            postImg = (ImageView) v.findViewById(R.id.post_image);
            thumbsUpBtn = (RelativeLayout) v.findViewById(R.id.thumbs_up_btn);
            thumbsUpIv = (ImageView) v.findViewById(R.id.thumbs_up_img);
            thumbsUpState = false;
            thumbsUp = (TextView) v.findViewById(R.id.post_thumbs_up);
            pollBtn = (LinearLayout) v.findViewById(R.id.post_poll_btn);
            price = (TextView) v.findViewById(R.id.post_price);
        }
    }

    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("kodok","doubletap");
            Gson gson = new Gson();
            Intent intent = new Intent(mContext, ItemDetailActivity.class);
            intent.putExtra("ITEM_INFO", gson.toJson(group.get(doubleTappedPos)));

            if (mContext instanceof Activity) {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                                doubleTappedView,   // The view which starts the transition
                                mContext.getString(R.string.item_detail_transition)    // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
            } else {
                mContext.startActivity(intent);
            }
            return true;
        }
    }

    public static class HeaderAdapter implements StickyHeadersAdapter<HeaderAdapter.HeaderViewHolder> {
        private ArrayList<HangoutPost> group;

        public HeaderAdapter( ArrayList<HangoutPost> group){
            this.group = group;
        }
        @Override
        public HeaderViewHolder onCreateViewHolder(ViewGroup viewGroup) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.list_post_header, viewGroup, false);
            return new HeaderViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final HeaderViewHolder viewHolder,final int position) {
            Picasso.with(mContext)
                    .load(group.get(position).getProfileUrl())
                    .resize(100,100)
                    .into(viewHolder.profilePic);
            viewHolder.fullName.setText(group.get(position).getFullname());
            viewHolder.title.setText(group.get(position).getTitle());
            viewHolder.profilePic.getRootView().setTag(viewHolder.profilePic);
        }

        @Override
        public long getHeaderId(int i) {
            return i;
        }

        static class HeaderViewHolder extends RecyclerView.ViewHolder {
            CircleImageView profilePic;
            TextView fullName;
            TextView title;

            public HeaderViewHolder(View v) {
                super(v);
                profilePic = (CircleImageView) v.findViewById(R.id.profile_picture);
                fullName = (TextView) v.findViewById(R.id.full_name);
                title = (TextView) v.findViewById(R.id.post_title);
            }
        }
    }

}
