package com.kitekite.initahunnyakita.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.util.ImageUtil;

/**
 * Created by florian on 2/9/2015.
 */
public class RoundLinearLayout extends LinearLayout{
    private Path mPath = new Path();
    private float mRadius = 20f;

    public RoundLinearLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedRelativeLayout);
        try {
            mRadius = a.getDimension(R.styleable.RoundedRelativeLayout_cornerRadius, 0);
        } finally {
            a.recycle();
        }*/
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath = new Path();
        mPath.addRoundRect(new RectF(0, 0, w, h), mRadius, mRadius, Path.Direction.CW);
        mPath.close();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
