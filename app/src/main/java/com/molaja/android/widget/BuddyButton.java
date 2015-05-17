package com.molaja.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.fragment.thebag.TheBagFragment;

/**
 * Created by florianhidayat on 3/5/15.
 */
public class BuddyButton extends LinearLayout{
    ImageButton buddyBtnBg;
    ImageView statusImage;
    TextView buddyBtnText;

    public BuddyButton(Context context) {
        super(context);
        init(context);
    }

    public BuddyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BuddyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.layout_buddies_btn, this);
        buddyBtnBg = (ImageButton) findViewById(R.id.buddy_btn_bg);
        buddyBtnText = (TextView) findViewById(R.id.buddy_btn_text);
        statusImage = (ImageView) findViewById(R.id.buddy_btn_status_image);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackground(Drawable background) {
        buddyBtnBg.setImageDrawable(background);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        buddyBtnBg.setOnClickListener(l);
    }

    public void setColor(int color) {
        buddyBtnText.setTextColor(color);
        statusImage.setColorFilter(color);
    }

    public void setStatus(int status) {
        switch (status) {
            case TheBagFragment.ARE_BUDDIES:
                statusImage.setVisibility(View.VISIBLE);
                statusImage.setImageResource(R.drawable.ic_tick);
                buddyBtnText.setText(getResources().getString(R.string.buddies));
                break;
            case TheBagFragment.PENDING_INVITE:
                statusImage.setVisibility(View.GONE);
                buddyBtnText.setText(getResources().getString(R.string.pending));
                break;
            case TheBagFragment.NOT_BUDDIES:
                statusImage.setVisibility(View.VISIBLE);
                statusImage.setImageResource(R.drawable.ic_action_new);
                buddyBtnText.setText(getResources().getString(R.string.add_buddy));
                break;
        }
    }
}
