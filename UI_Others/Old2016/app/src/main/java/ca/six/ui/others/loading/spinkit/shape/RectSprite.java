package ca.six.ui.others.loading.spinkit.shape;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class RectSprite extends ShapeSprite {
    @Override
    public ValueAnimator getAnimation() {
        return null;
    }

    @Override
    public void drawShape(Canvas canvas, Paint paint) {
        if (getDrawBounds() != null) {
            Rect rect = getDrawBounds();
            rect = new Rect(
                    rect.left + rect.width() / 3,
                    rect.top + rect.height() / 3,
                    rect.right,
                    rect.bottom
            );
            canvas.drawRect(rect, paint);
        }
    }
}