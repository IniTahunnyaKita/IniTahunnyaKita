package com.kitekite.initahunnyakita.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.*;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

public class BlurImage {

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

}
