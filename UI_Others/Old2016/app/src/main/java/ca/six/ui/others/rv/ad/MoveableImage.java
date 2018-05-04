package ca.six.ui.others.rv.ad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class MoveableImage extends AppCompatImageView {
    public MoveableImage(Context context) {
        super(context);
        init(context, null);
    }

    public MoveableImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    int downX, downY, dy;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case ACTION_MOVE:
                int y = (int) event.getY();
                dy = y - downY; //手指往上时, y越来越小;
                System.out.println("szw dy = " + dy + " ; y = " + y + " ; downY = " + downY);
                if (dy >= 0) {
                    invalidate();
                }
                break;
            case ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 &&
                        Math.abs(upY - downY) < 10) {
                    performClick();
                }

                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        int w = getWidth();
        int h = (int) (w * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, w, h);

        canvas.save();
        canvas.translate(0, -dy);
        super.onDraw(canvas);
        canvas.restore();
    }
}

