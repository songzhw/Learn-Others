package ca.six.ui.others.progress.progress_dilating;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/*
Drawable基本只能作为ImageView的src， 或是View的background来显示的。
Drawable的核心方法是 draw()。   View的工作原理就是调用Drawable的draw()方法来绘制View的背景。
所以我们要自定义Drawable，一定要重写Drawable的draw()方法，

重写Drawable， 要重写draw(), setAlpha(), setColorFilter(), getOpacity()这几个方法，
后三者的编码可以参考ShapeDrawable，BitmapDrawable的源码。

*/
public class DilatingDotDrawable extends Drawable {
    private float radius, maxRadius;BitmapDrawable bd;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Rect dirtyBounds = new Rect(0, 0, 0, 0);

    public DilatingDotDrawable(final int color, final float radius, final float maxRadius) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        this.radius = radius;
        this.maxRadius = maxRadius;

        dirtyBounds.bottom = (int) (maxRadius * 2) + 2; // dialect
        dirtyBounds.right = (int) (maxRadius * 2) + 2;
    }

    // ======================================================
    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = this.getBounds();
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius - 1, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != paint.getAlpha()) {
            paint.setAlpha(alpha);
            this.invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    public int getIntrinsicWidth() {
        return (int) (maxRadius * 2) + 2;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (maxRadius * 2) + 2;
    }


    @Override
    public Rect getDirtyBounds() {
        return dirtyBounds;
    }

    @Override
    protected void onBoundsChange(final Rect bounds) {
        super.onBoundsChange(bounds);
        dirtyBounds.offsetTo(bounds.left, bounds.top); //Offset the rectangle to a specific (left, top) position, keeping its width and height the same.
    }

     // ======================================================

    public void setColor(int color) {
        paint.setColor(color);
        this.invalidateSelf();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.invalidateSelf();
    }

    public float getRadius() {
        return radius;
    }

}
