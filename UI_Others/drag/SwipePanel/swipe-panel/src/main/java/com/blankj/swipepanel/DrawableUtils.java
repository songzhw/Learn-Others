package com.blankj.swipepanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;

public class DrawableUtils {
  private static final Object sLock = new Object();

  private static TypedValue sTempValue;

  public static Bitmap drawable2Bitmap(final Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
      if (bitmapDrawable.getBitmap() != null) {
        return bitmapDrawable.getBitmap();
      }
    }
    Bitmap bitmap;
    if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
      bitmap = Bitmap.createBitmap(1, 1,
          drawable.getOpacity() != PixelFormat.OPAQUE
              ? Bitmap.Config.ARGB_8888
              : Bitmap.Config.RGB_565);
    } else {
      bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
          drawable.getIntrinsicHeight(),
          drawable.getOpacity() != PixelFormat.OPAQUE
              ? Bitmap.Config.ARGB_8888
              : Bitmap.Config.RGB_565);
    }
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
    if (Build.VERSION.SDK_INT >= 21) {
      return context.getDrawable(id);
    } else if (Build.VERSION.SDK_INT >= 16) {
      return context.getResources().getDrawable(id);
    } else {
      final int resolvedId;
      synchronized (sLock) {
        if (sTempValue == null) {
          sTempValue = new TypedValue();
        }
        context.getResources().getValue(id, sTempValue, true);
        resolvedId = sTempValue.resourceId;
      }
      return context.getResources().getDrawable(resolvedId);
    }
  }
}
