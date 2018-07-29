package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Tetrahedron extends MultiColorModel {
    private final Coordinates center;
    private final float radius;

    public Tetrahedron(final Coordinates center, final float radius) {
        this.center = center;
        this.radius = radius;
    }

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
        final Coordinates top = new Coordinates(this.center.x, this.center.y + radius,
                this.center.z);

        final float xoffset = (float) Math.cos(Math.PI/6) * this.radius;
        final float yoffset = (float) Math.sin(Math.PI/6) * this.radius;

        final Coordinates bL = new Coordinates(this.center.x - xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates bR = new Coordinates(this.center.x + xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates bBehind = new Coordinates(this.center.x,
                this.center.y + yoffset / 2,
                this.center.z + xoffset);

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
