package ca.six.ui.others.trianglify.mine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

/**
 * Created by songzhw on 2016/1/21
 *
 * how to draw a fill-styled triangle
 */
public class DrawFillTriangleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyFillTriangleView(this));
    }
}

class MyFillTriangleView extends View {
    private Paint paint;
    private Path path;

    public MyFillTriangleView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(90, 730);
        path.lineTo(450, 430);
        path.lineTo(620, 970);
        path.close();
        canvas.drawPath(path, paint);
    }
}
