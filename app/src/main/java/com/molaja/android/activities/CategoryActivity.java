package com.molaja.android.activities;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.molaja.android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by florianhidayat on 3/7/15.
 */
public class CategoryActivity extends BaseActivity {
    private static final long NEXT_BUTTON_ANIMATION_DELAY = 500;
    public static final String CATEGORY_BG_EXTRA = "CATEGORY_BG";
    public static final String CATEGORY_ICON_EXTRA = "CATEGORY_ICON";
    public static final String CATEGORY_TITLE_EXTRA = "CATEGORY_TITLE";

    private FloatingActionButton nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initViews();
    }

    private void initViews() {
        final ImageView categoryBg = (ImageView) findViewById(R.id.category_bg);
        ImageView categoryIcon = (ImageView) findViewById(R.id.category_icon);
        TextView categoryTitle = (TextView) findViewById(R.id.category_title);
        nextButton = (FloatingActionButton) findViewById(R.id.next_button);

        nextButton.setScaleX(0f);
        nextButton.setScaleY(0f);

        String title = getIntent().getStringExtra(CATEGORY_TITLE_EXTRA);
        String background = getIntent().getStringExtra(CATEGORY_BG_EXTRA);
        int drawableId = 0;

        if (title.equals("Fashion")){
            drawableId = R.drawable.icon_fashion;
            background = "http://media.digitalphotogallery.com/kecelafvsrvp/images/" +
                    "4ae54e02-7e81-11e2-8c47-fefd616b8533/" +
                    "london_fashion_weekend14_website_image_wlvx_wuxga.jpg?20130224125426";
        } else if(title.equals("Beauty")){
            drawableId = R.drawable.icon_beauty;
            background = "http://i.ytimg.com/vi/R3qGjuDFOXk/maxresdefault.jpg";
        } else if(title.equals("Games & Toys")){
            drawableId = R.drawable.icon_toys;
            background = "http://7-themes.com/data_images/out/75/7030309-smurfs-toys.jpg";
        } else if(title.equals("Bags")){
            drawableId = R.drawable.icon_bags;
            background = "http://www.robertearp.com/data/photos/35_1bags9.jpg";
        } else if(title.equals("Kids")){
            drawableId = R.drawable.icon_kids;
            background = "http://shefashionstyle.com/wp-content/uploads/2013/04/kids-summer-party-wears-2013-4.jpg";
        } else if(title.equals("Home")){
            drawableId = R.drawable.icon_home;
            background = "http://redaksikita.com/wp-content/uploads/2015/06/for-home-interiors-desktop.jpg";
        } else if(title.equals("Gadgets")){
            drawableId = R.drawable.icon_gadgets;
            background = "http://www.wallpaperseek.com/gadgets-&-books_wallpapers_6186_1920x1200.jpg";
        } else if(title.equals("Electronics")){
            drawableId = R.drawable.icon_electronics;
            background = "http://www.geappliances.com/images/design_center/kitchen-pictures/magnify/pinkkitchen.jpg";
        } else if(title.equals("Sports")){
            drawableId = R.drawable.icon_sports;
        } else if(title.equals("Books")){
            drawableId = R.drawable.icon_books;
        }

        Picasso.with(this)
                .load(drawableId)
                .fit().noFade()
                .into(categoryIcon);

        Picasso.with(this)
                .load(background)
                .into(categoryBg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Drawable drawable = categoryBg.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            Palette palette = Palette.from(((BitmapDrawable) drawable).getBitmap()).generate();
                            nextButton.setVisibility(View.VISIBLE);
                            nextButton.setRippleColor(palette.getVibrantColor(getColor(R.color.Teal)));

                            nextButton.animate()
                                    .scaleX(1f).scaleY(1f)
                                    .setInterpolator(new FastOutSlowInInterpolator())
                                    .setStartDelay(NEXT_BUTTON_ANIMATION_DELAY)
                                    .start();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

        categoryTitle.setText(title);
    }
}
