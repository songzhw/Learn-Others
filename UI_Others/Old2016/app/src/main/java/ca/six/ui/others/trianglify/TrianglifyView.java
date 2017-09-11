package ca.six.ui.others.trianglify;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

import ca.six.ui.others.R;
import ca.six.ui.others.trianglify.domain.Point;
import ca.six.ui.others.trianglify.domain.Triangle;
import ca.six.ui.others.trianglify.generator.color.BrewerColorGenerator;
import ca.six.ui.others.trianglify.generator.point.PointGenerator;
import ca.six.ui.others.trianglify.generator.point.RegularPointGenerator;
import ca.six.ui.others.trianglify.renderer.TriangleRenderer;
import ca.six.ui.others.trianglify.triangulator.DelaunayTriangulator;
import ca.six.ui.others.trianglify.triangulator.Triangulator;


/**
 * Trianglify view
 *
 * @author manolovn
 */
public class TrianglifyView extends View {

    private PointGenerator pointGenerator;
    private Triangulator triangulator;
    private TriangleRenderer triangleRenderer;

    private int width;
    private int height;

    private int cellSize;
    private int variance;
    private int bleedX;
    private int bleedY;

    private Vector<Point> points;
    private Vector<Triangle> triangles;

    public TrianglifyView(Context context) {
        super(context);
        init(null);
    }

    public TrianglifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TrianglifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            parseAttributes(attrs);
        }

        pointGenerator = new RegularPointGenerator(cellSize, variance);
        triangulator = new DelaunayTriangulator();
        triangleRenderer = new TriangleRenderer();
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.TrianglifyView, 0, 0);

        try {
            cellSize = a.getInteger(R.styleable.TrianglifyView_cellSize, TriDefault.cellSize);
            variance = a.getInteger(R.styleable.TrianglifyView_variance, TriDefault.variance);
            bleedX = a.getInteger(R.styleable.TrianglifyView_bleedX, TriDefault.bleedX);
            bleedY = a.getInteger(R.styleable.TrianglifyView_bleedY, TriDefault.bleedY);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pointGenerator.setBleedX(bleedX);
        pointGenerator.setBleedY(bleedY);
        points = pointGenerator.generatePoints(width, height);
        triangles = triangulator.triangulate(points);

        triangleRenderer.render(triangles, canvas);
    }

    public void setVariance(int variance) {
        this.variance = variance;
        pointGenerator = new RegularPointGenerator(cellSize, variance);
        invalidate();
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
        pointGenerator = new RegularPointGenerator(cellSize, variance);
        invalidate();
    }

    public void setColor(ColorBrewer color) {
        triangleRenderer = new TriangleRenderer(new BrewerColorGenerator(color));
        invalidate();
    }

    public void clear(){
        points.clear();
        triangles.clear();
//        System.gc();
    }
}


