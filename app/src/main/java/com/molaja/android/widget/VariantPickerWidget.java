package com.molaja.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.model.ItemVariance;
import com.molaja.android.util.Validations;

/**
 * Created by florianhidayat on 30/7/15.
 */
public class VariantPickerWidget extends LinearLayout implements View.OnClickListener {
    private static final String LOG_TAG = VariantPickerWidget.class.getSimpleName();
    private static final int TITLE_TEXT_SIZE = 18;
    private static final int VARIANT_TEXT_SIZE = 14;
    private int mPadding, mSelectedVariant;

    public VariantPickerWidget(Context context) {
        super(context);
        init();
    }

    public VariantPickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VariantPickerWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * initializes the LinearLayout parent.
     */
    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        mPadding = getContext().getResources().getDimensionPixelSize(R.dimen.default_medium_margin);
        int margin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(margin, margin, margin, 0);
        setLayoutParams(lp);
    }

    /**
     * Initializes the layout given the variants.
     * @param variance
     */
    public void initVariants(ItemVariance variance) {
        if (!TextUtils.isEmpty(variance.varianceTitle)) {
            TextView title = new TextView(getContext());
            title.setText(variance.varianceTitle);
            title.setTextSize(TITLE_TEXT_SIZE);
            addView(title);
        }

        if (!Validations.isEmptyOrNull(variance.variants)) {
            LinearLayout variantsLayout = new LinearLayout(getContext());
            variantsLayout.setOrientation(HORIZONTAL);
            addView(variantsLayout);

            int grayColor = getResources().getColor(R.color.LightGrey);
            for (int i=0; i<variance.variants.length; i++) {
                String variant  = variance.variants[i];
                TextView variantTv = new TextView(getContext());
                variantTv.setText(variant);
                variantTv.setTextSize(VARIANT_TEXT_SIZE);
                variantTv.setTextColor(grayColor);
                variantTv.setPadding(mPadding, mPadding, mPadding, mPadding);
                variantTv.setOnClickListener(this);
                variantTv.setTag(i);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    variantTv.setBackground(obtainSelectableItemBackground());
                else //noinspection deprecation
                    variantTv.setBackgroundDrawable(obtainSelectableItemBackground());

                int margin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(margin, 0, margin, 0);
                variantsLayout.addView(variantTv, lp);
            }
        }

    }

    private Drawable obtainSelectableItemBackground() {
        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();

        return drawableFromTheme;
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "onclick");
        if (v instanceof TextView) {
            Log.d(LOG_TAG, "v is textview");
            //unselect the previously selected one
            ((TextView) findViewWithTag(mSelectedVariant)).setTypeface(Typeface.DEFAULT);
            ((TextView) findViewWithTag(mSelectedVariant)).setTextColor(getResources().getColor(R.color.LightGrey));
            //select the selected variant
            ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
            ((TextView) v).setTextColor(getResources().getColor(R.color.Black));
            mSelectedVariant = (int) v.getTag();
        }
    }

}
