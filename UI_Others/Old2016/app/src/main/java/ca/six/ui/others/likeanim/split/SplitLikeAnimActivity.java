package ca.six.ui.others.likeanim.split;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import ca.six.ui.others.utils.UiUtil;


/**
 * Created by songzhw on 2015/12/29
 */
public class SplitLikeAnimActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplitMyCirleView view = new SplitMyCirleView(this);
        int size = UiUtil.dp2px(this, 200);
        setContentView(view, new FrameLayout.LayoutParams(size, size));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.start();
            }
        });
    }

}


class SplitMyCirleView extends View {
    private static final int START_COLOR = 0xFFFF5722;
    private static final int END_COLOR = 0xFFFFC107;

    private static final int DOTS_COUNT = 7;
    private static final int OUTER_DOTS_POSITION_ANGLE = 51; // 51 * 7 = 357, 约等于360，正好一周了


    private Paint fillPaint,  clearPaint, dotPaint;
    private float x, y, radius, innerRadius, outProgress, innerProgress;
    private Bitmap tmpBitmap;
    private Canvas tmpCanvas;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    // 0 [circle expand] ; 1 [firework] ;
    private int stage = 0;

    public SplitMyCirleView(Context context) {
        super(context);
        init();
    }

    private void init(){
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        dotPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w,h) / 3;
        innerRadius = (float) (0.8 * radius);
        x = w / 2;
        y = h / 2;

        tmpBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        tmpCanvas = new Canvas(tmpBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(stage == 0){

            tmpCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR); // clear the canvas

            fillPaint.setColor( (Integer) argbEvaluator.evaluate(outProgress, START_COLOR, END_COLOR));
            tmpCanvas.drawCircle(x, y , radius * outProgress , fillPaint );

            tmpCanvas.drawCircle(x, y , radius * innerProgress, clearPaint);

            canvas.drawBitmap(tmpBitmap, 0, 0, null);

        }
        else if(stage == 1){
            tmpBitmap.recycle();
            for (int i = 0; i < DOTS_COUNT; i++) {
                int cX1 = (int) (x + radius * Math.cos(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
                int cY1 = (int) (y + radius * Math.sin(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
                canvas.drawCircle(cX1, cY1, 20, dotPaint);

                int cX2 = (int) (x + radius * 0.7 * Math.cos((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180));
                int cY2 = (int) (y + radius * 0.7 * Math.sin((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180));
                canvas.drawCircle(cX2, cY2, 12, dotPaint);

            }
        }
    }

    public void start(){
        outProgress = 0;
        innerProgress = 0;
        stage = 0;

        // For skilled eye there is one more thing - our outer circle changes its color based on current progress.
        // It’s done by ArgbEvaluator class which transforms two colors based on given fraction



        handler.sendEmptyMessageDelayed(11, 50);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            // 0 : circle expand
            if(stage == 0){
                if(outProgress <= 1) {
                    outProgress += 0.1;
                    if (outProgress >= 0.5) {
                        innerProgress += 0.15;
                    }
                    handler.sendEmptyMessageDelayed(11, 50);
                    invalidate();
                } else {
                    stage = 1;
                    handler.sendEmptyMessageDelayed(12, 600);
                }
            }

            // 1 : fireworks
            else if(stage == 1) {
                invalidate();
            }


        }
    };

}