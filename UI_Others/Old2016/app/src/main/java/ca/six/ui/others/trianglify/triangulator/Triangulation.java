package ca.six.ui.others.trianglify.triangulator;


import java.util.Arrays;
import java.util.Vector;

import ca.six.ui.others.trianglify.domain.Edge;
import ca.six.ui.others.trianglify.domain.Point;
import ca.six.ui.others.trianglify.domain.Triangle;
import ca.six.ui.others.trianglify.util.EdgeDistanceComparator;

/**
 * Triangulation model
 *
 * @author manolovn
 */
public class Triangulation {

    private Vector<Triangle> triangleVector;

    public Triangulation() {
        this.triangleVector = new Vector<>();
    }

    public void add(Triangle triangle) {
        this.triangleVector.add(triangle);
    }

    public void remove(Triangle triangle) {
        this.triangleVector.remove(triangle);
    }

    public Vector<Triangle> getTriangles() {
        return this.triangleVector;
    }

    public Triangle findContainingTriangle(Point point) {
        for (Triangle triangle : triangleVector) {
            if (triangle.contains(point)) {
                return triangle;
            }
        }
        return null;
    }

    public Triangle findNeighbour(Triangle triangle, Edge edge) {
        for (Triangle triangleFromSoup : triangleVector) {
            if (triangleFromSoup.isNeighbour(edge) && triangleFromSoup != triangle) {
                return triangleFromSoup;
            }
        }
        return null;
    }

    public Triangle findOneTriangleSharing(Edge edge) {
        for (Triangle triangle : triangleVector) {
            if (triangle.isNeighbour(edge)) {
                return triangle;
            }
        }
        return null;
    }

    public Edge findNearestEdge(Point point) {
        Vector<EdgeDistanceComparator> edgeVector = new Vector<>();

        for (Triangle triangle : triangleVector) {
            edgeVector.add(triangle.findNearestEdge(point));
        }

        EdgeDistanceComparator[] edgeDistancePacks = new EdgeDistanceComparator[edgeVector.size()];
        edgeVector.toArray(edgeDistancePacks);

        Arrays.sort(edgeDistancePacks);
        return edgeDistancePacks[0].edge;
    }

    public void removeTrianglesUsing(Point vertex) {
        Vector<Triangle> trianglesToBeRemoved = new Vector<>();

        for (Triangle triangle : triangleVector) {
            if (triangle.hasVertex(vertex)) {
                trianglesToBeRemoved.add(triangle);
            }
        }

        triangleVector.removeAll(trianglesToBeRemoved);
    }
}
