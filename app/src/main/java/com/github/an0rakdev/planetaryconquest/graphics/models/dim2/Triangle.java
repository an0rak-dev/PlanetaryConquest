package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.List;

public final class Triangle extends MonoColorModel {
    private final Coordinates center;
    private final float radius;

    public Triangle(final Coordinates centerPosition, final float radius) {
        this.center = centerPosition;
        this.radius = radius;
    }

    @Override
    protected void calculateCoordonates(final List<Coordinates> coordsToFill) {
        final Coordinates top = new Coordinates(this.center.x, this.center.y + radius,
                this.center.z);

        final float xoffset = (float) Math.cos(Math.PI/6) * this.radius;
        final float yoffset = (float) Math.sin(Math.PI/6) * this.radius;

        final Coordinates left = new Coordinates(this.center.x - xoffset, this.center.y -yoffset, this.center.z);
        final Coordinates right = new Coordinates(this.center.x + xoffset, this.center.y -yoffset, this.center.z);
        coordsToFill.add(top);
        coordsToFill.add(right);
        coordsToFill.add(left);
    }
}
