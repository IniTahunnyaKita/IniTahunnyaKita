package com.molaja.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

/**
 * Created by tinklabs on 4/16/2015.
 */
public class PicassoBlurRequestHandler extends RequestHandler {

    private Context context;

    public PicassoBlurRequestHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandleRequest(Request data) {
        return true;
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        Bitmap b = MediaStore.Images.Media.getBitmap(context.getContentResolver(), request.uri);
        return new Result(ImageUtil.BlurBitmap(context, b, 20), Picasso.LoadedFrom.DISK);
    }
}
