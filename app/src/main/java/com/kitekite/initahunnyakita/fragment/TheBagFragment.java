package com.kitekite.initahunnyakita.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.MainActivity;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.widget.ProfileItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Florian on 2/12/2015.
 */
public class TheBagFragment extends Fragment{
    private SharedPreferences loginCookies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.header_the_bag, container, false);
        loginCookies = getActivity().getSharedPreferences(Global.login_cookies, 0);
        initProfile(fragmentView);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initProfile(final View v){
        String name = loginCookies.getString(Global.name,"Florian Pranata");
        ((TextView)v.findViewById(R.id.user_fullname)).setText(name);
        ((ProfileItem)v.findViewById(R.id.profile_item_following)).setItemValue(56);
        ((ProfileItem)v.findViewById(R.id.profile_item_shares)).setItemValue(71);
        ((ProfileItem)v.findViewById(R.id.profile_item_friends)).setItemValue(650);
        //load image
        final ImageView profilePicture = (ImageView)v.findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).logOut();
            }
        });
        Picasso.with(getActivity())
                .load(loginCookies.getString(Global.photo_url, "file:///android_asset/prof_pic.jpg"))
                .error(R.drawable.ensa_shop)
                .into(profilePicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        BitmapDrawable ppDrawable = ((BitmapDrawable) profilePicture.getDrawable());
                        Bitmap blurredImg;
                        if (ppDrawable != null) {
                            Bitmap profileBitmap = ppDrawable.getBitmap();
                            blurredImg = ImageUtil.BlurBitmap(getActivity(), profileBitmap, 20);
                            ((ImageView) v.findViewById(R.id.list_bg)).setImageBitmap(blurredImg);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
