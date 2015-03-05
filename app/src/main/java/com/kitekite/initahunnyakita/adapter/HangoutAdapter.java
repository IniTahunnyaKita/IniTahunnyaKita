package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kitekite.initahunnyakita.MainActivity;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.fragment.itemdetail.ItemDetailFragment;
import com.kitekite.initahunnyakita.fragment.ProfileFragment;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;

public class HangoutAdapter extends RecyclerView.Adapter<HangoutAdapter.PostViewHolder> {
	public final static int TYPE_PROFILE = 0;
	public final static int TYPE_POST = 1;
	public final static int TYPE_DISCUSS = 2;
	private ArrayList<HangoutPost> group;
    private static Context mContext;
    private static Resources resources;
    private GestureDetector gestureDetector;
    private int doubleTappedPos;

	public HangoutAdapter(Context context, ArrayList<HangoutPost> list) {
        this.group = list;
        this.mContext = context;
        this.resources = mContext.getResources();
        gestureDetector = new GestureDetector(mContext, new DoubleTapListener());
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
                return gestureDetector.onTouchEvent(event);
            }
        });
        postHolder.pollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().addPollItem(group.get(position));
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
            ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putString("ITEM_INFO",gson.toJson(group.get(doubleTappedPos)));
            itemDetailFragment.setArguments(bundle);
            ((ActionBarActivity) mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container,itemDetailFragment,"ITEM_DETAIL")
                    .addToBackStack("ITEM_DETAIL")
                    .commit();
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
            View.OnClickListener profileClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doTranslateAnimation(viewHolder.profilePic,group.get(position).getProfileUrl().toString());
                    ((ActionBarActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container,new ProfileFragment(),"PROFILE")
                            .addToBackStack("PROFILE")
                            .commit();
                }
            };
            viewHolder.fullName.setOnClickListener(profileClickListener);
            viewHolder.profilePic.setOnClickListener(profileClickListener);
        }

        @Override
        public long getHeaderId(int i) {
            return i;
        }

        private void doTranslateAnimation(View v, String imgUrl) {
            View root = v.getRootView();
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();
            int originalPos[] = new int[2];

            v.getLocationOnScreen(originalPos);
            originalPos[1] += statusBarOffset/2;

            int dest[] = new int[2];
            dest[0] = dm.widthPixels / 2;
            dest[0] -= (v.getMeasuredWidth() / 2);
            //dest[1] = dm.heightPixels / 2 - (v.getMeasuredHeight() / 2) - statusBarOffset;
            int mHeaderHeight = mContext.getResources().getDimensionPixelSize(R.dimen.profile_header_height);
            int headerLogoSize = mContext.getResources().getDimensionPixelSize(R.dimen.profile_header_logo_size);
            dest[1] = -headerLogoSize / 2 + mHeaderHeight/2+168;
            dest[1] = 240;
            Log.d("kodok","status bar offset:"+headerLogoSize+"y pos:"+mHeaderHeight);

            MainActivity mainActivity = MainActivity.getMainActivity();
            mainActivity.iconPos = originalPos;
            mainActivity.iconDest = dest;
            mainActivity.doTranslateAnimation(imgUrl);
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
