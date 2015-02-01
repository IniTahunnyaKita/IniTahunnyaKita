package com.kitekite.initahunnyakita.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kitekite.initahunnyakita.MainActivity;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Florian on 1/26/2015.
 */
public class NotificationLayout extends RelativeLayout{

    private NotificationLayout layout;
    private MainActivity mainActivity;
    private ImageView blurredBg;
    private int initialY, maxHeight, mActionBarSize, topOffset;
    private float initialTouchY;
    private int triggerPixel = 250;
    private Timer t;
    private int timeCounter = 0;
    private static boolean isExpanded = false;
    private static boolean isBlurred = false;
    ArrayList<PropertyValuesHolder> props;
    ObjectAnimator anim;
    int contentWidth = getResources().getDisplayMetrics().widthPixels;
    Bitmap blurredBitmap;
    Drawable windowBackground;

    public NotificationLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public NotificationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public NotificationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);

    }

    public void initLayout(Context context){
        layout = (NotificationLayout) findViewById(R.id.notification_layout);
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        maxHeight = getResources().getDisplayMetrics().heightPixels - mActionBarSize;
        Log.d("kodok", "initlayout" + getResources().getDisplayMetrics().heightPixels + " " + mActionBarSize);
        anim = ObjectAnimator.ofFloat(layout,"y",0,-getResources().getDisplayMetrics().heightPixels+mActionBarSize);
        anim.setDuration(0);
        anim.start();

        contentWidth = getResources().getDisplayMetrics().widthPixels;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainActivity = MainActivity.getMainActivity();
                TypedValue outValue = new TypedValue();
                int[] attrs = { android.R.attr.windowBackground };
                mainActivity.getTheme().resolveAttribute(android.R.attr.windowBackground, outValue, true);
                TypedArray style = mainActivity.getTheme().obtainStyledAttributes(outValue.resourceId, attrs);
                windowBackground = style.getDrawable(0);
                style.recycle();
            }
        },500);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int [] location = new int[2];
        int goBack = -getResources().getDisplayMetrics().heightPixels+mActionBarSize;
        topOffset = getResources().getDisplayMetrics().heightPixels - layout.getMeasuredHeight();
        blurredBg = (ImageView) getRootView().findViewById(R.id.blur_image);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //ignore double tap
                if(event.getRawY()<mActionBarSize+topOffset)
                    return true;
                layout.setVisibility(VISIBLE);
                layout.getLocationInWindow(location);
                initialY = location[1];
                initialTouchY = event.getRawY();
                blurredBg.setVisibility(View.VISIBLE);
                drawBlurredBackground();
                cutBackground((float) getToggleYPosition() / maxHeight);
                isBlurred = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                //ignore double tap
                if(event.getRawY()<mActionBarSize)
                    return true;
                layout.setVisibility(VISIBLE);
                if(!isBlurred){
                    Log.d("hello","hello");
                    layout.getLocationInWindow(location);
                    initialY = location[1];
                    initialTouchY = event.getRawY();
                    blurredBg.setVisibility(View.VISIBLE);
                    drawBlurredBackground();
                    cutBackground((float) getToggleYPosition() / maxHeight);
                    isBlurred = true;
                }
                int newPos;
                if(isExpanded && (event.getRawY() - initialTouchY > 0))
                    newPos = initialY + (int) (event.getRawY() - initialTouchY)/3;
                else
                    newPos = initialY + (int) (event.getRawY() - initialTouchY);
                cutBackground((float)getToggleYPosition()/maxHeight);
                props = new ArrayList<PropertyValuesHolder>(1);
                props.add(PropertyValuesHolder.ofFloat("y", newPos));
                anim = ObjectAnimator.ofPropertyValuesHolder(layout,
                        props.toArray(new PropertyValuesHolder[props.size()]));
                anim.setDuration(0);
                anim.start();
                return true;
            case MotionEvent.ACTION_UP:
                //ignore double tap
                if(event.getRawY()<mActionBarSize)
                    return true;
                if(!isExpanded){
                    if(triggerPixel < event.getRawY() - initialTouchY)
                    {
                        int y = 0 ;
                        isExpanded = true;
                        props = new ArrayList<PropertyValuesHolder>(1);
                        props.add(PropertyValuesHolder.ofFloat("y", y));
                        anim = ObjectAnimator.ofPropertyValuesHolder(layout,
                                props.toArray(new PropertyValuesHolder[props.size()]));
                        anim.setDuration(300);
                        anim.addListener(animatorListener);
                        anim.start();
                    }else{
                        int y = goBack;
                        isExpanded = false;
                        props = new ArrayList<PropertyValuesHolder>(1);
                        props.add(PropertyValuesHolder.ofFloat("y", y));
                        anim = ObjectAnimator.ofPropertyValuesHolder(layout,
                                props.toArray(new PropertyValuesHolder[props.size()]));
                        anim.setDuration(300);
                        anim.addListener(animatorListener);
                        anim.start();
                    }
                } else {
                    if(triggerPixel < initialTouchY - event.getRawY())
                    {
                        int y = goBack;
                        isExpanded = false;
                        props = new ArrayList<PropertyValuesHolder>(1);
                        props.add(PropertyValuesHolder.ofFloat("y", y));
                        anim = ObjectAnimator.ofPropertyValuesHolder(layout,
                                props.toArray(new PropertyValuesHolder[props.size()]));
                        anim.setDuration(300);
                        anim.addListener(animatorListener);
                        anim.start();
                    }else{
                        int y = 0;
                        isExpanded = true;
                        props = new ArrayList<PropertyValuesHolder>(1);
                        props.add(PropertyValuesHolder.ofFloat("y", y));
                        ObjectAnimator anim = null;
                        anim = ObjectAnimator.ofPropertyValuesHolder(layout,
                                props.toArray(new PropertyValuesHolder[props.size()]));
                        anim.setDuration(300);
                        anim.addListener(animatorListener);
                        anim.start();
                    }
                }

                return true;
        }
        return super.dispatchTouchEvent(event);
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener(){
        @Override
        public void onAnimationStart(Animator animation) {
            animateBlurredBg(300);
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            if(!isExpanded) {
                if(blurredBitmap!=null)
                    blurredBitmap.recycle();
                blurredBitmap = null;
                blurredBg.setVisibility(GONE);
                layout.setVisibility(GONE);
                isBlurred = false;
            }
            else
                cutBackground(100);
        }
        @Override
        public void onAnimationCancel(Animator animation) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onAnimationRepeat(Animator animation) {
            // TODO Auto-generated method stub
        }
    };

    private void drawBlurredBackground(){
        maxHeight = getRootView().findViewById(R.id.content_view).getMeasuredHeight();
        blurredBitmap = ImageUtil.drawViewToBitmap(
                blurredBitmap, getRootView().findViewById(R.id.content_view), contentWidth, maxHeight, 5, windowBackground);
        blurredBitmap = ImageUtil.BlurBitmap(mainActivity, blurredBitmap, 20);
        blurredBg.setVisibility(View.VISIBLE);
        blurredBg.setImageBitmap(blurredBitmap);

    }

    public void cutBackground(float percent){
        if(blurredBitmap == null)
            return;
        Bitmap mutableBitmap = blurredBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Paint p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Canvas c = new Canvas(mutableBitmap);
        c.drawRect(0, percent*(mutableBitmap.getHeight()-1), mutableBitmap.getWidth()-1, mutableBitmap.getHeight()-1, p);
        blurredBg.setImageBitmap(mutableBitmap);
    }

    private int getToggleYPosition(){
        View toggle = findViewById(R.id.notif_toggle);
        int [] pos = new int[2];
        toggle.getLocationOnScreen(pos);
        return pos[1]+toggle.getMeasuredHeight()-topOffset;
    }

    private void animateBlurredBg(final int animationTime){
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        cutBackground((float) getToggleYPosition() / maxHeight);
                        timeCounter += 5;
                        if (timeCounter == animationTime) {
                            t.cancel();
                            timeCounter = 0;
                        }
                    }
                });
            }
        }, 0, 5);
    }

}
