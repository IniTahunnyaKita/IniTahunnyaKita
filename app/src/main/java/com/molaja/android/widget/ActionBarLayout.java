package com.molaja.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.molaja.android.R;
import com.molaja.android.activities.MainActivity;

/**
 * Created by florian on 31/1/15.
 */
public class ActionBarLayout extends RelativeLayout{
    private static boolean hasRevealLayout = false;
    private RevealLayout mRevealLayout;
    private NotificationLayout mNotificationLayout;
    GestureDetector gestureDetector;

    public ActionBarLayout(Context context) {
        super(context);
        //initLayout(context);
    }

    public ActionBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public ActionBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    public void setHasRevealLayout(boolean b){
        hasRevealLayout = b;
    }

    public void initLayout(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //getRootView().findViewById(R.id.notification_layout).dispatchTouchEvent(event);
        if(hasRevealLayout && mRevealLayout==null){
            mRevealLayout = (RevealLayout) getRootView().findViewById(R.id.reveal_layout);
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        findViewById(R.id.notif_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNotificationLayout == null)
                    mNotificationLayout = (NotificationLayout) getRootView().findViewById(R.id.notification_layout);

                if(mNotificationLayout.isLayoutExpanded()) {
                    mNotificationLayout.collapseLayout();

                    if (getContext() instanceof MainActivity)
                        ((MainActivity) getContext()).switchToProfileActionBar(false);
                } else {
                    mNotificationLayout.expandLayout();

                    if (getContext() instanceof MainActivity)
                        ((MainActivity) getContext()).switchToProfileActionBar(true);
                }
            }
        });
        super.onFinishInflate();
    }

    public void switchToShopMode(){
        if(hasRevealLayout){
            mRevealLayout.next();
        }
        MainActivity.mode = MainActivity.SHOP_MODE;

        if (getContext() instanceof MainActivity)
            ((MainActivity) getContext()).setToolbarColor(getResources().getColor(R.color.Aquamarine), true);
        /*YoYo.with(Techniques.FadeOut)
                .duration(500)
                .playOn(findViewById(R.id.usermode_action_bar_bg));
        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .playOn(findViewById(R.id.shopmode_action_bar_bg));*/
    }

    public void switchToUserMode(){
        if(hasRevealLayout){
            mRevealLayout.next();
        }
        MainActivity.mode = MainActivity.USER_MODE;

        if (getContext() instanceof MainActivity)
            ((MainActivity) getContext()).setToolbarColor(getResources().getColor(R.color.Teal), true);

        /*YoYo.with(Techniques.FadeIn)
                .duration(500)
                .playOn(findViewById(R.id.usermode_action_bar_bg));
        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .playOn(findViewById(R.id.shopmode_action_bar_bg));*/
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("kodok","doubletap");

            if(MainActivity.mode==MainActivity.USER_MODE) {
                switchToShopMode();
            }else {
                switchToUserMode();
            }
            return true;
        }
    }
}
