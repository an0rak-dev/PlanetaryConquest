package com.github.an0rakdev.planetaryconquest.graphics;

/**
 * A 4 components (Red, Green, Blue, Alpha) color.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class Color {
    /**
     * The size of a Color object in bytes.
     */
    public static final int SIZE = (Float.SIZE / Byte.SIZE) * 4;
    /**
     * The black color
     */
    public static final Color BLACK = new Color(0f, 0f, 0f);
    /** The white color */
    public static final Color WHITE = new Color(1f, 1f, 1f);

    /** The red component of this color */
    public float r;
    /** The green component of this color */
    public float g;
    /** The blue component of this color */
    public float b;
    /** The opacity component of this color */
    public float a;

    public Color(final float r, final float g, final float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1f;
    }

    /**
     * Create a new random color.
     *
     * @param opaque true if the color needs to be fully opaque, false if it can be
     *               randomized too.
     * @return a fully random color.
     */
    public static Color random(final boolean opaque) {
        final Color result = new Color(0f, 0f ,0f);
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
