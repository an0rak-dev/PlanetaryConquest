package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;

import java.nio.FloatBuffer;

public class Line extends Model {
    private final Coordinates start;
    private final Coordinates end;
    private float[] color;
    private FloatBuffer vertices;

    public Line(final Coordinates a, final Coordinates b) {
        this.start = a;
        this.end = b;
        this.vertices = null;
    }

    public void color(final int red, final int green, final int blue) {
        this.color = OpenGLUtils.toOpenGLColor(red, green, blue);
    }

    @Override
    public FloatBuffer bufferize() {
        if (null == this.vertices) {
            this.vertices = this.createFloatBuffer(this.size() * Coordinates.DIMENSION * FLOAT_BYTE_SIZE);
            this.vertices.put(this.start.x);
            this.vertices.put(this.start.y);
            this.vertices.put(this.start.z);
            this.vertices.put(this.end.x);
            this.vertices.put(this.end.y);
            this.vertices.put(this.end.z);
        }
        this.vertices.position(0);
        return this.vertices;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public FloatBuffer colors() {
        final FloatBuffer colorBuffer = this.createFloatBuffer(this.size()
                * COLOR_SIZE
                * FLOAT_BYTE_SIZE);
        for (int i=0; i < this.size(); i++) {
            colorBuffer.put(this.color);
        }
        colorBuffer.position(0);
        return colorBuffer;
    }
}
