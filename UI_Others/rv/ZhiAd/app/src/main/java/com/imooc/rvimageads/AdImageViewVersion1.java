package com.imooc.rvimageads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

// 老版本, drawableToBitmap很耗内存的

public class AdImageViewVersion1 extends AppCompatImageView {
    private RectF rectf;
    private Bitmap bitmap;
    private int minDy;

    public AdImageViewVersion1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        minDy = h;
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        bitmap = drawableToBitamp(drawable);
        rectf = new RectF(0, 0,
                w,
                bitmap.getHeight() * w / bitmap.getWidth());

    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private int mDy;

    public void setDy(int dy) {

        if (getDrawable() == null) {
            return;
        }
        mDy = dy - minDy;
        if (mDy <= 0) {
            mDy = 0;
        }
        if (mDy > rectf.height() - minDy) {
            mDy = (int) (rectf.height() - minDy);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null) {
            return;
        }
        canvas.save();
        canvas.translate(0, -mDy);
        canvas.drawBitmap(bitmap, null, rectf, null);
        canvas.restore();
    }


}
