package com.molaja.android.fragment.itemdetail;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.activities.ItemDetailActivity;
import com.molaja.android.model.ItemVariance;
import com.molaja.android.widget.VariantPickerWidget;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by florian on 2/5/2015.
 */
public class DescriptionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout fragmentView = (LinearLayout) inflater.inflate(R.layout.fragment_item_detail_description, container, false);
        final ImageView shopPicture = (ImageView) fragmentView.findViewById(R.id.description_shop_pic);
        final TextView shopName = (TextView) fragmentView.findViewById(R.id.description_shop_name);
        final View shopIdContainer = fragmentView.findViewById(R.id.shop_id_container);

        //dummy variants
        ItemVariance variance1 = new ItemVariance();
        variance1.varianceTitle = "Size";
        variance1.variants = new String[] {"S", "M", "L"};
        VariantPickerWidget widget = new VariantPickerWidget(fragmentView.getContext());
        widget.initVariants(variance1);
        fragmentView.addView(widget, 1);


        if (ItemDetailActivity.itemInfo != null) {
            Picasso.with(fragmentView.getContext())
                    .load(ItemDetailActivity.itemInfo.getProfileUrl())
                    .into(shopPicture, new Callback() {
                        @Override
                        public void onSuccess() {
                            Palette.from(((BitmapDrawable) shopPicture.getDrawable()).getBitmap())
                                    .generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            Palette.Swatch swatch = palette.getDarkVibrantSwatch();

                                            if (swatch == null) {
                                                swatch = palette.getDarkMutedSwatch();
                                            }

                                            if (swatch != null) {
                                                shopIdContainer.setBackgroundColor(swatch.getRgb());
                                                shopName.setTextColor(swatch.getBodyTextColor());
                                                updateActivityBackground(swatch.getRgb());
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onError() {

                        }
                    });
            shopName.setText(ItemDetailActivity.itemInfo.getFullname());
        }

        return fragmentView;
    }

    private void updateActivityBackground(int color) {
        if (getActivity() != null && getActivity() instanceof ItemDetailActivity) {
            ItemDetailActivity activity = (ItemDetailActivity) getActivity();
            activity.setBackgroundColor(color);
        }
    }
}
