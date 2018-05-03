package com.imooc.rvimageads;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by zhanghongyang01 on 17/11/23.
 */

public class AdImageView extends AppCompatImageView {
    public AdImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private int dy;
    private int minDy;

    public void setDy(int dy) {
        if (getDrawable() == null) {
            return;
        }
        this.dy = dy - minDy;
        if (this.dy <= 0) {
            this.dy = 0;
        }
        if (this.dy > getDrawable().getBounds().height() - minDy) {
            this.dy = getDrawable().getBounds().height() - minDy;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        minDy = h;
    }

    public int getDy() {
        return dy;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        int w = getWidth();
        int h = (int) (getWidth() * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, w, h);
        canvas.save();
        canvas.translate(0, -getDy());
        super.onDraw(canvas);
        canvas.restore();
    }
}
