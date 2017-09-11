package ca.six.ui.others.progress.progresssmog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import ca.six.ui.others.R;


/**
 * This code is copyright (c) 2015 juhuiwan.cn
 * Created by zerob13 on 10/29/15.
 *
 * see : https://github.com/jhw-dev/CircleLoadingView
 */

/*
  cl_circleRadius [dimension]         --> 中间那个圆的半径
  cl_circleStrokeSize [dimension]     --> 圆的边框大小
  cl_fillAnimationDuration [integer]  --> 最后扩散动画的时间
  cl_src [dimension]                  --> 背景的 BitmapDrawable 资源
 */
public class CircleLoadingView extends View {
    private Bitmap originalBitmap, resultBitmap;
    private Paint grayPaint, arcPaint, ringPaint, circlePaint, normalPaint;
    private RectF rect;

    private int ringRadius, circleRadius, circleStrokeSize, percentage, animationState;
    private float circleRadiusMax, circleStep, centerX, centerY, animationDuration;

    public static final int STATE_RUNNING = 0;
    public static final int STATE_EXPAND = 1;
    public static final int STATE_FINISHED = 2;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        animationState = STATE_FINISHED;

        ColorMatrix cm = new ColorMatrix();
        cm.setScale(0.382f, 0.382f, 0.382f, 1f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaint.setColorFilter(f);

        arcPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        ringPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        circlePaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        normalPaint = new Paint((Paint.ANTI_ALIAS_FLAG));

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        try {
            Drawable drawable = typedArray .getDrawable(R.styleable.CircleLoadingView_cl_src);
            circleRadius = typedArray .getDimensionPixelSize(R.styleable.CircleLoadingView_cl_circleRadius, -1);
            animationDuration = typedArray .getFloat(R.styleable.CircleLoadingView_cl_fillAnimationDuration, 800);
            circleStrokeSize = typedArray .getDimensionPixelSize(R.styleable.CircleLoadingView_cl_circleStrokeSize, -1);
            if (drawable instanceof BitmapDrawable) {
                originalBitmap = ((BitmapDrawable) drawable).getBitmap();
                initRect();
            }
        } finally {
            typedArray.recycle();
        }
    }

    public void setImageBitmap(Bitmap bm) {
        this.originalBitmap = bm;
        initRect();
    }

    private void initRect() {
        int w = this.getMeasuredWidth();
        int h = this.getMeasuredHeight();
        if (w == 0 || h == 0) {
            return;
        }
        if (w > 0 && h > 0) {

            if (circleRadius < 0) {
                circleRadius = ringRadius = w / 4;
            } else {
                ringRadius = circleRadius;
            }
            centerX = w / 2f;
            centerY = h / 2f;
            resultBitmap = Bitmap.createScaledBitmap(originalBitmap, w, h, true);
            rect = new RectF((w / 2 - circleRadius), (h / 2 - circleRadius),
                    w / 2 + circleRadius,
                    h / 2 + circleRadius);
            ringPaint.setStyle(Paint.Style.STROKE);
            if (circleStrokeSize < 0) {
                circleStrokeSize = w / 36;
            }
            ringPaint.setStrokeWidth(circleStrokeSize);
            circleRadiusMax = (float) Math.sqrt(w * w + h * h) / 2f;
            circleStep = (circleRadiusMax - circleRadius) / (animationDuration / 25);
            Matrix m = new Matrix();
            RectF src = new RectF(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
            RectF dst = new RectF(0, 0, this.getWidth(), this.getHeight());
            m.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            Shader shader = new BitmapShader(originalBitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            shader.setLocalMatrix(m);
            arcPaint.setShader(shader);
            ringPaint.setShader(shader);
            circlePaint.setShader(shader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (originalBitmap == null) {
            return;
        }
        int percent = percentage;
        if (percent == 0) {
            int saveCount = canvas.save();
            canvas.drawBitmap(resultBitmap, 0, 0, grayPaint);
            canvas.restoreToCount(saveCount);
        } else if (percent < 100) {
            int saveCount = canvas.save();
            canvas.drawBitmap(resultBitmap, 0, 0, grayPaint);
            canvas.drawCircle(centerX, centerY, ringRadius, ringPaint);
            canvas.drawArc(rect, -90, (float) ((percent / 100.0) * 360.0), true, arcPaint);
            if (animationState == STATE_FINISHED) {
                animationState = STATE_RUNNING;
            }
            canvas.restoreToCount(saveCount);
        } else if (percent == 100 && animationState == STATE_RUNNING) {
            int saveCount = canvas.save();
            canvas.drawBitmap(resultBitmap, 0, 0, grayPaint);
            canvas.drawCircle(centerX, centerY, circleRadius, circlePaint);
            circleRadius += circleStep;
            animationState = STATE_EXPAND;
            postInvalidateDelayed(100);
            canvas.restoreToCount(saveCount);
        } else if (animationState == STATE_EXPAND) {
            int saveCount = canvas.save();
            canvas.drawBitmap(resultBitmap, 0, 0, grayPaint); //仍是灰色背景
            canvas.drawCircle(centerX, centerY, circleRadius, circlePaint); // 画透明效果
            circleRadius += circleStep; // 透明圆的半径越来越大
            if (circleRadius >= circleRadiusMax) { // 超过图片的大小后，就停止了，不再invalidate()了
                animationState = STATE_FINISHED;
                circleRadius = ringRadius;
            } else {
                postInvalidateDelayed(25); // 否则就每隔一段时间就invalidate(). 不要马上刷新，不会CPU和内存吃不住
            }
            canvas.restoreToCount(saveCount);

        } else {
            int saveCount = canvas.save();
            canvas.drawBitmap(resultBitmap, 0, 0, normalPaint);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            if (resultBitmap != null) {
                resultBitmap = null;
            }
            if (originalBitmap != null) {
                initRect();
            }
        }
    }

    public void setPercent(int percent) {
        this.percentage = percent;
        postInvalidate();
    }
}

