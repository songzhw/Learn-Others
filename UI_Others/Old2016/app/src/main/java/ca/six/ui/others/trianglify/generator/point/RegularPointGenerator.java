package ca.six.ui.others.trianglify.generator.point;


import java.util.Random;
import java.util.Vector;

import ca.six.ui.others.trianglify.domain.Point;

/**
 * Regular point generator
 *
 * @author manolovn
 */
public class RegularPointGenerator implements PointGenerator {

    private final Random random = new Random();
    private final int cellSize;
    private final int variance;

    private int bleedX = 0;
    private int bleedY = 0;

    public RegularPointGenerator(int cellSize, int variance) {
        this.cellSize = cellSize;
        this.variance = variance;
    }

    @Override
    public void setBleedX(int bleedX) {
        this.bleedX = bleedX;
    }

    @Override
    public void setBleedY(int bleedY) {
        this.bleedY = bleedY;
    }

    @Override
    public Vector<Point> generatePoints(int width, int height) {
        Vector<Point> points = new Vector<>();

        for (int j = -bleedY; j < height + bleedY; j += cellSize) {
            for (int i = -bleedX; i < width + bleedX; i += cellSize) {
                int x = i + random.nextInt(variance);
                int y = j + random.nextInt(variance);
                points.add(new Point(x, y));
                System.out.println("one, two = "+j+" ; "+i);
            }
        }

        System.out.println("szw PointGen "+points.size());
        return points;
    }
}
