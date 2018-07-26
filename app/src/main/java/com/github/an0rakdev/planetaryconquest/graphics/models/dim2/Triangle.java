package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public final class Triangle extends MonoColorModel {
    @Override
    protected void calculateCoordonates(final List<Coordinates> coordsToFill) {
        coordsToFill.add(new Coordinates(-0.25f, 0f, 0f)); // LEFT
        coordsToFill.add(new Coordinates(-0f, 0.5f, 0f)); // TOP
        coordsToFill.add(new Coordinates(0.25f, 0f, 0f)); // RIGHT
    }


}
