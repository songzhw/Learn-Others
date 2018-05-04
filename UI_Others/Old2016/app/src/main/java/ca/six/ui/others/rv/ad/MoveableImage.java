package ca.six.ui.others.rv.ad;

import android.content.Context;
import android.graphics.Canvas;
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

    int downX,downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch(action){
            case ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case ACTION_MOVE:

                break;
            case ACTION_UP:

                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if( Math.abs(upX - downX) < 10 &&
                        Math.abs(upY - downY) < 10) {
                    performClick();
                }

                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

