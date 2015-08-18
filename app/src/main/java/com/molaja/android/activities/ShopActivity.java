package com.molaja.android.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.adapter.ShopPagerAdapter;
import com.molaja.android.model.Shop;

/**
 * Created by Florian on 1/27/2015.
 */
public class ShopActivity extends BaseActivity {

    public static final String SHOP_PICTURE = "PICTURE_URL";
    public static final String SHOP_NAME = "SHOP_NAME";

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19)
            MolajaApplication.makeContentAppearBehindStatusBar(this);

        setContentView(R.layout.activity_shop);

        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.shop_pager);

        ShopPagerAdapter adapter = new ShopPagerAdapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putString(SHOP_NAME, getIntent()
                .getStringExtra(SHOP_NAME));
        args.putString(SHOP_PICTURE, getIntent()
                .getStringExtra(SHOP_PICTURE));
        adapter.setArguments(args, 0);
        mViewPager.setAdapter(adapter);

        /*ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        ImageView featuredImage = (ImageView) findViewById(R.id.featured_image);
        TextView shopName = (TextView) findViewById(R.id.shop_name);
        ProfileItem rating = (ProfileItem) findViewById(R.id.profile_item_rating);
        ProfileItem items = (ProfileItem) findViewById(R.id.profile_item_items);

        PicassoPalette paletteCallback = PicassoPalette.with(getIntent()
                .getStringExtra(ProfileFragment.SHOP_PICTURE), profilePicture)
                .use(PicassoPalette.Profile.VIBRANT)
                .intoBackground(featuredImage);

        Picasso.with(this)
                .load(getIntent().getStringExtra(ProfileFragment.SHOP_PICTURE))
                .fit().centerCrop()
                .into(profilePicture, paletteCallback);

        shopName.setText(getIntent().getStringExtra(ProfileFragment.SHOP_NAME));

        Random rand = new Random();

        rating.setItemValue(5d + (double) rand.nextInt(50) / 10);
        items.setItemValue(rand.nextInt(50));*/

    }

    public static Shop convertArgsToShop(Bundle args) {
        Shop shop = new Shop();
        shop.name = args.getString(SHOP_NAME);
        shop.image = args.getString(SHOP_PICTURE);

        return shop;
    }

    public void goToCollection() {
        mViewPager.setCurrentItem(1, true);
    }

}
