package com.molaja.android.fragment.thebag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.molaja.android.R;
import com.molaja.android.activities.UploadImageActivity;
import com.molaja.android.adapter.TheBagTabAdapter;
import com.molaja.android.model.User;
import com.molaja.android.util.BackendHelper;
import com.molaja.android.util.ImageUtil;
import com.molaja.android.util.Validations;
import com.molaja.android.widget.ProfileItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Florian on 2/12/2015.
 */
public class TheBagFragment extends Fragment implements Target {
    public final int PICK_IMAGE_REQUEST_CODE = 1;
    public final int TAKE_PHOTO_REQUEST_CODE = 2;
    public final int EDIT_IMAGE_REQUEST_CODE = 3;

    ViewPager mViewPager;
    ImageView profilePicture;
    ImageView blurredBg;
    View mHeader;

    User currentUser;
    UploadImageTask uploadImageTask;
    String mCurrentPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_the_bag, container, false);
        initProfile(fragmentView);

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }*/
        //mViewPager.setonp
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initProfile(final View v){
        currentUser = User.getCurrentUser(getActivity());

        mViewPager = (ViewPager) v.findViewById(R.id.the_bag_pager);
        blurredBg = (ImageView) v.findViewById(R.id.blurred_bg);
        mHeader = v.findViewById(R.id.header);
        ((TextView)v.findViewById(R.id.user_fullname)).setText(currentUser.name);
        ((TextView)v.findViewById(R.id.username)).setText(currentUser.username);
        ((ProfileItem)v.findViewById(R.id.profile_item_following)).setItemValue(56);
        ((ProfileItem)v.findViewById(R.id.profile_item_shares)).setItemValue(71);
        ((ProfileItem)v.findViewById(R.id.profile_item_friends)).setItemValue(650);

        v.findViewById(R.id.profile_item_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendHelper.logOut(getActivity());
            }
        });

        //load image
        profilePicture = (ImageView)v.findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        String image = Validations.isEmptyOrNull(currentUser.image)? "file:///android_asset/default_profile_picture.jpg" : currentUser.image;
        setProfilePicture(image);

        //init pager
        initPager(v);
    }

    private void initPager(View v) {
        mViewPager.setAdapter(new TheBagTabAdapter(getChildFragmentManager()));
        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpager_tab);
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {

            @Override
            public View createTabView(ViewGroup viewGroup, int position, PagerAdapter pagerAdapter) {
                ImageView icon = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.child_thebag_tab, viewGroup, false);
                switch (position) {
                    case 0:
                        icon.setImageResource(R.drawable.ic_activities_tab);
                        break;
                    case 1:
                        icon.setImageResource(R.drawable.ic_buddies_tab);
                        break;
                    case 2:
                        icon.setImageResource(R.drawable.ic_settings_tab);
                        break;
                }
                return icon;
            }
        });
        viewPagerTab.setViewPager(mViewPager);
    }

    public void setProfilePicture(String imagePath) {
        Picasso.with(getActivity())
                .load(imagePath)
                .placeholder(new ColorDrawable(getResources().getColor(R.color.Gray)))
                .into(profilePicture, new Callback() {
                    @Override
                    public void onSuccess() {
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

                    @Override
                    public void onError() {

                    }
                });
        Picasso.with(getActivity())
                .load(imagePath)
                .placeholder(new ColorDrawable(getResources().getColor(R.color.LightGrey)))
                .into(this);
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
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    private class UploadImageTask extends AsyncTask<String, Void, Void> {
        Context context;
        final String UPLOAD_URL = "http://molaja-backend.herokuapp.com/api/v1/picture_uploader.json";
        final String CONTENT_TYPE_PREFIX = "data:image/jpg;base64,";

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
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                //upload to database
                JsonObject json = new JsonObject();
                JsonObject user = new JsonObject();
                user.addProperty("authentication_token", currentUser.authentication_token);
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
                                    currentUser.image = jsonObject.getAsJsonObject("image").get("url").getAsString();
                                    currentUser.save(context);

                                    setProfilePicture(currentUser.image);
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
