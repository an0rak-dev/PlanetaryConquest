package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Octahedron extends Model {
    private final Coordinates center;
    private final float centerToSquare;
    private final float centerToTop;

    public Octahedron(final Coordinates center,
                      final float centerToSquare, final float centerToTop) {
        this.center = center;
        this.centerToSquare = centerToSquare;
        this.centerToTop = centerToTop;
    }

    @Override
    protected void calculateTriangles(List<TrianglePrimitive> coordsToFill) {
        final Coordinates top = new Coordinates(this.center.x, this.center.y + this.centerToTop,
                this.center.z);
        final Coordinates bottom = new Coordinates(this.center.x, this.center.y - this.centerToTop,
                this.center.z);

        final Coordinates fLeft = new Coordinates(this.center.x - this.centerToSquare,
                this.center.y, this.center.z + this.centerToSquare);
        final Coordinates fRight = new Coordinates(this.center.x + this.centerToSquare,
                this.center.y, this.center.z + this.centerToSquare);
        final Coordinates bRight = new Coordinates(this.center.x + this.centerToSquare,
                this.center.y, this.center.z - this.centerToSquare);
        final Coordinates bLeft = new Coordinates(this.center.x - this.centerToSquare,
                this.center.y, this.center.z - this.centerToSquare);

        this.triangles.add(new TrianglePrimitive(top, fRight, fLeft));
        this.triangles.add(new TrianglePrimitive(top, bRight, fRight));
        this.triangles.add(new TrianglePrimitive(top, bLeft, bRight));
        this.triangles.add(new TrianglePrimitive(top, fLeft, bLeft));
        this.triangles.add(new TrianglePrimitive(fRight, bottom, fLeft));
        this.triangles.add(new TrianglePrimitive(bRight, bottom, fRight));
        this.triangles.add(new TrianglePrimitive(bLeft, bottom, bRight));
        this.triangles.add(new TrianglePrimitive(fLeft, bottom, bLeft));
    }
}
