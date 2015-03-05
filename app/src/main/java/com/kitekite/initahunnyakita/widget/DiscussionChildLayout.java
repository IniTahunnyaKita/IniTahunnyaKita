package com.kitekite.initahunnyakita.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Florian on 3/1/2015.
 */
public class DiscussionChildLayout extends LinearLayout{
    private boolean isExpanded = false;

    public DiscussionChildLayout(Context context) {
        super(context);
    }

    public DiscussionChildLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscussionChildLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setExpanded(boolean expanded){
        isExpanded = expanded;
    }

    public boolean isExpanded(){
        return isExpanded;
    }
}
