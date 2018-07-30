package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.List;

public final class Triangle extends Model {
    private final Coordinates center;
    private final float radius;

    public Triangle(final Coordinates centerPosition, final float radius) {
        this.center = centerPosition;
        this.radius = radius;
    }

    @Override
    protected void fillTriangles(final List<TrianglePrimitive> triangles) {
        final Coordinates top = new Coordinates(this.center.x, this.center.y + radius,
                this.center.z);

        final float xoffset = (float) Math.cos(Math.PI/6) * this.radius;
        final float yoffset = (float) Math.sin(Math.PI/6) * this.radius;

        final Coordinates left = new Coordinates(this.center.x - xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates right = new Coordinates(this.center.x + xoffset, this.center.y -yoffset, this.center.z);
        triangles.add(new TrianglePrimitive(top, right, left));
    }
}
