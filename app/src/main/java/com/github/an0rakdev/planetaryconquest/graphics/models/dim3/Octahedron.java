package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.List;

public class Octahedron extends TriangleBasedModel {
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
    protected void fillTriangles(List<TrianglePrimitive> trianglesToFill) {
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

        trianglesToFill.add(new TrianglePrimitive(top, fRight, fLeft));
        trianglesToFill.add(new TrianglePrimitive(top, bRight, fRight));
        trianglesToFill.add(new TrianglePrimitive(top, bLeft, bRight));
        trianglesToFill.add(new TrianglePrimitive(top, fLeft, bLeft));
        trianglesToFill.add(new TrianglePrimitive(fRight, bottom, fLeft));
        trianglesToFill.add(new TrianglePrimitive(bRight, bottom, fRight));
        trianglesToFill.add(new TrianglePrimitive(bLeft, bottom, bRight));
        trianglesToFill.add(new TrianglePrimitive(fLeft, bottom, bLeft));
    }
}
