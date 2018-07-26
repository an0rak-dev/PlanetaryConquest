package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public final class Square extends MonoColorModel {
    @Override
    protected void calculateCoordonates(final List<Coordinates> coordsToFill) {
        final Coordinates topLeft = new Coordinates(-0.5f, 0.5f, 0f);
        final Coordinates bottomRight = new Coordinates(0.5f, -0.5f, 0f);

        coordsToFill.add(topLeft);
        coordsToFill.add(new Coordinates(0.5f, 0.5f, 0f)); // TOP RIGHT
        coordsToFill.add(bottomRight);

        coordsToFill.add(topLeft);
        coordsToFill.add(bottomRight);
        coordsToFill.add(new Coordinates(-0.5f, -0.5f, 0f)); // BOTTOM LEFT
    }
}
