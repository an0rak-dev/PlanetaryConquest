package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Octahedron extends MultiColorModel {
    private final List<Color> colors;

    public Octahedron() {
        this.colors = new ArrayList<>();
    }

    @Override
    protected List<Color> getColorsComponents() {
        final List<Color> result = new ArrayList<>();
        for (int i=0; i < 8; i++) {
            result.add(Color.random(true));
        }
        return result;
    }

    @Override
    protected void calculateCoordonates(List<Coordinates> coordsToFill) {
        final float size = 1.0f;
        final Coordinates top = new Coordinates(0f, size, 0f);
        final Coordinates bottom = new Coordinates(0f, -size, 0f);

        final Coordinates fLeft = new Coordinates(-size, 0f, -size);
        final Coordinates fRight = new Coordinates(size, 0f, -size);
        final Coordinates bRight = new Coordinates(size, 0f, size);
        final Coordinates bLeft = new Coordinates(-size, 0f, size);

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
