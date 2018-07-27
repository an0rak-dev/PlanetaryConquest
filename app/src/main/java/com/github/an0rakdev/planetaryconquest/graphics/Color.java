package com.github.an0rakdev.planetaryconquest.graphics;

public class Color {
    public static final int SIZE = Float.BYTES * 4;
    public static final Color RED = new Color(1f, 0f, 0f);
    public static final Color GREEN = new Color(0f, 1f, 0f);
    public static final Color BLUE = new Color(0f, 0f, 1f);
    public static final Color PURPLE = new Color(1f, 0f, 1f);
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
        result.r = ((float)Math.random() % 100) / 100;
        result.g = ((float)Math.random() % 100) / 100;
        result.b = ((float)Math.random() % 100) / 100;
        if (opaque) {
            result.a = 1f;
        } else {
            result.a = ((float) Math.random() % 100) / 100;
        }
        return result;
    }
}
