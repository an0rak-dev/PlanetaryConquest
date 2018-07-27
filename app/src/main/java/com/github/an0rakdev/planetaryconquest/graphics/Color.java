package com.github.an0rakdev.planetaryconquest.graphics;

public class Color {
    public static final int SIZE = Float.BYTES * 4;
    public float r;
    public float g;
    public float b;
    public float a;

    public Color() {
        this.r = 0f;
        this.g = 0f;
        this.b = 0f;
        this.a = 0f;
    }

    public Color(final float r, final float g, final float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1f;
    }
}
