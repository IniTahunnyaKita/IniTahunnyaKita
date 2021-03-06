package com.molaja.android.adapter;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.activities.ChatActivity;
import com.molaja.android.model.Discussion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Florian on 2/26/2015.
 */
public class DiscussionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Discussion> list;
    List<Discussion> threads;

    View elevatedView;

    Discussion selectedDiscussion;
    boolean mIsAnItemSelected = false;
    boolean mIsAnimating = false;

    public DiscussionAdapter(List<Discussion> list){
        this.list = list;
        this.threads = new ArrayList<>(list);
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
            View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_discussion_expanded, parent, false);
            return new ChildViewHolder(childView);
        } else {
            View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_discussion, parent, false);
            return new DiscussionViewHolder(childView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Discussion discussion = list.get(position);
        if(holder instanceof DiscussionViewHolder){
            final DiscussionViewHolder discussionHolder = (DiscussionViewHolder) holder;
            discussionHolder.name.setText(discussion.name);

            Picasso.with(holder.itemView.getContext())
                    .load(discussion.profile_url)
                    .fit().centerCrop()
                    .into(discussionHolder.profilePicture);

            discussionHolder.noOfDiscussions.setText(discussion.getDiscussionsText());

            Picasso.with(holder.itemView.getContext())
                    .load(discussion.discussions[0].image_url)
                    .fit().centerCrop()
                    .into(discussionHolder.mostRecent);

            discussionHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsAnItemSelected) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            elevatedView = v;
                            v.animate().translationZ(20f).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    mIsAnimating = true;
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    selectItem(discussionHolder.getAdapterPosition());
                                    mIsAnimating = false;
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    mIsAnimating = false;
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            }).start();
                        } else {
                            selectItem(discussionHolder.getAdapterPosition());
                        }
                    } else {
                        restoreItemsCompat();
                    }
                }
            });
        } else if(holder instanceof ChildViewHolder && discussion instanceof Discussion.Conversation){
            final ChildViewHolder childHolder = (ChildViewHolder) holder;
            final Discussion.Conversation conversation = (Discussion.Conversation) list.get(position);
            childHolder.title.setText(conversation.title);

            Picasso.with(holder.itemView.getContext())
                    .load(conversation.image_url)
                    .into(childHolder.image);

            childHolder.lastMessage.setText(Html.fromHtml(conversation.last_message));

            childHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("NAME",selectedDiscussion.name);
                    intent.putExtra("PROFILE_PICTURE", selectedDiscussion.profile_url);
                    intent.putExtra("CONVERSATION", conversation);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public boolean isAnItemSelected(){
        return mIsAnItemSelected;
    }

    public boolean isAnimating() {
        return mIsAnimating;
    }

    public void selectItem(final int position){
        new Handler().post(new Runnable() {
            @Override
            public void run() {

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
        });
    }

    public void restoreItemsCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && elevatedView != null)
            elevatedView.animate().translationZ(0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    restoreItems();
                    mIsAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsAnimating = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        else
            restoreItems();
    }

    private void restoreItems() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mIsAnItemSelected = false;

                //remove DiscussionChildren
                for (Iterator<Discussion> iter = list.listIterator(); iter.hasNext();) {
                    Discussion discussion = iter.next();
                    if (discussion instanceof Discussion.Conversation) {
                        iter.remove();
                        notifyItemRemoved(1);
                    }
                }

                String identifier = list.get(0).name;
                boolean identifierFound = false;
                for (int i = 0; i < threads.size(); i++) {
                    if (threads.get(i).name.equals(identifier)) {
                        identifierFound = true;
                    } else {
                        if (identifierFound) {
                            list.add(list.size(), threads.get(i));
                            notifyItemInserted(list.size());
                        } else {
                            list.add(i, threads.get(i));
                            notifyItemInserted(i);
                        }
                    }
                }
            }
        });
    }

    static class DiscussionViewHolder extends RecyclerView.ViewHolder{
        protected CircleImageView profilePicture;
        protected TextView name;
        protected TextView noOfDiscussions;
        protected ImageView mostRecent;

        public DiscussionViewHolder(View itemView) {
            super(itemView);
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
