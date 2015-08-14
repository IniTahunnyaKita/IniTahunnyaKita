package com.molaja.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.molaja.android.R;
import com.molaja.android.activities.ShopActivity;
import com.molaja.android.widget.ProfileItem;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Created by Florian on 1/9/2015.
 */
public class ShopProfileFragment extends BaseFragment implements View.OnClickListener {

    public static final String PROFILE_PICTURE_URL = "PICTURE_URL";
    public static final String SHOP_NAME = "SHOP_NAME";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView  = inflater.inflate(R.layout.fragment_shop_profile, container, false);

        setFragmentView(fragmentView);

        initViews();

        return fragmentView;
    }

    private void initViews() {
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        ImageView featuredImage = (ImageView) findViewById(R.id.featured_image);
        TextView shopName = (TextView) findViewById(R.id.shop_name);
        ProfileItem rating = (ProfileItem) findViewById(R.id.profile_item_rating);
        ProfileItem items = (ProfileItem) findViewById(R.id.profile_item_items);
        final FloatingActionButton browseBtn = (FloatingActionButton) findViewById(R.id.browse_btn);

        PicassoPalette paletteCallback = PicassoPalette.with(getArguments()
                .getString(ShopProfileFragment.PROFILE_PICTURE_URL), profilePicture)
                .use(PicassoPalette.Profile.VIBRANT)
                .intoBackground(featuredImage)
                .intoCallBack(new BitmapPalette.CallBack() {
                    @Override
                    public void onPaletteLoaded(Palette palette) {
                        browseBtn.setRippleColor(palette.getVibrantColor(R.color.Teal));
                    }
                });

        Picasso.with(getActivity())
                .load(getArguments().getString(ShopProfileFragment.PROFILE_PICTURE_URL))
                .fit().centerCrop()
                .into(profilePicture, paletteCallback);

        shopName.setText(getArguments().getString(ShopProfileFragment.SHOP_NAME));

        Random rand = new Random();

        rating.setItemValue(5d + (double) rand.nextInt(50) / 10);
        items.setItemValue(rand.nextInt(50));

        browseBtn.setOnClickListener(this);
        findViewById(R.id.browse_text).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (getActivity() instanceof ShopActivity)
            ((ShopActivity) getActivity()).goToCollection();
    }
}

