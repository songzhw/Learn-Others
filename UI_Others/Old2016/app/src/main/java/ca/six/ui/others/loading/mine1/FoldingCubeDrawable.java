package ca.six.ui.others.loading.mine1;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/**
 * Created by songzhw on 2016/4/5.
 */
public class FoldingCubeDrawable extends Drawable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Cube[] children = new Cube[4];
    private Rect rect;

    public FoldingCubeDrawable() {
        paint.setColor(Color.DKGRAY);
    }

    // override from Drawable
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        rect = getBounds();
        for (int i = 0; i < 4; i++) {
            Cube cube = new Cube(i);
            cube.bounds = new Rect(rect.left + rect.width()/6, rect.top+rect.height()/6,
                    rect.left + rect.width()/2 - 3, rect.top + rect.height()/2 - 3);
            cube.pivotX = cube.bounds.centerX();
            cube.pivotY = cube.bounds.centerY();

            PropertyValuesHolder alpha = PropertyValuesHolder.ofInt("alpha", 0, 255);
            PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotateX", -180, 0, 0);
            PropertyValuesHolder rotateY = PropertyValuesHolder.ofFloat("rotateY", 0,  0, 180);
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(cube, alpha, rotateX, rotateY)
                    .setDuration(2400);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setStartDelay( 600 * i);
            anim.start();

            children[i] = cube;
        }

    }

    // override from Drawable
    @Override
    public void setAlpha(int alpha) {
        if (alpha != paint.getAlpha()) {
            paint.setAlpha(alpha);
            this.invalidateSelf();
        }
    }

    // override from Drawable
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    // override from Drawable
    @Override
    public int getOpacity() {
        return PixelFormat.RGBA_8888;
    }


    // override from Drawable
    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            Cube cube = children[i];
            canvas.save();
            canvas.rotate(45 + i * 90, rect.centerX(), rect.centerY());
            cube.draw(canvas);
            canvas.restore();
        }
    }

    class Cube {
        public Rect bounds;
        public ValueAnimator anim;
        public int index;
        public Paint paintCube;

        public float rotateX = 0 , rotateY = 0, pivotX, pivotY;
        public Camera camera;
        public Matrix matrix;

        public Cube(int i){
            index = i;
            paintCube = new Paint(Paint.ANTI_ALIAS_FLAG);
            camera = new Camera();
            matrix = new Matrix();
        }

        public void draw(Canvas canvas) {
            if(rotateX != 0 || rotateY != 0){
                camera.save();
                camera.rotateX(rotateX);
                camera.rotateY(rotateY);
                camera.getMatrix(matrix);
                matrix.preTranslate(-pivotX, -pivotY);
                matrix.postTranslate(pivotX, pivotY);
                camera.restore();
                canvas.concat(matrix);
            }

            canvas.drawRect(bounds, paintCube);
        }

        private int alpha;
        public int getAlpha() {
            return alpha;
        }
        public void setAlpha(int alpha) {
            this.alpha = alpha;
            paintCube.setAlpha(alpha);
            invalidateSelf();   // Drawable's function
        }

        public float getRotateX() {
            return rotateX;
        }
        public void setRotateX(float rotateX) {
            this.rotateX = rotateX;
            invalidateSelf();
        }

        public float getRotateY() {
            return rotateY;
        }

        public void setRotateY(float rotateY) {
            this.rotateY = rotateY;
            invalidateSelf();
        }
    }


}
