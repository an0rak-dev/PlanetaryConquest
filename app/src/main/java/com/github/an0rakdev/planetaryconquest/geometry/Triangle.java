/* ****************************************************************************
 * Triangle.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.adoc file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Object used to draw a polyhedron (which is a composition of triangles to create
 * more detailled models).
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class Triangle {
    /**
     * Number of vertices per triangle.
     */
    static final int NB_VERTEX = 3;
    private Coordinates top;
    private Coordinates left;
    private Coordinates right;

    /**
     * Create a new Triangle with the given 3 coordinates.
     *
     * @param c1 the first vertex
     * @param c2 the second vertex
     * @param c3 the third vertex
     */
    Triangle(final Coordinates c1, final Coordinates c2, final Coordinates c3) {
        this.top = c1;
        this.left = c2;
        this.right = c3;
    }

    /**
     * Returns the coordinates of this Triangle.
     *
     * @return the coordinates of this Triangle.
     */
    public List<Coordinates> coordinates() {
        List<Coordinates> result = new ArrayList<>();
        result.add(new Coordinates(this.top));
        result.add(new Coordinates(this.left));
        result.add(new Coordinates(this.right));
        return result;
    }

    /**
     * Split the given times this Triangle to create smaller triangles (will not change
     * this model).
     *
     * @param splitCount the number of split to apply.
     * @return the triangles which represents this triangle splited <code>splitCount</code>
     * times.
     */
    public List<Triangle> split(final int splitCount) {
        final List<Triangle> result = new ArrayList<>();
        if (splitCount <= 1) {
            final Coordinates middleL = new Line(this.top, this.left).middle();
            final Coordinates middleR = new Line(this.top, this.right).middle();
            final Coordinates middleB = new Line(this.left, this.right).middle();
            result.add(new Triangle(this.top, middleR, middleL));
            result.add(new Triangle(middleR, this.right, middleB));
            result.add(new Triangle(middleL, middleR, middleB));
            result.add(new Triangle(middleL, middleB, this.left));
        } else {
            final List<Triangle> subtriangles = this.split(1);
            for (final Triangle subtriangle : subtriangles) {
                result.addAll(subtriangle.split(splitCount - 1));
            }
        }
        return result;
    }
}
