package com.molaja.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.molaja.android.R;

/**
 * Created by Florian on 1/12/2015.
 */
public class ProfileItem extends RelativeLayout{
    private TextView itemValue;
    private TextView itemTitle;

    public ProfileItem(Context context) {
        super(context);
        init(context);
    }

    public ProfileItem(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
        initAttrs(attrs);
    }

    public ProfileItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs){
        //set attributes value
        TypedArray a=getContext().obtainStyledAttributes(attrs,
                R.styleable.ProfileItem);
        itemTitle.setText(a.getText(R.styleable.ProfileItem_item_title));
        a.recycle();
    }

    private void init(Context context){
        inflate(context,R.layout.layout_profile_item, this);
        this.itemValue = (TextView) this.findViewById(R.id.profile_item_value);
        this.itemTitle = (TextView) this.findViewById(R.id.profile_item_title);
    }

    public void setItemValue(String value){
        if(this.itemValue!=null)
            this.itemValue.setText(value);
    }

    public void setItemValue(int value){
        if(this.itemValue!=null)
            this.itemValue.setText(Integer.toString(value));
    }

    public void setItemTitle(String title){
        if(this.itemValue!=null)
            this.itemValue.setText(title);
    }

    public void setValueColor(int color) {
        itemValue.setTextColor(color);
    }
}
