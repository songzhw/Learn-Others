package ca.six.ui.others.progress.progresssmog.split;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ca.six.ui.others.R;

/**
 * Created by songzhw on 2015/12/23
 */
public class SmogSplitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        ImageView iv1 = new ImageView(this); iv1.setImageResource(R.drawable.forest1);
        layout.addView(iv1, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        GrayMaskView grayView = new GrayMaskView(this);
        layout.addView(grayView, new LayoutParams(350, 350));

        OpenMaskView openView = new OpenMaskView(this);
        layout.addView(openView, new LayoutParams(350, 350));

        ExpandMaskView expandView = new ExpandMaskView(this);
        layout.addView(expandView, new LayoutParams(350, 350));

        setContentView(layout);
    }
}

class GrayMaskView extends View {
    protected Bitmap bitmap;
    protected Paint grayPaint;

    public GrayMaskView(Context context) {
        super(context);
        Drawable drawable = context.getResources().getDrawable(R.drawable.forest1);
        bitmap = ((BitmapDrawable)drawable).getBitmap();

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(0.382f, 0.382f, 0.382f, 1f);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaint.setColorFilter(colorFilter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0 , grayPaint);
    }
}

class OpenMaskView extends GrayMaskView {
    protected Paint ringPaint, arcPaint;
    protected float centerX, centerY, radius;
    protected RectF rect;
    protected Shader shader;

    public OpenMaskView(Context context) {
        super(context);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w != oldw || h != oldh){
            int w2 = this.getMeasuredWidth();
            int h2 = this.getMeasuredHeight();
            centerX = w2 / 2f;
            centerY = h2 / 2f;
            radius = centerX /2f;
            rect = new RectF((w2 / 2 - radius), (h2 / 2 - radius),
                    w2 / 2 + radius,
                    h2 / 2 + radius);

//            Matrix m = new Matrix();
//            RectF src = new RectF(0, 0, w2, h2);
//            RectF dst = new RectF(0, 0, w2, h2);
//            m.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            shader.setLocalMatrix(m);

            ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ringPaint.setStyle(Paint.Style.STROKE);
            ringPaint.setStrokeWidth(10);
            ringPaint.setShader(shader);

            arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            arcPaint.setShader(shader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.drawBitmap(bitmap, 0, 0 , grayPaint);
        canvas.drawCircle(centerX, centerY, radius, ringPaint);
        canvas.drawArc(rect, -90, 45, true, arcPaint);
        canvas.restoreToCount(saveCount);

    }
}

class ExpandMaskView extends OpenMaskView {
    private Paint circlePaint;

    public ExpandMaskView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setShader(shader); // it's not stroke style !
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0 , grayPaint);
        canvas.drawCircle(centerX, centerY, radius + 20, circlePaint);
    }
}