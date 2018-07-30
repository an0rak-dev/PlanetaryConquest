package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.List;

public class Sphere extends Model {
    final Coordinates center;
    final float radius;

    public Sphere(final Coordinates center, final float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected void fillTriangles(List<TrianglePrimitive> triangles) {
        final Octahedron octahedron = new Octahedron(this.center, this.radius, this.radius);
        octahedron.precision(this.getPrecision());
        final List<TrianglePrimitive> points = octahedron.calculateAllTriangles();
        for (final TrianglePrimitive t : points) {
            triangles.add(normalizeTriangle(t));
        }
    }

    private TrianglePrimitive normalizeTriangle(final TrianglePrimitive tP) {
        final List<Coordinates> oldCoords = tP.getCoordinates();
        return new TrianglePrimitive(
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
