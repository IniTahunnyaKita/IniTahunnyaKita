package com.molaja.android.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.model.User;
import com.molaja.android.util.BackendHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by florian hidayat on 8/5/15.
 */
public class RequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_DEFAULT = 1;
    public static final int ITEM_TYPE_ACCEPTED = 2;
    public static final int ITEM_TYPE_REJECTED = 3;
    public static final int TIMEOUT = 2000;

    Context context;
    View emptyView;
    List<Request> requestList;

    int layoutHeight = 0;

    public RequestsAdapter(Context context, View emptyView, List<Request> requestList) {
        this.context = context;
        this.emptyView = emptyView;
        this.requestList = requestList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_DEFAULT:
                return new RequestViewHolder(LayoutInflater.from(context).
                        inflate(R.layout.child_notification_request, parent, false));
            case ITEM_TYPE_ACCEPTED:
                return new ResultViewHolder(LayoutInflater.from(context).
                        inflate(R.layout.child_notification_request_accepted, parent, false));
            case ITEM_TYPE_REJECTED:
                return new ResultViewHolder(LayoutInflater.from(context).
                        inflate(R.layout.child_notification_request_rejected, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (requestList.get(position).STATUS == Request.ACCEPTED)
            return ITEM_TYPE_ACCEPTED;
        if (requestList.get(position).STATUS == Request.REJECTED)
            return ITEM_TYPE_REJECTED;
        return ITEM_TYPE_DEFAULT;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RequestViewHolder) {
            if (layoutHeight == 0) {
                holder.itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                layoutHeight = holder.itemView.getMeasuredHeight();
            }

            final RequestViewHolder viewHolder = (RequestViewHolder) holder;

            Picasso.with(context)
                    .load(requestList.get(position).image)
                    .fit()
                    .into(viewHolder.profilePicture);

            viewHolder.name.setText(requestList.get(position).name);
            viewHolder.username.setText(requestList.get(position).username);

            viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    acceptRequest(requestList.get(pos), pos);
                }
            });
            viewHolder.rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    rejectRequest(requestList.get(pos), pos);
                }
            });
        }

        if (holder instanceof ResultViewHolder) {
            holder.itemView.getLayoutParams().height = layoutHeight;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos = holder.getAdapterPosition();
                    requestList.remove(pos);
                    notifyItemRemoved(pos);
                }
            }, TIMEOUT);
        }
    }

    private void acceptRequest(Request request, int position) {
        BackendHelper.acceptRequest(context, request.id);
        requestList.remove(position);
        notifyItemRemoved(position);

        request.STATUS = Request.ACCEPTED;
        requestList.add(position, request);
        notifyItemInserted(position);
    }

    private void rejectRequest(Request request, int position) {
        requestList.remove(position);
        notifyItemRemoved(position);

        request.STATUS = Request.REJECTED;
        requestList.add(position, request);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        int count = requestList.size();

        int visibility = count==0? View.VISIBLE : View.GONE;
        emptyView.setVisibility(visibility);

        return count;
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView name, username;
        Button acceptButton, rejectButton;

        public RequestViewHolder(View itemView) {
            super(itemView);
            profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
            name = (TextView) itemView.findViewById(R.id.fullname);
            username = (TextView) itemView.findViewById(R.id.username);
            acceptButton = (Button) itemView.findViewById(R.id.accept_btn);
            rejectButton = (Button) itemView.findViewById(R.id.reject_btn);
        }
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View itemView) {
            super(itemView);
        }

    }

    public static class Request extends User {
        public static final int ACCEPTED = 1;
        public static final int REJECTED = -1;
        public int STATUS;
    }

}
