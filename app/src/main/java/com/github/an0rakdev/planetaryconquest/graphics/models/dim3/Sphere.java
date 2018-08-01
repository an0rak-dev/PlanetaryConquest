package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends Octahedron {
    private final Coordinates center;
    private final float radius;
    public Sphere(final Coordinates center, final float radius) {
        super(center, radius, radius);
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected List<TrianglePrimitive> generate() {
        final List<TrianglePrimitive> old = super.generate();
        final List<TrianglePrimitive> result = new ArrayList<>();
        for (final TrianglePrimitive tp : old) {
            result.add(this.normalizeTriangle(tp));
        }
        return result;
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
