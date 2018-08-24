package com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons;

import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;

import java.util.List;

/**
 * A sphere 3D model created from triangles.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class Sphere extends Polyhedron {
    private final Coordinates center;
    private final float radius;
    private int realPrecision;

    /**
     * Create a new Sphere with the given informations.
     *
     * @param center the center of the sphere in the 3D space.
     * @param radius the distance between the center and the border of the sphere.
     */
    public Sphere(final Coordinates center, final float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void precision(final int precision) {
        if (precision >= 1) {
            this.realPrecision = precision;
            super.precision(1);
        }
    }

    @Override
    public Coordinates getPosition() {
        return this.center;
    }

    public float getRadius() {
        return this.radius;
    }

    @Override
    protected void fillTriangles(final List<Triangle> triangles) {
        final Octahedron octahedron = new Octahedron(this.center, this.radius, this.radius);
        octahedron.precision(this.realPrecision);
        final List<Triangle> old = octahedron.generate();
        for (final Triangle tp : old) {
            triangles.add(this.normalize(tp));
        }
    }

    private Triangle normalize(final Triangle tP) {
        final List<Coordinates> oldCoords = tP.coordinates();
        return new Triangle(
                this.normalize(oldCoords.get(0)),
                this.normalize(oldCoords.get(1)),
                this.normalize(oldCoords.get(2))
        );
    }

    private Coordinates normalize(final Coordinates old) {
        float dx = old.x - this.center.x;
        float dy = old.y - this.center.y;
        float dz = old.z - this.center.z;
        final float distance = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
        dx *= this.radius / distance;
        dy *= this.radius / distance;
        dz *= this.radius / distance;
        return new Coordinates(this.center.x + dx, this.center.y + dy, this.center.z + dz);
    }
}
