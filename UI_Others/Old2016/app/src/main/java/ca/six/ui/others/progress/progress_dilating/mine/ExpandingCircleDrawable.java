package ca.six.ui.others.progress.progress_dilating.mine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by songzhw on 2016/2/13.
 */
public class ExpandingCircleDrawable extends Drawable {

    private Paint paint;
    private float radius;

    public ExpandingCircleDrawable(float radius, float maxRadius) {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(Color.BLUE);
        this.radius = radius;
    }

    // ====================== Abstract Method ================================
    @Override
    public void draw(Canvas canvas) {
        Rect bounds = this.getBounds();
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius, paint);
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

    // ======================================================

    // for the ValueAnimator.ofFloat(..., "radius", ...);
    public void setRadius(float radius) {
        this.radius = radius;
        this.invalidateSelf();
    }

}
