package com.molaja.android.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.molaja.android.R;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.util.HardcodeValues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Florian on 1/9/2015.
 */
public class ProfileFragment extends Fragment{
    private View mFakeHeader;
    private View mRealHeader;
    public ImageView mHeaderLogo;
    private ListView mListView;
    private Context mContext;
    private int mActionBarTitleColor;
    private int mActionBarHeight;
    private int mMinHeaderTranslation;
    private int mHeaderHeight;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;
    private TypedValue mTypedValue = new TypedValue();
    private static int mFirstTime;
    public String profileUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        mContext = container.getContext();
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.profile_header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

        mHeaderLogo = (ImageView) rootView.findViewById(R.id.header_logo);
        Picasso.with(mContext)
                .load(profileUrl)
                .noFade()
                .error(R.drawable.ensa_shop)
                .into(mHeaderLogo);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).setToolbarTransparent();

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mListView = (ListView) view.findViewById(android.R.id.list);
        mRealHeader = view.findViewById(R.id.header);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFakeHeader = inflater.inflate(R.layout.fake_profile_header,mListView,false);
        mFirstTime = 0;
        mListView.addHeaderView(mFakeHeader);
        initListView();

        //load image
        if(profileUrl==null)
            profileUrl = "file:///android_asset/jersenesia.jpg";

        Picasso.with(mContext)
                .load("file:///android_asset/jersenesia_cover_photo.jpg")
                .error(R.drawable.ensa_shop)
                .into((ImageView)view.findViewById(R.id.header_picture));

        /*if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int scrollY = getScrollY();
                    //sticky actionbar
                    mRealHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                    //header_logo --> actionbar icon
                    float ratio = clamp(mRealHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                    Log.d("taikodok", "kodokbanget");
                    if (mFirstTime > 1) {
                        Log.d("taikodok", "kodok");
                        interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
                    } else if (mFirstTime <= 1)
                        mFirstTime++;
                }
            });
        }*/

    }

    public int getScrollY() {
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mFakeHeader.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    public void initListView(){
        mListView.setDividerHeight(0);
        ArrayList<HangoutPost> list = new ArrayList<HangoutPost>();

        for(int i=0;i< HardcodeValues.fullnames.length;i++){
            HangoutPost post = new HangoutPost();
            post.setProfileUrl(HardcodeValues.profileUrls[i]);
            post.setFullname(HardcodeValues.fullnames[i]);
            post.setTitle(HardcodeValues.titles[i]);
            post.setOverview(HardcodeValues.overviews[i]);
            post.setItemUrl(HardcodeValues.itemUrls[i]);
            post.setPrice(HardcodeValues.prices[i]);
            post.setThumbsUp(HardcodeValues.thumbsUps[i]);
            list.add(post);
        }
        //HangoutAdapter mAdapter= new HangoutAdapter(getActivity(),R.layout.list,list);
        //mListView.setAdapter(mAdapter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void interpolate(View view1, View view2, float interpolation) {
        RectF mRect1 = new RectF();
        RectF mRect2 = new RectF();
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mRealHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min,Math.min(value, max));
    }

    private View getActionBarIconView() {
        ActionBar actionBar = ((ActionBarActivity)mContext).getSupportActionBar();
        return actionBar.getCustomView().findViewById(R.id.header_logo_holder);
    }


}

