package com.molaja.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by florianhidayat on 28/4/15.
 */
public class CustomEditText extends EditText {
    SoftKeyboardListener keyboardListener;

    public interface SoftKeyboardListener {
        public void onHide();
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyboardListener(SoftKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (keyboardListener != null)
                keyboardListener.onHide();
            //return super.o;
        }
        //return super.dispatchKeyEvent(event);
        return super.onKeyPreIme(keyCode, event);
    }
}
