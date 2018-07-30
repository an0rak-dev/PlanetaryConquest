package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public final class Square extends Model {
    private final Coordinates center;
    private final float size;

    public Square(final Coordinates center, final float size) {
        this.center = center;
        this.size = size;
    }

    @Override
    protected void calculateTriangles(final List<TrianglePrimitive> triangles) {
        final float half = this.size /2;

        final Coordinates tL = new Coordinates(this.center.x - half, this.center.y + half, this.center.z);
        final Coordinates bR = new Coordinates(this.center.x + half, this.center.y - half, this.center.z);
        final Coordinates tR = new Coordinates(this.center.x + half, this.center.y + half, this.center.z);
        final Coordinates bL = new Coordinates(this.center.x - half, this.center.y - half, this.center.z);
        triangles.add(new TrianglePrimitive(tL, tR, bR));
        triangles.add(new TrianglePrimitive(tL, bR, bL));
    }
}
