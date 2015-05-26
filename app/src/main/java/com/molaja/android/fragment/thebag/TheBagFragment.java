package com.molaja.android.fragment.thebag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.molaja.android.MolajaApplication;
import com.molaja.android.R;
import com.molaja.android.activities.MainActivity;
import com.molaja.android.activities.UploadImageActivity;
import com.molaja.android.adapter.TheBagTabAdapter;
import com.molaja.android.model.BusEvent.GetBuddiesEvent;
import com.molaja.android.model.User;
import com.molaja.android.util.BackendHelper;
import com.molaja.android.util.ImageUtil;
import com.molaja.android.util.Scroller;
import com.molaja.android.util.Validations;
import com.molaja.android.widget.BaseFragment;
import com.molaja.android.widget.BuddyButton;
import com.molaja.android.widget.ProfileItem;
import com.nineoldandroids.view.ViewHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by Florian on 2/12/2015.
 */
public class TheBagFragment extends BaseFragment implements Target, Scroller, ViewPager.OnPageChangeListener,
        View.OnClickListener {
    private static final String DEFAULT_PROFILE_PICTURE = "file:///android_asset/default_profile_picture.jpg";
    private int DEFAULT_THEME;
    private Palette.Swatch userSwatch;
    public final int PICK_IMAGE_REQUEST_CODE = 1;
    public final int TAKE_PHOTO_REQUEST_CODE = 2;
    public final int EDIT_IMAGE_REQUEST_CODE = 3;
    public static final int ARE_BUDDIES = 1;
    public static final int PENDING_INVITE = 2;
    public static final int NOT_BUDDIES = -1;

    View fragmentView, mHeader, mHeaderShadow;
    ViewPager mViewPager;
    SmartTabLayout viewPagerTab;
    ImageView profilePicture, blurredBg;
    ProfileItem activitiesItem, buddiesItem, subscriptionsItem;
    BuddyButton buddyBtn;
    Button discussBtn;

    User user;
    UploadImageTask uploadImageTask;
    Context mContext;

    private int mMinHeaderTranslation;
    private String mCurrentPhotoPath;
    private boolean isCurrentUser;
    private int areBuddies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_the_bag, container, false);

        mContext = getActivity().getApplicationContext();

        bindViews();

        DEFAULT_THEME = getResources().getColor(R.color.Teal);

        String username;
        final boolean withRebound;
        if (getArguments() != null &&
                !getArguments().getString("USERNAME").equals(User.getCurrentUser(getActivity()).username) ) {
            isCurrentUser = false;
            username = getArguments().getString("USERNAME");
            withRebound = true;
        } else {
            isCurrentUser = true;
            withRebound = false;
            user = User.getCurrentUser(getActivity());
            username = user.username;
            initProfile(fragmentView, true);
            //initPager();
        }

        BackendHelper.getProfile(getActivity(), username, new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    user = new Gson().fromJson(result.get("user"), User.class);

                    if (result.get("are_buddies") instanceof JsonNull) {
                        areBuddies = NOT_BUDDIES;
                    } else if (result.get("are_buddies").getAsBoolean()) {
                        areBuddies = ARE_BUDDIES;
                    } else {
                        areBuddies = PENDING_INVITE;
                    }


                    initProfile(fragmentView, withRebound);
                    initPager();

                    BackendHelper.getBuddies(getActivity(), user.id, 1, new FutureCallback<JsonObject>() {

                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            EventBus.getDefault().post(new GetBuddiesEvent(result));
                        }
                    });
                }
            }
        });



        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }*/
        return fragmentView;
    }

    private void bindViews() {
        mViewPager = (ViewPager) fragmentView.findViewById(R.id.the_bag_pager);
        blurredBg = (ImageView) fragmentView.findViewById(R.id.blurred_bg);
        mHeader = fragmentView.findViewById(R.id.header);
        mHeaderShadow = fragmentView.findViewById(R.id.header_shadow);
        profilePicture = (ImageView) fragmentView.findViewById(R.id.profile_picture);
        activitiesItem = (ProfileItem) fragmentView.findViewById(R.id.profile_item_subscriptions);
        subscriptionsItem = (ProfileItem) fragmentView.findViewById(R.id.profile_item_activities);
        buddiesItem = (ProfileItem) fragmentView.findViewById(R.id.profile_item_buddies);
        buddyBtn = (BuddyButton) fragmentView.findViewById(R.id.buddy_btn);
        discussBtn = (Button) fragmentView.findViewById(R.id.discuss_btn);
        viewPagerTab = (SmartTabLayout) fragmentView.findViewById(R.id.viewpager_tab);
    }

    private int getActionBarHeight() {
        final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    public void initProfile(final View v, boolean withRebound){
        ((TextView)v.findViewById(R.id.user_fullname)).setText(user.name);
        ((TextView)v.findViewById(R.id.username)).setText(user.username);
        subscriptionsItem.setItemValue(0);
        activitiesItem.setItemValue(0);
        buddiesItem.setItemValue(user.buddies_count);

        if (isCurrentUser) {
            mHeader.findViewById(R.id.header_btns_container).setVisibility(View.GONE);
            profilePicture.setOnClickListener(this);
        } else {
            buddyBtn.setOnClickListener(this);
            discussBtn.setOnClickListener(this);
        }

        buddyBtn.setStatus(areBuddies);

        //measure header size and pass it to the tabs
        mHeader.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mMinHeaderHeight = mHeader.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.profile_tab_height);
        mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

        //load image
        String image = Validations.isEmptyOrNull(user.image)? DEFAULT_PROFILE_PICTURE : user.image;
        setProfilePicture(image, withRebound);
    }

    private void initPager() {
        mViewPager.setAdapter(new TheBagTabAdapter(getChildFragmentManager(), this,
                mHeader.getMeasuredHeight(), isCurrentUser));
        mViewPager.setOffscreenPageLimit(3);

        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {

            @Override
            public View createTabView(ViewGroup viewGroup, int position, PagerAdapter pagerAdapter) {
                ImageView icon = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.child_thebag_tab, viewGroup, false);
                switch (position) {
                    case 0:
                        icon.setImageResource(R.drawable.ic_activities_tab_normal);
                        icon.setTag("TAB0");
                        break;
                    case 1:
                        icon.setImageResource(R.drawable.ic_buddies_tab_normal);
                        icon.setTag("TAB1");
                        break;
                    case 2:
                        icon.setImageResource(R.drawable.ic_settings_tab_normal);
                        icon.setTag("TAB2");
                        break;
                }
                return icon;
            }
        });
        viewPagerTab.setViewPager(mViewPager);
        viewPagerTab.setOnPageChangeListener(this);
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    public void setProfilePicture(String imagePath, final boolean withRebound) {
        Picasso.with(getActivity())
                .load(imagePath)
                .placeholder(new ColorDrawable(getResources().getColor(R.color.Gray)))
                .into(profilePicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (withRebound) {
                            //do a beautiful spring animation
                            SpringSystem springSystem = SpringSystem.create();
                            Spring spring = springSystem.createSpring();
                            spring.addListener(new SimpleSpringListener() {
                                @Override
                                public void onSpringUpdate(Spring spring) {
                                    super.onSpringUpdate(spring);
                                    float value = (float) spring.getCurrentValue();
                                    profilePicture.setScaleX(value);
                                    profilePicture.setScaleY(value);
                                }
                            });
                            spring.setEndValue(1);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
        Picasso.with(getActivity())
                .load(imagePath)
                .placeholder(new ColorDrawable(getResources().getColor(R.color.LightGrey)))
                .into(this);

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).initProfileActionBar(user.name, imagePath);
        }
    }

    public void sendPickImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image)), PICK_IMAGE_REQUEST_CODE);
    }

    public void sendTakePhotoIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePicture, TAKE_PHOTO_REQUEST_CODE);
            }
        }
    }

    private void setTabIconColor(int whichTab, int color, float alpha) {
        Log.d("settabcolor","tab:"+whichTab);
        ImageView tab = (ImageView) fragmentView.findViewWithTag("TAB"+whichTab);

        if (tab != null) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            float alphaf = alpha * 255;
            tab.setColorFilter(Color.argb((int) alphaf, red, green, blue));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "temp";
        File picturesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File storageDir = new File(picturesDir.getAbsolutePath() + "/Molaja");

        if (!storageDir.exists())
            storageDir.mkdir();

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onDetach() {
        Ion.getDefault(mContext).cancelAll("GET_PROFILE");
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //cancel if not result ok
        if(resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            Uri selectedImage = data.getData();
            Intent intent = new Intent(getActivity(), UploadImageActivity.class);
            intent.putExtra("URI", selectedImage);
            startActivityForResult(intent, EDIT_IMAGE_REQUEST_CODE);
        } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            Intent intent = new Intent(getActivity(), UploadImageActivity.class);
            intent.putExtra("URI", Uri.parse(mCurrentPhotoPath));
            startActivityForResult(intent, EDIT_IMAGE_REQUEST_CODE);
        } else if (requestCode == EDIT_IMAGE_REQUEST_CODE) {

            if(uploadImageTask != null)
                uploadImageTask.cancel(true);

            uploadImageTask = new UploadImageTask(getActivity().getApplicationContext());
            uploadImageTask.execute(data.getStringExtra("image_path"));
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        blurredBg.setImageBitmap(ImageUtil.BlurBitmap(getActivity(),bitmap, 20));
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if (!isVisible())
                    return;

                setActionBarColor(palette.getDarkVibrantColor(DEFAULT_THEME));
                viewPagerTab.setBackgroundColor(palette.getDarkVibrantColor(DEFAULT_THEME));
                userSwatch = palette.getVibrantSwatch();

                if (userSwatch != null) {
                    //setTabIconColor(mViewPager.getCurrentItem(), userSwatch.getRgb(), 1f);
                    setButtonsColor(userSwatch.getRgb());
                    activitiesItem.setValueColor(userSwatch.getRgb());
                    buddiesItem.setValueColor(userSwatch.getRgb());
                    subscriptionsItem.setValueColor(userSwatch.getRgb());
                }
            }
        });
    }

    /*private void changeHeaderBackgroundHeightAnimated(boolean shouldShowGap, boolean animated) {
        if (mGapIsChanging) {
            return;
        }
        final int heightOnGapShown = mHeaderBar.getHeight();
        final int heightOnGapHidden = mHeaderBar.getHeight() + mActionBarSize;
        final float from = mHeaderBackground.getLayoutParams().height;
        final float to;
        if (shouldShowGap) {
            if (!mGapHidden) {
                // Already shown
                return;
            }
            to = heightOnGapShown;
        } else {
            if (mGapHidden) {
                // Already hidden
                return;
            }
            to = heightOnGapHidden;
        }
        if (animated) {
            //ViewPropertyAnimatorCompat.animate(mHeaderBackground).cancel();
            ValueAnimator a = ValueAnimator.ofFloat(from, to);
            a.setDuration(100);
            a.setInterpolator(new AccelerateDecelerateInterpolator());
            a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float height = (float) animation.getAnimatedValue();
                    changeHeaderBackgroundHeight(height, to, heightOnGapHidden);
                }
            });
            a.start();
        } else {
            changeHeaderBackgroundHeight(to, to, heightOnGapHidden);
        }
    }*/

    @SuppressWarnings("deprecation")
    private void setButtonsColor(int color) {
        StateListDrawable newDrawable = new StateListDrawable();
        StateListDrawable buddiesBtnDrawable = new StateListDrawable();
        GradientDrawable pressed;
        GradientDrawable normal;

        GradientDrawable buddiesDisabled = (GradientDrawable) getResources().getDrawable(R.drawable.btn_buddies_bg_disabled);
        buddiesDisabled.setStroke(MolajaApplication.dpToPx(1), color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            normal = (GradientDrawable) getResources().getDrawable(R.drawable.default_btn_bg_normal, null);
            pressed = (GradientDrawable) getResources().getDrawable(R.drawable.default_btn_bg_pressed, null);
        } else {
            normal = (GradientDrawable) getResources().getDrawable(R.drawable.default_btn_bg_normal);
            pressed = (GradientDrawable) getResources().getDrawable(R.drawable.default_btn_bg_pressed);
        }

        pressed.setColor(color);
        pressed.setColorFilter(Color.parseColor("#77000000"), PorterDuff.Mode.DARKEN);
        normal.setColor(color);

        newDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        newDrawable.addState(new int[] { }, normal);
        buddiesBtnDrawable.addState(new int[]{ }, buddiesDisabled);

        //assign the new drawable to the buttons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //discussBtn.setBackground(newDrawable);
            buddyBtn.setBackground(buddiesBtnDrawable);
        } else {
            //discussBtn.setBackgroundDrawable(newDrawable);
            buddyBtn.setBackgroundDrawable(buddiesBtnDrawable);
        }
        ViewCompat.setBackgroundTintList(discussBtn, ColorStateList.valueOf(color));
        buddyBtn.setColor(color);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    @Override
    public void onYScroll(int totalScroll) {
        ViewCompat.setTranslationY(mHeader, Math.max(-totalScroll, mMinHeaderTranslation));
        ViewCompat.setTranslationY(mHeaderShadow, Math.max(-totalScroll, mMinHeaderTranslation));
        mViewPager.getChildAt(1);
        float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
        setTitleAlpha(ratio);

        switch (actionBarMode) {
            case MODE_ACTIONBAR_DEFAULT:
                if (totalScroll > Math.abs(mMinHeaderTranslation)) {
                    Log.d ("basdl", "mminheadertr:"+mMinHeaderTranslation);
                    switchActionBarMode(MODE_ACTIONBAR_PROFILE);
                }
                break;
            case MODE_ACTIONBAR_PROFILE:
                if (totalScroll < Math.abs(mMinHeaderTranslation)) {
                    Log.d ("basdls", "mminheadertr:"+mMinHeaderTranslation);
                    switchActionBarMode(MODE_ACTIONBAR_DEFAULT);
                }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //int color = userSwatch==null? DEFAULT_THEME : userSwatch.getRgb();
        int color = Color.WHITE;
        setTabIconColor(position, color, 1 - positionOffset);

        if(position + 1 < mViewPager.getAdapter().getCount())
            setTabIconColor(position + 1, color, positionOffset);

    }

    @Override
    public void onPageSelected(int position) {
        /*for (int i=0; i<mViewPager.getAdapter().getCount(); i++) {
            if (i == position) {
                if (userSwatch != null) {
                    setTabIconColor(i, userSwatch.getRgb(), 1f);
                } else {
                    setTabIconColor(i, DEFAULT_THEME, 1f);
                }
            } else {
                setTabIconColor(i, DEFAULT_THEME, 0f);
            }
        }*/

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_picture:

                new MaterialDialog.Builder(getActivity())
                        .title(R.string.change_profile_picture)
                        .items(R.array.change_profile_picture_options)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                switch (which) {
                                    case 0:
                                        sendPickImageIntent();
                                        break;
                                    case 1:
                                        //TODO import from facebook
                                        break;
                                    case 2:
                                        sendTakePhotoIntent();
                                        break;
                                    case 3:
                                        //TODO delete current photo
                                        break;
                                }

                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .show();
                break;
            case R.id.buddy_btn_bg:
                if (areBuddies == NOT_BUDDIES) {
                    BackendHelper.addBuddy(getActivity(), user.id);
                    areBuddies = PENDING_INVITE;
                    buddyBtn.setStatus(areBuddies);
                } else if (areBuddies == PENDING_INVITE) {

                }
                break;
            case R.id.discuss_btn:
                //TODO do sth
                break;
        }
    }

    private class UploadImageTask extends AsyncTask<String, Void, Void> {
        final String UPLOAD_URL = "http://molaja-backend.herokuapp.com/api/v1/picture_uploader.json";
        final String CONTENT_TYPE_PREFIX = "data:image/jpg;base64,";
        Context context;
        User userInstance;

        public UploadImageTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlphaAnimation animation = new AlphaAnimation(0.25f,1f);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setDuration(1000);
            profilePicture.setAnimation(animation);

            userInstance = User.getCurrentUser(context);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                //upload to database
                JsonObject json = new JsonObject();
                JsonObject user = new JsonObject();
                user.addProperty("authentication_token", userInstance.authentication_token);
                user.addProperty("image", CONTENT_TYPE_PREFIX +
                        Base64.encodeToString(getBytesFromFile(params[0]), Base64.DEFAULT));
                json.add("user", user);

                Ion.with(context)
                        .load(UPLOAD_URL)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .noCache()
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject jsonObject) {
                                profilePicture.clearAnimation();

                                if (e != null) {
                                    Toast.makeText(context, getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
                                } else {
                                    userInstance.image = jsonObject.getAsJsonPrimitive("image").getAsString();
                                    userInstance.save(context);

                                    TheBagFragment.this.user = userInstance;

                                    setProfilePicture(TheBagFragment.this.user.image, true);
                                }
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private byte[] getBytesFromFile(String path) throws IOException {
            File file = new File(path);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return bytes;
        }
    }
}
