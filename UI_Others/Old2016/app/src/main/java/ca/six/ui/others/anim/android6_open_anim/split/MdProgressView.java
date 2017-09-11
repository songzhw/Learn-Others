package ca.six.ui.others.anim.android6_open_anim.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by songzhw on 2016/3/13.
 */
public class MdProgressView extends View {
    private int width, height;
    private ValueAnimator anim;
    private int currentValue;
    private RectF oval;
    private Paint paint;
    private float startAngle, sweepAngle;

    public MdProgressView(Context context) {
        super(context);
        anim = ValueAnimator.ofInt(1, 2500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(10000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (int) animation.getAnimatedValue();
//                System.out.println("ProgressUpdate curr = "+currentValue);
                startAngle = currentValue % 360;
                sweepAngle = 20 + currentValue % 340;

            }
        });

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.oval = new RectF(getLeft(), getTop(),getRight(),getBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
    }


    public void startAnim(){
        anim.start();

        ValueAnimator timer = ValueAnimator.ofInt(0, 1)
                .setDuration(16);
        timer.setRepeatCount(ValueAnimator.INFINITE);
        timer.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                invalidate();
            }
        });// onAnimationEnd() will never be called since "INFINIT"
        timer.start();
    }
}
