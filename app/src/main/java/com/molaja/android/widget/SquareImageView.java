package com.molaja.android.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Florian on 2/25/2015.
 */
public class SquareImageView extends ImageView{
    private Rect rect;
    private boolean filteringEnabled = false;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFilteringEnabled(boolean enabled){
        filteringEnabled = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? width : height;
        setMeasuredDimension(size, size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(filteringEnabled){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setColorFilter(Color.argb(50, 0, 0, 0));
                rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setColorFilter(null);
            }
            if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                setColorFilter(null);
            }
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                setColorFilter(null);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!rect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY())) {
                    setColorFilter(null);
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
