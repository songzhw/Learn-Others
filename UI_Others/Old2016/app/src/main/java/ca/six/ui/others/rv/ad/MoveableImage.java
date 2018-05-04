package ca.six.ui.others.rv.ad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MoveableImage extends AppCompatImageView {
    public MoveableImage(Context context) {
        super(context);
        init(context, null);
    }

    public MoveableImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true; //本View就是要被drag的
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    };
    private ViewDragHelper dragger;

    private void init(Context context, @Nullable AttributeSet attrs) {
        ViewGroup parent = (ViewGroup) this.getParent();
        System.out.println("szw : parent = "+this.getParent()); //=> null
        dragger = ViewDragHelper.create(parent, dragCallback);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragger.processTouchEvent(event);
        return true;
    }
}
