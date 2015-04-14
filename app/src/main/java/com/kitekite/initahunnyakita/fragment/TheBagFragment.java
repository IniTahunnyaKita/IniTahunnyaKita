package com.kitekite.initahunnyakita.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.UploadImageActivity;
import com.kitekite.initahunnyakita.util.BackendHelper;
import com.kitekite.initahunnyakita.util.Global;
import com.kitekite.initahunnyakita.util.ImageUtil;
import com.kitekite.initahunnyakita.widget.ProfileItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * Created by Florian on 2/12/2015.
 */
public class TheBagFragment extends Fragment{
    public final int PICK_IMAGE_REQUEST_CODE = 1;
    public final int TAKE_PHOTO_REQUEST_CODE = 2;

    private SharedPreferences loginCookies;
    String mCurrentPhotoPath;

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

        v.findViewById(R.id.profile_item_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendHelper.logOut(getActivity());
            }
        });

        //load image
        final ImageView profilePicture = (ImageView)v.findViewById(R.id.profile_picture);
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

    public void sendPickImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image)), PICK_IMAGE_REQUEST_CODE);
    }

    public void sendTakePhotoIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePicture, TAKE_PHOTO_REQUEST_CODE);
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
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
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
            startActivity(intent);
        } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            Intent intent = new Intent(getActivity(), UploadImageActivity.class);
            intent.putExtra("URI", Uri.parse(mCurrentPhotoPath));
            startActivity(intent);
        }
    }
}
