package com.github.an0rakdev.planetaryconquest.graphics.models;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;

import java.nio.FloatBuffer;

public class Laser extends Model {
    private final Coordinates start;
    private final Coordinates end;
    private float[] color;
    private FloatBuffer vertices;
    private final float[] translations;
    private final float[] rotation;

    public Laser(final Coordinates a, final Coordinates b) {
        this.start = a;
        this.end = b;
        this.vertices = null;
        this.translations = new float[16];
        Matrix.setIdentityM(this.translations, 0);
        this.rotation = new float[16];
        Matrix.setIdentityM(this.rotation, 0);
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

    public float[] translations() {
        return this.translations;
    }

    public void move(final float x, final float y, final float z) {
        this.translations[12] += x;
        this.translations[13] += y;
        this.translations[14] += z;
    }

    public Coordinates getPosition() {
        return this.start;
    }

    public void pitch(final float pitchDegrees) {
        final float[] rotation = new float[16];
        Matrix.setRotateM(rotation, 0, pitchDegrees, 0, 1, 0);
        Matrix.multiplyMM(this.rotation, 0, rotation, 0, this.rotation, 0);
    }

    public float[] rotation() {
        return this.rotation;
    }
}
