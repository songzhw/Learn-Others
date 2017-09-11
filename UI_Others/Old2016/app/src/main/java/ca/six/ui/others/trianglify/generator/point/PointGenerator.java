package ca.six.ui.others.trianglify.generator.point;


import java.util.Vector;

import ca.six.ui.others.trianglify.domain.Point;

/**
 * Point generator
 *
 * @author manolovn
 */
public interface PointGenerator {

    Vector<Point> generatePoints(int width, int height);

    void setBleedX(int bleedX);

    void setBleedY(int bleedY);
}
