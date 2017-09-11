package ca.six.ui.others.m_intro_view.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import ca.six.ui.others.m_intro_view.target.Target;


/**
 * Created by mertsimsek on 25/01/16.
 */
public class Circle {
    public static int DEFAULT_TARGET_PADDING = 10;

    private Target target;

    private Focus focus;

    private FocusGravity focusGravity;

    private int radius;

    private Point circlePoint;

    private int padding;

    public Circle(Target target) {
        this(target, Focus.MINIMUM);
    }

    public Circle(Target target,Focus focus) {
        this(target, focus, FocusGravity.CENTER, DEFAULT_TARGET_PADDING);
    }

    public Circle(Target target, Focus focus, FocusGravity focusGravity, int padding) {
        this.target = target;
        this.focus = focus;
        this.focusGravity = focusGravity;
        this.padding = padding;
        circlePoint = getFocusPoint();
        calculateRadius(padding);
    }

    public void draw(Canvas canvas, Paint eraser, int padding){
        calculateRadius(padding);
        circlePoint = getFocusPoint();
        canvas.drawCircle(circlePoint.x, circlePoint.y, radius, eraser);
    }

    private Point getFocusPoint(){
        if(focusGravity == FocusGravity.LEFT){
            int xLeft = target.getRect().left + (target.getPoint().x - target.getRect().left) / 2;
            return new Point(xLeft, target.getPoint().y);
        }
        else if(focusGravity == FocusGravity.RIGHT){
            int xRight = target.getPoint().x + (target.getRect().right - target.getPoint().x) / 2;
            return new Point(xRight, target.getPoint().y);
        }
        else
            return target.getPoint();
    }

    public void reCalculateAll(){
        calculateRadius(padding);
        circlePoint = getFocusPoint();
    }

    private void calculateRadius(int padding){
        int side;

        if(focus == Focus.MINIMUM)
            side = Math.min(target.getRect().width() / 2, target.getRect().height() / 2);
        else if(focus == Focus.ALL)
            side = Math.max(target.getRect().width() / 2, target.getRect().height() / 2);
        else{
            int minSide = Math.min(target.getRect().width() / 2, target.getRect().height() / 2);
            int maxSide = Math.max(target.getRect().width() / 2, target.getRect().height() / 2);
            side = (minSide + maxSide) / 2;
        }

        radius = side + padding;
    }

    public int getRadius(){
        return radius;
    }

    public Point getPoint(){
        return circlePoint;
    }

}
