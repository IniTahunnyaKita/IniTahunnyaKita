package com.kitekite.initahunnyakita.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.ChatActivity;
import com.kitekite.initahunnyakita.model.Discussion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Florian on 2/26/2015.
 */
public class DiscussionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    RecyclerView recyclerView;
    Context mContext;
    List<Discussion> list;
    List<Discussion> backupList;

    Discussion selectedDiscussion;
    boolean mIsAnItemSelected = false;

    public DiscussionAdapter(RecyclerView recyclerView, Context context, List<Discussion> list){
        this.recyclerView = recyclerView;
        mContext = context;
        this.list = list;
        this.backupList = new ArrayList<>(list);
    }

    @Override
    public int getItemViewType(int position) {
        if(mIsAnItemSelected && position!=0){
            return R.layout.child_discussion_expanded;
        } else {
            return R.layout.child_discussion;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == R.layout.child_discussion_expanded){
            View childView = LayoutInflater.from(mContext).inflate(R.layout.child_discussion_expanded, null);
            return new ChildViewHolder(childView);
        } else {
            View childView = LayoutInflater.from(mContext).inflate(R.layout.child_discussion, null);
            return new DiscussionViewHolder(childView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Discussion discussion = list.get(position);
        if(holder instanceof DiscussionViewHolder && discussion instanceof Discussion){
            DiscussionViewHolder discussionHolder = (DiscussionViewHolder) holder;
            discussionHolder.name.setText(discussion.name);

            Picasso.with(mContext)
                    .load(discussion.profile_url)
                    .fit().centerCrop()
                    .into(discussionHolder.profilePicture);

            discussionHolder.noOfDiscussions.setText(discussion.getDiscussionsText());

            Picasso.with(mContext)
                    .load(discussion.discussions[0].image_url)
                    .fit().centerCrop()
                    .into(discussionHolder.mostRecent);

            discussionHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsAnItemSelected)
                        selectItem(position);
                    else
                        restoreItems();
                }
            });
        } else if(holder instanceof ChildViewHolder && discussion instanceof Discussion.Conversation){
            ChildViewHolder childHolder = (ChildViewHolder) holder;
            final Discussion.Conversation conversation = (Discussion.Conversation) list.get(position);
            childHolder.title.setText(conversation.title);

            Picasso.with(mContext)
                    .load(conversation.image_url)
                    .into(childHolder.image);

            childHolder.lastMessage.setText(Html.fromHtml(conversation.last_message));

            childHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("NAME",selectedDiscussion.name);
                    intent.putExtra("PROFILE_PICTURE", selectedDiscussion.profile_url);
                    intent.putExtra("CONVERSATION", conversation);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public boolean isAnItemSelected(){
        return mIsAnItemSelected;
    }

    public void selectItem(int position){
        final Discussion discussion = selectedDiscussion = list.get(position);

        mIsAnItemSelected = true;
        String identifier = list.get(position).name;
        for(int i=list.size()-1; i>=0; i--){
            if(!list.get(i).name.equals(identifier)){
                list.remove(i);
                notifyItemRemoved(i);
            }
        }
        for(int i=0; i<discussion.discussions.length; i++){
            list.add(discussion.discussions[i]);
            notifyItemInserted(i+1);
        }
    }

    public void restoreItems(){
        mIsAnItemSelected = false;

        //remove DiscussionChildren
        //int counter = 0;
        for (Iterator<Discussion> iter = list.listIterator(); iter.hasNext();) {
            Discussion discussion = iter.next();
            if (discussion instanceof Discussion.Conversation) {
                iter.remove();
                notifyItemRemoved(1);
                //Log.d("hehe","count:"+counter);
            }
        }

        String identifier = list.get(0).name;
        boolean identifierFound = false;
        for (int i = 0; i < backupList.size(); i++) {
            if (backupList.get(i).name.equals(identifier)) {
                identifierFound = true;
            } else {
                if (identifierFound) {
                    list.add(list.size(), backupList.get(i));
                    notifyItemInserted(list.size());
                } else {
                    list.add(i, backupList.get(i));
                    notifyItemInserted(i);
                }
            }
        }
    }

    static class DiscussionViewHolder extends RecyclerView.ViewHolder{
        protected RelativeLayout parentLayout;
        protected CircleImageView profilePicture;
        protected TextView name;
        protected TextView noOfDiscussions;
        protected ImageView mostRecent;

        public DiscussionViewHolder(View itemView) {
            super(itemView);
            parentLayout = (RelativeLayout) itemView.findViewById(R.id.discussion_child_parent);
            profilePicture = (CircleImageView) itemView.findViewById(R.id.profile_picture);
            name = (TextView) itemView.findViewById(R.id.name);
            noOfDiscussions = (TextView) itemView.findViewById(R.id.no_of_discussions);
            mostRecent = (ImageView) itemView.findViewById(R.id.most_recent);
        }
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder{
        protected ImageView image;
        protected TextView title;
        protected TextView lastMessage;

        public ChildViewHolder(View itemView) {
            super(itemView);
            image  = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            lastMessage = (TextView) itemView.findViewById(R.id.last_message);
        }
    }
}
