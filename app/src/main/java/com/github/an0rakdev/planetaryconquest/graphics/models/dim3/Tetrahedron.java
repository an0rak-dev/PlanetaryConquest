package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Tetrahedron extends MultiColorModel {

    @Override
    protected List<Color> getColorsComponents() {
        final List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.PURPLE);
        return colors;
    }

    @Override
    protected void calculateCoordonates(List<Coordinates> coordsToFill) {
        final Coordinates top = new Coordinates(0.0f, 1f, 0.0f);
        final Coordinates bL = new Coordinates(0.5f, 0f, 0f);
        final Coordinates bR = new Coordinates(-0.5f, 0f, 0f);
        final Coordinates bBehind = new Coordinates(0f, 0.4f, 0.7f);

        coordsToFill.add(top);
        coordsToFill.add(bR);
        coordsToFill.add(bL);

        coordsToFill.add(top);
        coordsToFill.add(bBehind);
        coordsToFill.add(bR);

        coordsToFill.add(top);
        coordsToFill.add(bBehind);
        coordsToFill.add(bL);

        coordsToFill.add(bL);
        coordsToFill.add(bBehind);
        coordsToFill.add(bR);
    }
}
