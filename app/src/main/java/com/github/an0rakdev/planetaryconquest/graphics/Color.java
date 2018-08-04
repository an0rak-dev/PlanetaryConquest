package com.github.an0rakdev.planetaryconquest.graphics;

public class Color {
    public static final int SIZE = Float.BYTES * 4;
    public static final Color BLACK = new Color(0f, 0f, 0f);
    public static final Color WHITE = new Color(1f, 1f, 1f);
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

    public static Color random(final boolean opaque) {
        final Color result = new Color();
        result.r = (float)Math.random();
        result.g = (float)Math.random();
        result.b = (float)Math.random();
        if (opaque) {
            result.a = 1f;
        } else {
            result.a = (float) Math.random();
        }
        return result;
    }
}
