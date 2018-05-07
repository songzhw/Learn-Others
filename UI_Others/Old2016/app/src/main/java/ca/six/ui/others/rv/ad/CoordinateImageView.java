package ca.six.ui.others.rv.ad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CoordinateImageView extends AppCompatImageView {
    private int dy; // dy: the translateY in canvas.translate()
    private int minDy, maxDy;

    public CoordinateImageView(Context context) {
        super(context);
    }

    public CoordinateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO probabaly need the save/restore
        canvas.translate(0, -dy);
        super.onDraw(canvas);
    }

    public void setDiff(int diff) {
        dy = diff;
        invalidate();
    }
}
