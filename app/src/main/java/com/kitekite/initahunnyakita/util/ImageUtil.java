package com.kitekite.initahunnyakita.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class ImageUtil {

	public static Bitmap BlurBitmap (Context c, Bitmap input, int radius)
	{
		RenderScript rsScript = RenderScript.create(c);
		Allocation alloc = Allocation.createFromBitmap(rsScript, input);

		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, alloc.getElement());
		blur.setRadius(radius);
		blur.setInput(alloc);

		Bitmap result = Bitmap.createBitmap (input.getWidth(), input.getHeight(), input.getConfig());
		Allocation outAlloc = Allocation.createFromBitmap (rsScript, result);
		blur.forEach (outAlloc);
		outAlloc.copyTo (result);

		rsScript.destroy ();
		return result;
	}

    public static Bitmap drawViewToBitmap(Bitmap dest, View view, int width, int height, int downSampling, Drawable drawable) {
        float scale = 1f / downSampling;
        int heightCopy = view.getHeight();
        view.layout(0, 0, width, height);
        int bmpWidth = (int)(width * scale);
        int bmpHeight = (int)(height * scale);
        if (dest == null || dest.getWidth() != bmpWidth || dest.getHeight() != bmpHeight) {
            dest = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas c = new Canvas(dest);
        drawable.setBounds(new Rect(0, 0, width, height));
        drawable.draw(c);
        if (downSampling > 1) {
            c.scale(scale, scale);
        }
        view.draw(c);
        view.layout(0, 0, width, heightCopy);
        // saveToSdCard(original, "original.png");
        return dest;
    }

}
