package ca.six.ui.others.rv.ad.iv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class MoveableImage extends AppCompatImageView {
    int downX, downY, dy, height;
    private Drawable drawable;


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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.height = h;
    }

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
                System.out.println("szw dy = " + dy + " ; y = " + y + " ; downY = " + downY + " ; h1 = " + height + " ; h2 = " + getDrawable().getBounds().height());

                int heightDiff = getDrawable().getBounds().height() - height;
                if (dy > heightDiff) {
                    dy = heightDiff;
                }
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
        if (drawable == null) {
            drawable = getDrawable();
            int w = getWidth();
            int h = (int) (w * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
            drawable.setBounds(0, 0, w, h);
        }

        canvas.save();
        canvas.translate(0, -dy);
        super.onDraw(canvas);
        canvas.restore(); // 经实测, 不save/restore也行. 因为此句之后没有与原样相关的canvas操作嘛!
    }
}

/*
1. 没有记录当前up位置. 结果每次action_down, 结果都强制回到了最顶头
 */
