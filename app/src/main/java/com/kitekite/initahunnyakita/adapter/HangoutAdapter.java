package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitekite.initahunnyakita.MainActivity;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.fragment.ProfileFragment;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.widget.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.graphics.Bitmap.Config;

public class HangoutAdapter extends ArrayAdapter<HangoutPost>{
	public final static int TYPE_PROFILE = 0;
	public final static int TYPE_POST = 1;
	public final static int TYPE_DISCUSS = 2;
	private ArrayList<HangoutPost> group;
    private static Context mContext;
    private static Resources resources;

	public HangoutAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}
	public HangoutAdapter(Context context, int resource, ArrayList<HangoutPost> list) {
	    super(context, resource, list);
	    this.group = list;
        this.mContext = context;
        this.resources = mContext.getResources();
	}
	
	public Object getChild(int childPosition) {
		// TODO Auto-generated method stub
		return group.get(childPosition);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
        final PostViewHolder postHolder;
        View v = convertView;
        if(v==null){
            Log.d("getview","v is null. pos:"+position);
            postHolder = new PostViewHolder();
            LayoutInflater vi= LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_post, null);postHolder.profilePic = (RoundedImageView) v.findViewById(R.id.profile_picture);
            postHolder.fullName = (TextView) v.findViewById(R.id.full_name);
            postHolder.title = (TextView) v.findViewById(R.id.post_title);
            postHolder.overview = (TextView) v.findViewById(R.id.post_overview);
            postHolder.postImg = (ImageView) v.findViewById(R.id.post_image);
            postHolder.thumbsUpBtn = (RelativeLayout) v.findViewById(R.id.thumbs_up_btn);
            postHolder.thumbsUpIv = (ImageView) v.findViewById(R.id.thumbs_up_img);
            postHolder.thumbsUpState = false;
            postHolder.thumbsUp = (TextView) v.findViewById(R.id.post_thumbs_up);
            postHolder.pollBtn = (LinearLayout) v.findViewById(R.id.post_poll_btn);
            postHolder.price = (TextView) v.findViewById(R.id.post_price);
            v.setTag(postHolder);
        }
        else
            postHolder = (PostViewHolder) v.getTag();

        Log.d("getview","set values.pos:"+position);
        Picasso.with(mContext)
                .load(group.get(position).getProfileUrl())
                .error(R.drawable.ensa_shop)
                .resize(50,50)
                .tag(mContext)
                .into(postHolder.profilePic);
        postHolder.fullName.setText(group.get(position).getFullname());
        postHolder.title.setText(group.get(position).getTitle());
        postHolder.overview.setText(group.get(position).getOverview());
        postHolder.thumbsUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalThumbsUp = group.get(position).getThumbsUp();
                if(!postHolder.thumbsUpState){
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
                .tag(mContext)
                .into(postHolder.postImg);
        View.OnClickListener profileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTranslateAnimation(postHolder.profilePic,group.get(position).getProfileUrl().toString());
                ((ActionBarActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container,new ProfileFragment(),"PROFILE")
                        .addToBackStack(null)
                        .commit();
            }
        };
        postHolder.fullName.setOnClickListener(profileClickListener);
        postHolder.profilePic.setOnClickListener(profileClickListener);
        postHolder.pollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().addPollItem(group.get(position));
            }
        });

		return v;
        //case TYPE_DISCUSS:

		//}
		//return convertView;
		
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
	
	static class PostViewHolder{
		RoundedImageView profilePic;
        TextView fullName;
        TextView title;
        TextView overview;
        ImageView postImg;
        RelativeLayout thumbsUpBtn;
        ImageView thumbsUpIv;
        boolean thumbsUpState;
        TextView thumbsUp;
        LinearLayout pollBtn;
        TextView price;
	}

}
