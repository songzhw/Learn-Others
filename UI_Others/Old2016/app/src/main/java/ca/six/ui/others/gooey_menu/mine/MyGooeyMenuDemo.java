package ca.six.ui.others.gooey_menu.mine;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import ca.six.ui.others.R;
import ca.six.ui.others.utils.UiUtil;

/**
 * Created by songzhw on 2016/3/29.
 */
public class MyGooeyMenuDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGooeyMenu view = new MyGooeyMenu(this);
        setContentView(view);
    }

}

class MyGooeyMenu extends View {

    private int width, height, centerX, centerY, radiusCenter, radiusSmall;
    private Paint paint;
    private Bitmap bitmapPlus;
    private boolean isMenuOpen;
    private ValueAnimator centerRotationAnim;
    private float rotatedAngle = 0;

    public MyGooeyMenu(Context context) {
        super(context);

        radiusCenter = UiUtil.dp2px(getContext(), 40);
        radiusSmall = UiUtil.dp2px(getContext(), 26);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff363434);

        bitmapPlus = BitmapFactory.decodeResource(getResources(), R.drawable.ic_plus);

        centerRotationAnim = ValueAnimator.ofFloat(0, 135);
        centerRotationAnim.setDuration(1000);
        centerRotationAnim.setInterpolator(new AnticipateOvershootInterpolator());
        centerRotationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotatedAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    // omit for the onMeasure() with "wrap_content" right now.


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        centerX = width / 2;
        centerY = height - radiusCenter - 20;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the surrounding sub menus
        for (int i = 0; i < 3; i++) {
            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.rotate(i * 45 - 45);
            canvas.drawCircle(0, -rotatedAngle * 3, radiusSmall, paint);

            canvas.save();
            canvas.translate(0, -rotatedAngle * 3);
            canvas.drawBitmap(bitmapPlus, -bitmapPlus.getWidth() / 2, -bitmapPlus.getHeight() / 2, null);
            canvas.restore();

            canvas.restore();
        }


        // draw the main menu that is in the center
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(rotatedAngle);
        canvas.drawCircle(0, 0, radiusCenter, paint);
        canvas.drawBitmap(bitmapPlus, -bitmapPlus.getWidth() / 2, -bitmapPlus.getHeight() / 2, null);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean isHitCenter = x > (centerX - radiusCenter) && x < (centerX + radiusCenter)
                && y > (centerY - radiusCenter) && y < (centerY + radiusCenter);
        // clicking the sub menus is the same!


        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isHitCenter) {
                if (!isMenuOpen) {
                    centerRotationAnim.start();
                } else {
                    centerRotationAnim.reverse();
                }
                isMenuOpen = !isMenuOpen;
            } else {
                System.out.println("szw not hit the center circle !!!");
            }
        }
        return true;
    }

}
