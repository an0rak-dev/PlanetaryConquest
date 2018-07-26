package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public final class Square extends MultiColorModel {
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

    @Override
    protected List<Float> getColorsComponents() {
        final List<Float> result = new ArrayList<>();
        result.add(0.538f);
        result.add(0.124f);
        result.add(0.822f);
        result.add(1f);
        result.add(0.235f);
        result.add(0.767f);
        result.add(0.018f);
        result.add(1f);
        return result;
    }
}
