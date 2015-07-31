package com.molaja.android.widget;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.molaja.android.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Florian on 2/19/2015.
 */
public class ViewPagerIndicator extends LinearLayout {
    private static final int ANIMATION_TIME = 300;
    private static final int ANIMATION_DELAY = 300;
    int mSelectedPos = 0;
    Context mContext;

    public ViewPagerIndicator(Context context) {
        super(context);
        mContext = context;
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void initIndicators(int count){
        int size = getResources().getDimensionPixelSize(R.dimen.viewpager_indicator_size);
        int spacing = getResources().getDimensionPixelSize(R.dimen.viewpager_indicator_spacing);
        LayoutParams lp = new LayoutParams(size,size);
        lp.setMargins(spacing, spacing, spacing, spacing);
        for(int i=0; i<count; i++){
            ImageView indicator = new ImageView(mContext);
            indicator.setBackgroundResource(R.drawable.pager_indicator);
            indicator.setTag(i);
            addView(indicator, lp);

            indicator.setScaleX(0f);
            indicator.setScaleY(0f);

            if (i != mSelectedPos) {
                indicator.animate()
                        .scaleX(1f).scaleY(1f)
                        .setDuration(ANIMATION_TIME)
                        .setStartDelay(ANIMATION_DELAY)
                        .start();
            }
        }

        TransitionDrawable transition = (TransitionDrawable) findViewWithTag(mSelectedPos).getBackground();
        transition.startTransition(500);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(findViewWithTag(mSelectedPos), "scaleX", 2f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(findViewWithTag(mSelectedPos), "scaleY", 2f);

        scaleUpX.setDuration(ANIMATION_TIME);
        scaleUpY.setDuration(ANIMATION_TIME);

        AnimatorSet scaleUp = new AnimatorSet();

        scaleUp.play(scaleUpX).with(scaleUpY).after(ANIMATION_DELAY);
        scaleUp.start();

    }

    public void selectIndicator(int position){
        //unselect previous indicator
        TransitionDrawable transition1 = (TransitionDrawable) findViewWithTag(mSelectedPos).getBackground();
        transition1.reverseTransition(ANIMATION_TIME);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(findViewWithTag(mSelectedPos), "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(findViewWithTag(mSelectedPos), "scaleY", 1f);

        scaleDownX.setDuration(ANIMATION_TIME);
        scaleDownY.setDuration(ANIMATION_TIME);

        AnimatorSet scaleDown = new AnimatorSet();

        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.start();


        //select current indicator
        TransitionDrawable transition2 = (TransitionDrawable) findViewWithTag(position).getBackground();
        transition2.startTransition(500);
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(findViewWithTag(position), "scaleX", 2f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(findViewWithTag(position), "scaleY", 2f);

        scaleUpX.setDuration(300);
        scaleUpY.setDuration(300);

        AnimatorSet scaleUp = new AnimatorSet();

        scaleUp.play(scaleUpX).with(scaleUpY);
        scaleUp.start();
        mSelectedPos = position;
    }
}
