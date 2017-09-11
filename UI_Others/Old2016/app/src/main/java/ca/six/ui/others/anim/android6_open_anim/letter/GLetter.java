package ca.six.ui.others.anim.android6_open_anim.letter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import ca.six.ui.others.anim.android6_open_anim.A6Colors;

/**
 * Created by yanxing on 16/2/19.
 */
public class GLetter extends Letter {
    public final static int STROKE_WIDTH = 20;
    public final static int SHIFT = 60 - STROKE_WIDTH / 2;
    public final static int WIDTH = STROKE_WIDTH / 2 + 20;
    public final static int LEG_LENGTH = 120;
    private boolean isStart = false;
    private int mCurValue;
    private Path mPath;
    private Paint mPaint;
    // 圆
    private RectF mRectF;
    private int mStartAngle;
    private int mSweepAngle;
    // g的下半圆
    private RectF mRectFHalf;
    private int mSweepAngleHalf;
    private int mMoveX;
    private int mMoveY;
    private int mFv;
    // 判断是否进入了底部弧线绘制
    private boolean isInRoundDraw = false;

    public GLetter(int x, int y) {
        super(x, y);
        mPath = new Path();
        mMoveX =  curX + SHIFT;
        mMoveY = curY - SHIFT;
        mPath.moveTo(mMoveX, mMoveY);
        mFv = duration * 2 / 3;
        mRectFHalf = new RectF();
        mRectFHalf.set(curX - SHIFT, curY - SHIFT + LEG_LENGTH - SHIFT, curX + SHIFT, curY + SHIFT + LEG_LENGTH - SHIFT);
        mSweepAngleHalf = 0;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(A6Colors.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mRectF = new RectF();
        mRectF.set(curX - SHIFT, curY - SHIFT, curX + SHIFT, curY + SHIFT);
        mStartAngle = 0;
        mSweepAngle = 0;
    }

    @Override
    public void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, duration);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isStart) {
                    return;
                }
                // 完整圆的路径
                mCurValue = (int) animation.getAnimatedValue();
                mSweepAngle = mCurValue * 360 / duration;
                // 计算g除了圆的J勾路线
                if (mCurValue < mFv) {
                    mMoveY = curY - SHIFT + LEG_LENGTH * mCurValue / mFv;
                    mPath.lineTo(mMoveX, mMoveY);
                } else {
                    if (!isInRoundDraw) {
                        isInRoundDraw = true;
                        mMoveY = curY - SHIFT + LEG_LENGTH;
                        mPath.lineTo(mMoveX, mMoveY);
                    }
                    mCurValue -= mFv;
                    mSweepAngleHalf = mCurValue * 180 / (duration - mFv);
                    mPath.addArc(mRectFHalf, mStartAngle, mSweepAngleHalf);
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isStart = true;
            }
        });
        animator.start();
    }

    @Override
    public void drawSelf(Canvas canvas) {
        // 勾子
        canvas.drawPath(mPath, mPaint);
        // 圆
        canvas.drawArc(mRectF, mStartAngle, mSweepAngle, false, mPaint);
    }
}
