/* ****************************************************************************
 * Octahedron.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons;

import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;

import java.util.List;

/**
 * An Octahedron 3D model created with triangles.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class Octahedron extends Polyhedron {
    private final Coordinates center;
    private final float centerToSquareDistance;
    private final float centerToTopDistance;

	/**
	 * Creates a new Octahedron with the given center (the center of the base
	 * square) and given distances.
	 *
	 * @param center the center of the base square.
	 * @param centerToSquare the distance between the center and one point of
	 *                       the square.
	 * @param centerToTop the distance between the center and the point above
	 *                    the square.
	 */
    public Octahedron(final Coordinates center,
                      final float centerToSquare, final float centerToTop) {
        this.center = center;
        this.centerToSquareDistance = centerToSquare;
        this.centerToTopDistance = centerToTop;
    }

    @Override
    public Coordinates getPosition() {
        return this.center;
    }

    @Override
    protected void fillTriangles(final List<Triangle> trianglesToFill) {
		final float front = this.center.z + this.centerToSquareDistance;
		final float back = this.center.z - this.centerToSquareDistance;
		final float left = this.center.x - this.centerToSquareDistance;
		final float right = this.center.x + this.centerToSquareDistance;
        final Coordinates topPt = new Coordinates(this.center.x,
				this.center.y + this.centerToTopDistance,
                this.center.z);
        final Coordinates bottomPt = new Coordinates(this.center.x,
				this.center.y - this.centerToTopDistance,
                this.center.z);
		final Coordinates fLPt = new Coordinates(left, this.center.y, front);
		final Coordinates fRPt = new Coordinates(right, this.center.y, front);
		final Coordinates bRPt = new Coordinates(right, this.center.y, back);
        final Coordinates bLPt = new Coordinates(left, this.center.y, back);

        trianglesToFill.add(new Triangle(topPt, fRPt, fLPt));
        trianglesToFill.add(new Triangle(topPt, bRPt, fRPt));
        trianglesToFill.add(new Triangle(topPt, bLPt, bRPt));
        trianglesToFill.add(new Triangle(topPt, fLPt, bLPt));
        trianglesToFill.add(new Triangle(fRPt, bottomPt, fLPt));
        trianglesToFill.add(new Triangle(bRPt, bottomPt, fRPt));
        trianglesToFill.add(new Triangle(bLPt, bottomPt, bRPt));
        trianglesToFill.add(new Triangle(fLPt, bottomPt, bLPt));
    }
}
