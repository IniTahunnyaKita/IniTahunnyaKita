package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.model.HangoutPost;
import com.kitekite.initahunnyakita.widget.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimelineAdapter extends ArrayAdapter<HangoutPost>{
	public final static int TYPE_PROFILE = 0;
	public final static int TYPE_POST = 1;
	public final static int TYPE_DISCUSS = 2;
	private ArrayList<HangoutPost> group;
    private static Context mContext;
    private static Resources resources;

	public TimelineAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}
	public TimelineAdapter(Context context, int resource, ArrayList<HangoutPost> list) {
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
        PostViewHolder postHolder;
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
            postHolder.thumbsUp = (TextView) v.findViewById(R.id.post_thumbs_up);
            postHolder.price = (TextView) v.findViewById(R.id.post_price);
            v.setTag(postHolder);
        }
        else
            postHolder = (PostViewHolder) v.getTag();

        Log.d("getview","set values.pos:"+position);
        Picasso.with(mContext)
                .load(group.get(position).getProfileUrl())
                .error(R.drawable.ensa_shop)
                .tag(mContext)
                .into(postHolder.profilePic);
        postHolder.fullName.setText(group.get(position).getFullname());
        postHolder.title.setText(group.get(position).getTitle());
        postHolder.overview.setText(group.get(position).getOverview());
        //postHolder.postImg.setImageResource(R.drawable.jerseynesia_item1);
        postHolder.thumbsUp.setText(group.get(position).getThumbsUp());
        postHolder.price.setText(group.get(position).getPrice());
        Picasso.with(mContext)
                .load(group.get(position).getItemUrl())
                .error(R.drawable.ensa_shop)
                .tag(mContext)
                .into(postHolder.postImg);
		//(position){
		//case TYPE_POST:


		return v;
        //case TYPE_DISCUSS:

		//}
		//return convertView;
		
	}
	
	static class PostViewHolder{
		RoundedImageView profilePic;
        TextView fullName;
        TextView title;
        TextView overview;
        ImageView postImg;
        TextView thumbsUp;
        TextView price;
	}

}
