package com.molaja.android.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
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
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.molaja.android.R;
import com.molaja.android.util.ImageUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Florian on 1/26/2015.
 */
public class NotificationLayout extends RelativeLayout{
    public final int DEFAULT_ANIMATION_DURATION = 1000;

    private ImageView blurredBg;
    private int initialY, maxHeight, mActionBarSize, topOffset;
    private float initialTouchY;
    private int triggerPixel = 250;
    private int goBack;
    private int mDuration = DEFAULT_ANIMATION_DURATION;
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
        initLayout();
    }

    public NotificationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public NotificationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View toggle = findViewById(R.id.notif_toggle);
        toggle.setOnTouchListener(new OnTouchListener() {
            private final int RELEASE_DURATION = 300;

            int[] location = new int[2];

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                blurredBg = (ImageView) getRootView().findViewById(R.id.blur_image);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //ignore double tap
                        //if(event.getRawY()<mActionBarSize+topOffset)
                        //return true;
                        setVisibility(VISIBLE);
                        getLocationInWindow(location);
                        initialY = location[1];
                        initialTouchY = event.getRawY();
                        blurredBg.setVisibility(View.VISIBLE);
                        drawBlurredBackground();
                        cutBackground((float) getToggleYPosition() / maxHeight);
                        isBlurred = true;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        setVisibility(VISIBLE);
                        if (!isBlurred) {
                            Log.d("hello", "hello");
                            getLocationInWindow(location);
                            initialY = location[1];
                            initialTouchY = event.getRawY();
                            blurredBg.setVisibility(View.VISIBLE);
                            drawBlurredBackground();
                            cutBackground((float) getToggleYPosition() / maxHeight);
                            isBlurred = true;
                        }
                        int newPos;
                        if (isExpanded && (event.getRawY() - initialTouchY > 0))
                            newPos = initialY + (int) (event.getRawY() - initialTouchY) / 3;
                        else
                            newPos = initialY + (int) (event.getRawY() - initialTouchY);
                        cutBackground((float) getToggleYPosition() / maxHeight);
                        props = new ArrayList<>(1);
                        props.add(PropertyValuesHolder.ofFloat("y", newPos));
                        anim = ObjectAnimator.ofPropertyValuesHolder(NotificationLayout.this,
                                props.toArray(new PropertyValuesHolder[props.size()]));
                        anim.setDuration(0);
                        anim.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //ignore double tap
                        //if(event.getRawY()<mActionBarSize)
                        //return true;
                        if (!isExpanded) {
                            if (triggerPixel < event.getRawY() - initialTouchY) {
                                int y = 0;
                                isExpanded = true;
                                setDuration(RELEASE_DURATION);
                                animate().translationY(y)
                                        .setDuration(RELEASE_DURATION)
                                        .setListener(animatorListener)
                                        .start();
                            } else {
                                int y = goBack;
                                isExpanded = false;
                                props = new ArrayList<>(1);
                                props.add(PropertyValuesHolder.ofFloat("y", y));
                                anim = ObjectAnimator.ofPropertyValuesHolder(NotificationLayout.this,
                                        props.toArray(new PropertyValuesHolder[props.size()]));
                                anim.setDuration(RELEASE_DURATION);
                                setDuration(300);
                                anim.addListener(animatorListener);
                                anim.start();
                            }
                        } else {
                            if (triggerPixel < initialTouchY - event.getRawY()) {
                                int y = goBack;
                                isExpanded = false;
                                props = new ArrayList<PropertyValuesHolder>(1);
                                props.add(PropertyValuesHolder.ofFloat("y", y));
                                anim = ObjectAnimator.ofPropertyValuesHolder(NotificationLayout.this,
                                        props.toArray(new PropertyValuesHolder[props.size()]));
                                anim.setDuration(300);
                                anim.addListener(animatorListener);
                                anim.start();
                            } else {
                                int y = 0;
                                isExpanded = true;
                                props = new ArrayList<>(1);
                                props.add(PropertyValuesHolder.ofFloat("y", y));
                                ObjectAnimator anim = null;
                                anim = ObjectAnimator.ofPropertyValuesHolder(NotificationLayout.this,
                                        props.toArray(new PropertyValuesHolder[props.size()]));
                                anim.setDuration(300);
                                anim.addListener(animatorListener);
                                anim.start();
                            }
                        }

                        return true;
                }
                return false;
            }
        });
    }

    public boolean isLayoutExpanded(){
        return isExpanded;
    }

    public void initLayout(){
        //obtain actionbar size
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        maxHeight = getResources().getDisplayMetrics().heightPixels - mActionBarSize;
        goBack = -getResources().getDisplayMetrics().heightPixels;
        anim = ObjectAnimator.ofFloat(this, "y", 0, -getResources().getDisplayMetrics().heightPixels + mActionBarSize);
        anim.setDuration(0);
        anim.start();

        contentWidth = getResources().getDisplayMetrics().widthPixels;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //find blurred bg
                blurredBg = (ImageView) getRootView().findViewById(R.id.blur_image);

                //get window background drawable
                TypedValue outValue = new TypedValue();
                int[] attrs = { android.R.attr.windowBackground };
                getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, outValue, true);
                TypedArray style = getContext().getTheme().obtainStyledAttributes(outValue.resourceId, attrs);
                windowBackground = style.getDrawable(0);
                style.recycle();

                //compute top offset
                topOffset = getResources().getDisplayMetrics().heightPixels - getMeasuredHeight();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);


    }

    private void setDuration(int duration) {
        this.mDuration = duration;
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener(){
        @Override
        public void onAnimationStart(Animator animation) {
            blurredBg.setVisibility(View.VISIBLE);
            animateBlurredBg(mDuration);
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            if(!isExpanded) {
                //recycle bitmap
                if(blurredBitmap!=null) {
                    blurredBitmap.recycle();
                    blurredBitmap = null;
                }

                blurredBg.setVisibility(GONE);
                setVisibility(GONE);
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

    /**
     * blur whatever is on the main view.
     */
    private void drawBlurredBackground(){
        maxHeight = getRootView().findViewById(R.id.content_view).getMeasuredHeight();
        blurredBitmap = ImageUtil.drawViewToBitmap(
                blurredBitmap, getRootView().findViewById(R.id.content_view), contentWidth, maxHeight, 5, windowBackground);
        blurredBitmap = ImageUtil.BlurBitmap(getContext(), blurredBitmap, 20);
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
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        cutBackground((float) getToggleYPosition() / maxHeight);
                        timeCounter += 10;
                        if (timeCounter == animationTime) {
                            t.cancel();
                            timeCounter = 0;
                        }
                    }
                });
            }
        }, 0, 10);
    }

    public void expandLayout(){
        drawBlurredBackground();
        cutBackground((float) getToggleYPosition() / maxHeight);
        setVisibility(VISIBLE);
        int y = 0 ;
        isExpanded = true;

        setDuration(DEFAULT_ANIMATION_DURATION);

        animate().translationY(y)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(animatorListener)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }

    public void collapseLayout(){
        int y = goBack;
        isExpanded = false;

        setDuration(DEFAULT_ANIMATION_DURATION);

        animate().translationY(y)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(animatorListener)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }

}
