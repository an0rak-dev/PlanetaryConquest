package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Octahedron extends MultiColorModel {
    private final Coordinates center;
    private final float centerToSquare;
    private final float centerToTop;
    private List<Triangle> triangles;

    public Octahedron(final Coordinates center,
                      final float centerToSquare, final float centerToTop) {
        this.center = center;
        this.centerToSquare = centerToSquare;
        this.centerToTop = centerToTop;
    }

    @Override
    protected List<Color> getColorsComponents() {
        if (null == this.triangles) {
            this.loadTriangles();
        }
        final List<Color> result = new ArrayList<>();
        for (final Triangle t : this.triangles) {
            result.add(t.getColor());
        }
        return result;
    }

    @Override
    protected void calculateCoordonates(List<Coordinates> coordsToFill) {
        if (null == this.triangles) {
            this.loadTriangles();
        }
        for (final Triangle t : this.triangles) {
            coordsToFill.addAll(t.getCoordinates());
        }
    }

    private void loadTriangles() {
        this.triangles = new ArrayList<>();
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

        this.triangles.addAll(new Triangle(top, fRight, fLeft).split());
        this.triangles.addAll(new Triangle(top, bRight, fRight).split());
        this.triangles.addAll(new Triangle(top, bLeft, bRight).split());
        this.triangles.addAll(new Triangle(top, fLeft, bLeft).split());
        this.triangles.addAll(new Triangle(fRight, bottom, fLeft).split());
        this.triangles.addAll(new Triangle(bRight, bottom, fRight).split());
        this.triangles.addAll(new Triangle(bLeft, bottom, bRight).split());
        this.triangles.addAll(new Triangle(fLeft, bottom, bLeft).split());
    }
}
