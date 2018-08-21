package com.github.an0rakdev.planetaryconquest.graphics.models.lines;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Cross extends Lines {
    private final FloatBuffer vertices;

    public Cross(final Coordinates center, final float size) {
        this.vertices = this.createFloatBuffer(this.size() * Coordinates.DIMENSION * FLOAT_BYTE_SIZE);

        final float distance = size /2f;
        final List<Coordinates> bounds = new ArrayList<>();
        bounds.add(new Coordinates(center.x-distance, center.y, center.z));
        bounds.add(new Coordinates(center.x + distance, center.y, center.z));
        bounds.add(new Coordinates(center.x, center.y - distance, center.z));
        bounds.add(new Coordinates(center.x, center.y + distance, center.z));

        for (final Coordinates c : bounds) {
            this.vertices.put(c.x);
            this.vertices.put(c.y);
            this.vertices.put(c.z);
        }
    }

    @Override
    public FloatBuffer bufferize() {
        this.vertices.position(0);
        return this.vertices;
    }

    @Override
    public int size() {
        return 4;
    }
}
