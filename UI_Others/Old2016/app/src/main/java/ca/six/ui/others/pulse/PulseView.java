package ca.six.ui.others.pulse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by songzhw on 2016-05-13.
 */
public class PulseView extends View {
    private RectF bounds;
    private Bitmap bitmap;
    private Paint paint;

    public PulseView(Context context, Bitmap bitmap) {
        super(context);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        this.bounds = new RectF();
        this.bitmap = bitmap;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(0xff7b1fa2);
        paint.setColorFilter(
                new PorterDuffColorFilter(0xff7b1fa2,
                        PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float bitmapLeft = bounds.centerX() - (bitmap.getWidth() * 0.5F);
        final float bitmapTop = bounds.centerY() - (bitmap.getHeight() * 0.5F);

        for(int i = 0 ; i < 3; i++){
            canvas.save();
            float scale = 2 - 0.33f * i;
            int alpha = 75 * i;
            canvas.scale(scale, scale, bounds.centerX(), bounds.centerY() );
            paint.setAlpha(alpha);
            canvas.drawBitmap(bitmap, bitmapLeft, bitmapTop, paint);
            canvas.restore();
        }

        // draw a original bitmap that has no PorterDuffColorFilter effect
//        canvas.drawBitmap(bitmap, bitmapLeft, bitmapTop, null);
    }

    // ============  public methods   ============

    public void startPulse() {

    }

    public void finishPulse() {

    }

}
