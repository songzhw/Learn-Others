package ca.six.ui.others.trianglify.triangulator;


import java.util.Vector;

import ca.six.ui.others.trianglify.domain.Point;
import ca.six.ui.others.trianglify.domain.Triangle;

/**
 * Triangulator behavior
 *
 * @author manolovn
 */
public interface Triangulator {

    Vector<Triangle> triangulate(Vector<Point> pointSet);
}
