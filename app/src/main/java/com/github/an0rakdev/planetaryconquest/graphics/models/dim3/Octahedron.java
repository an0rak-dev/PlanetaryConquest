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

    public Octahedron(final Coordinates center,
                      final float centerToSquare, final float centerToTop) {
        this.center = center;
        this.centerToSquare = centerToSquare;
        this.centerToTop = centerToTop;
    }

    @Override
    protected List<Color> getColorsComponents() {
        final List<Color> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            result.add(Color.random(true));
        }
        return result;
    }

    @Override
    protected void calculateCoordonates(List<Coordinates> coordsToFill) {
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

        // Up front triangle
        coordsToFill.add(top);
        coordsToFill.add(fRight);
        coordsToFill.add(fLeft);
        //*
        // Up right triangle
        coordsToFill.add(top);
        coordsToFill.add(bRight);
        coordsToFill.add(fRight);
        // Up back triangle
        coordsToFill.add(top);
        coordsToFill.add(bLeft);
        coordsToFill.add(bRight);
        // Up left triangle
        coordsToFill.add(top);
        coordsToFill.add(fLeft);
        coordsToFill.add(bLeft);
        // Down front triangle
        coordsToFill.add(fRight);
        coordsToFill.add(bottom);
        coordsToFill.add(fLeft);
        // Down right triangle
        coordsToFill.add(bRight);
        coordsToFill.add(bottom);
        coordsToFill.add(fRight);
        // Down back triangle
        coordsToFill.add(bLeft);
        coordsToFill.add(bottom);
        coordsToFill.add(bRight);
        // Down left triangle
        coordsToFill.add(fLeft);
        coordsToFill.add(bottom);
        coordsToFill.add(bLeft);
        // */
    }
}
