package com.molaja.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.model.ChatData;
import com.molaja.android.model.User;
import com.molaja.android.widget.ReversableLinearLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by florianhidayat on 1/7/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatData> chatItems;
    private User currentUser;

    public ChatAdapter(List<ChatData> chatItems) {
        this.chatItems = chatItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatBoxViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_chat_box, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (currentUser == null)
            currentUser = User.getCurrentUser(holder.itemView.getContext());

        if (holder instanceof ChatBoxViewHolder) {
            ChatBoxViewHolder chatBoxViewHolder = (ChatBoxViewHolder) holder;
            if (chatItems.get(position).id == currentUser.id &&
                    !chatBoxViewHolder.reversableLayout.isReversed()) {
                chatBoxViewHolder.reversableLayout.reverse();
                ((LinearLayout)chatBoxViewHolder.itemView).setGravity(Gravity.RIGHT);
            }

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            chatBoxViewHolder.chatText.setText(chatItems.get(position).message);
            chatBoxViewHolder.timeText.setText(sdf.format(cal.getTime()));

            Picasso.with(holder.itemView.getContext())
                    .load(currentUser.image)
                    .fit().centerCrop()
                    .into(chatBoxViewHolder.chatPicture);
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void addMessage(String message) {
        ChatData data = new ChatData();
        data.id = currentUser.id;
        data.message = message;
        chatItems.add(0, data);
        notifyItemInserted(0);
    }

    public static class ChatBoxViewHolder extends RecyclerView.ViewHolder {
        ReversableLinearLayout reversableLayout;
        ImageView chatPicture;
        TextView chatText, timeText;

        public ChatBoxViewHolder(View itemView) {
            super(itemView);
            reversableLayout = (ReversableLinearLayout) itemView.findViewById(R.id.reversable_layout);
            chatPicture = (ImageView) itemView.findViewById(R.id.chat_picture);
            chatText = (TextView) itemView.findViewById(R.id.chat_text);
            timeText = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
