package ca.six.ui.others.rv.ad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CoordinateImageView extends AppCompatImageView {
    private int dy; // dy: the translateY in canvas.translate()
    private int height;

    public CoordinateImageView(Context context) {
        super(context);
    }

    public CoordinateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, -dy);
        super.onDraw(canvas);
        canvas.restore();
    }

    public void setDiff(int diff) {
        dy = diff;

        // viePicDiff为1645. 当diff正好是1645时, 正好到达图片最下方. 再上拉(diff更大), 就有空白了. 所以要加这个判断
        int viewPicDiff = getDrawable().getBounds().height() - height;
        if(dy > viewPicDiff){
            dy = viewPicDiff;
        }

        invalidate();
    }
}
