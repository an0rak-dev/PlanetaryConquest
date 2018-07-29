package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Tetrahedron extends MultiColorModel {
    private final Coordinates center;
    private final float radius;
    private List<Triangle> triangles;

    public Tetrahedron(final Coordinates center, final float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected List<Color> getColorsComponents() {
        if (null == this.triangles) {
            this.loadTriangles();
        }
        final List<Color> colors = new ArrayList<>();
        for (final Triangle t : this.triangles) {
            colors.add(t.getColor());
        }
        return colors;
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
        final Coordinates top = new Coordinates(this.center.x, this.center.y + radius,
                this.center.z);

        final float xoffset = (float) Math.cos(Math.PI/6) * this.radius;
        final float yoffset = (float) Math.sin(Math.PI/6) * this.radius;

        final Coordinates bL = new Coordinates(this.center.x - xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates bR = new Coordinates(this.center.x + xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates bBehind = new Coordinates(this.center.x,
                this.center.y + yoffset /2,
                this.center.z + radius);

        this.triangles.addAll(new Triangle(top, bR, bL).split());
        this.triangles.addAll(new Triangle(top, bBehind, bR).split());
        this.triangles.addAll(new Triangle(top, bBehind, bL).split());
        this.triangles.addAll(new Triangle(bL, bBehind, bR).split());
    }
}
