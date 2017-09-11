package ca.six.ui.others.anim.android6_open_anim.letter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

import ca.six.ui.others.anim.android6_open_anim.A6Colors;


/**
 * 字母i的绘制以及动画
 * Created by yanxing on 16/2/18.
 */
public class ILetter extends Letter {
    public final static int STROKE_WIDTH = 20;
    public final static int WIDTH = STROKE_WIDTH;
    public final static int LENGTH = 120;
    private int currValue;
    private boolean isStart = false;
    private Paint paint;
    // 竖线弹出的时间
    private int duration1 = duration /3*2;
    // 圆球弹出的时间
    private int duration2 = duration /3;
    // 竖线变化的长度
    private float lenLine;
    // 圆球弹出的变化高度
    private float lenCircle;
    // 圆球的半径
    private int radius;

    public ILetter(int x, int y) {
        super(x, y);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(A6Colors.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
        curY += LENGTH / 2;
    }

    @Override
    public void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, duration1 + duration2);
        animator.setDuration(duration1 + duration2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isStart) {
                    return;
                }
                currValue = (int) animation.getAnimatedValue();
                if (currValue <= duration1) {
                    lenLine = LENGTH * currValue / duration1;
                } else {
                    currValue -= duration1;
                    radius = 12 * currValue / 500;
                    lenCircle = 30 * currValue / 500;
                }

            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isStart = true;
                radius = 0;
            }
        });
        animator.start();
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (isStart) {
            // 绘制竖线
            canvas.drawLine(curX, curY, curX, curY - lenLine, paint);
            // 绘制圆点
            canvas.drawCircle(curX, curY - lenLine + 30 - lenCircle - 20, radius, paint);
        }
    }
}
