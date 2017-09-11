package ca.six.ui.others.trianglify.triangulator;


import java.util.Collection;
import java.util.Vector;

import ca.six.ui.others.trianglify.domain.Edge;
import ca.six.ui.others.trianglify.domain.Point;
import ca.six.ui.others.trianglify.domain.Triangle;
import ca.six.ui.others.trianglify.util.Preconditions;

/**
 * Delaunay triangulator
 *
 * @author manolovn
 */
public class DelaunayTriangulator implements Triangulator {

    private Triangulation triangulation;

    public DelaunayTriangulator() {

    }

    @Override
    public Vector<Triangle> triangulate(Vector<Point> pointSet) {
        System.out.println("szw trianglator 01 point: "+pointSet.size());
        Preconditions.checkNotNull(pointSet);
        Preconditions.checkArgument(pointSet.size() >= 3, "Can't triangulate less than 3 points");
        triangulation = new Triangulation();

        Triangle superTriangle = generateSuperTriangle(pointSet);
        triangulation.add(superTriangle);

        for (int i = 0; i < pointSet.size(); i++) {
            Triangle triangle = triangulation.findContainingTriangle(pointSet.get(i));

            if (triangle == null) {
                Edge edge = triangulation.findNearestEdge(pointSet.get(i));

                Triangle first = triangulation.findOneTriangleSharing(edge);
                Triangle second = triangulation.findNeighbour(first, edge);

                if (first == null || second == null) {
                    continue;
                }

                Point firstNoneEdgeVertex = first.getNoneEdgeVertex(edge);
                Point secondNoneEdgeVertex = second.getNoneEdgeVertex(edge);

                triangulation.remove(first);
                triangulation.remove(second);

                Triangle triangle1 = new Triangle(edge.a, firstNoneEdgeVertex, pointSet.get(i));
                Triangle triangle2 = new Triangle(edge.b, firstNoneEdgeVertex, pointSet.get(i));
                Triangle triangle3 = new Triangle(edge.a, secondNoneEdgeVertex, pointSet.get(i));
                Triangle triangle4 = new Triangle(edge.b, secondNoneEdgeVertex, pointSet.get(i));

                triangulation.add(triangle1);
                triangulation.add(triangle2);
                triangulation.add(triangle3);
                triangulation.add(triangle4);

                legalizeEdge(triangle1, new Edge(edge.a, firstNoneEdgeVertex), pointSet.get(i));
                legalizeEdge(triangle2, new Edge(edge.b, firstNoneEdgeVertex), pointSet.get(i));
                legalizeEdge(triangle3, new Edge(edge.a, secondNoneEdgeVertex), pointSet.get(i));
                legalizeEdge(triangle4, new Edge(edge.b, secondNoneEdgeVertex), pointSet.get(i));
            } else {
                Point a = triangle.a;
                Point b = triangle.b;
                Point c = triangle.c;

                triangulation.remove(triangle);

                Triangle first = new Triangle(a, b, pointSet.get(i));
                Triangle second = new Triangle(b, c, pointSet.get(i));
                Triangle third = new Triangle(c, a, pointSet.get(i));

                triangulation.add(first);
                triangulation.add(second);
                triangulation.add(third);

                legalizeEdge(first, new Edge(a, b), pointSet.get(i));
                legalizeEdge(second, new Edge(b, c), pointSet.get(i));
                legalizeEdge(third, new Edge(c, a), pointSet.get(i));
            }
        }

        // remove super triangle
        triangulation.removeTrianglesUsing(superTriangle.a);
        triangulation.removeTrianglesUsing(superTriangle.b);
        triangulation.removeTrianglesUsing(superTriangle.c);

        System.out.println("szw trianglator 02 trian : " + triangulation.getTriangles().size());
        System.out.println("szw trianglator legalizeEdge "+i+" times");
        i = 0;
        return triangulation.getTriangles();
    }

    private int i = 0;
    private void legalizeEdge(Triangle triangle, Edge edge, Point vertex) {
        i++;
        Triangle neighbourTriangle = triangulation.findNeighbour(triangle, edge);

        if (neighbourTriangle != null) {
            if (neighbourTriangle.isPointInCircumcircle(vertex)) {
                triangulation.remove(triangle);
                triangulation.remove(neighbourTriangle);

                Point noneEdgeVertex = neighbourTriangle.getNoneEdgeVertex(edge);

                Triangle firstTriangle = new Triangle(noneEdgeVertex, edge.a, vertex);
                Triangle secondTriangle = new Triangle(noneEdgeVertex, edge.b, vertex);

                triangulation.add(firstTriangle);
                triangulation.add(secondTriangle);

                legalizeEdge(firstTriangle, new Edge(noneEdgeVertex, edge.a), vertex);
                legalizeEdge(secondTriangle, new Edge(noneEdgeVertex, edge.b), vertex);
            }
        }
    }

    private Triangle generateSuperTriangle(Collection<Point> pointSet) {
        final int factor = 3;
        int maxCoordinate = 0;
        int minCoordinate = 0;

        for (Point point : pointSet) {
            maxCoordinate = Math.max(Math.max(point.x, point.y), maxCoordinate);
            minCoordinate = Math.min(Math.min(point.x, point.y), minCoordinate);
        }

        Point p1 = new Point(minCoordinate, factor * maxCoordinate);
        Point p2 = new Point(factor * maxCoordinate, minCoordinate);
        Point p3 = new Point(-factor * maxCoordinate, -factor * maxCoordinate);

        return new Triangle(p1, p2, p3);
    }
}
