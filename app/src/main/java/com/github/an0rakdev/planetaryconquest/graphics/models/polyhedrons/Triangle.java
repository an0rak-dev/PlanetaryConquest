/* ****************************************************************************
 * Triangle.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons;

import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;

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
    public static final int NB_VERTEX = 3;
    private final List<Coordinates> coords;

    /**
     * Create a new Triangle with the given 3 coordinates.
     *
     * @param c1 the first vertex
     * @param c2 the second vertex
     * @param c3 the third vertex
     */
    public Triangle(final Coordinates c1, final Coordinates c2, final Coordinates c3) {
        this.coords = new ArrayList<>();
        this.coords.add(c1);
        this.coords.add(c2);
        this.coords.add(c3);
    }

    /**
     * Returns the coordinates of this Triangle.
     *
     * @return the coordinates of this Triangle.
     */
    public List<Coordinates> coordinates() {
        return this.coords;
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
            final Coordinates top = this.coords.get(0);
            final Coordinates right = this.coords.get(1);
            final Coordinates left = this.coords.get(2);
            final Coordinates middleL = this.middleOf(top, left);
            final Coordinates middleR = this.middleOf(top, right);
            final Coordinates middleB = this.middleOf(left, right);
            result.add(new Triangle(top, middleR, middleL));
            result.add(new Triangle(middleR, right, middleB));
            result.add(new Triangle(middleL, middleR, middleB));
            result.add(new Triangle(middleL, middleB, left));
        } else {
            final List<Triangle> subtriangles = this.split(1);
            for (final Triangle subtriangle : subtriangles) {
                result.addAll(subtriangle.split(splitCount - 1));
            }
        }
        return result;
    }

    private Coordinates middleOf(final Coordinates c1, final Coordinates c2) {
        return new Coordinates(this.middleOf(c1.x, c2.x),
                this.middleOf(c1.y, c2.y),
                this.middleOf(c1.z, c2.z));
    }

    private float middleOf(final float p1, final float p2) {
        final float a = (p1 > p2) ? p1 : p2;
        final float b = (p1 > p2) ? p2 : p1;
        return a - ((a - b) / 2);
    }
}
