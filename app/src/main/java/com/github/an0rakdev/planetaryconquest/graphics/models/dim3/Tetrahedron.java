package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.List;

public class Tetrahedron extends TriangleBasedModel {
    private final Coordinates center;
    private final float radius;

    public Tetrahedron(final Coordinates center, final float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected void fillTriangles(List<TrianglePrimitive> triangles) {
        final Coordinates top = new Coordinates(this.center.x, this.center.y + radius,
                this.center.z);

        final float xoffset = (float) Math.cos(Math.PI/6) * this.radius;
        final float yoffset = (float) Math.sin(Math.PI/6) * this.radius;

        final Coordinates bL = new Coordinates(this.center.x - xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates bR = new Coordinates(this.center.x + xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates bBehind = new Coordinates(this.center.x,
                this.center.y + yoffset /2,
                this.center.z + radius);

        this.triangles.add(new TrianglePrimitive(top, bR, bL));
        this.triangles.add(new TrianglePrimitive(top, bBehind, bR));
        this.triangles.add(new TrianglePrimitive(top, bBehind, bL));
        this.triangles.add(new TrianglePrimitive(bL, bBehind, bR));
    }
}
