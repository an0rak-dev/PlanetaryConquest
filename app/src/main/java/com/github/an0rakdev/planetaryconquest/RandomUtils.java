package com.github.an0rakdev.planetaryconquest;

public class RandomUtils {

    public static float randRange(final float min, final float max) {
        return min + (float) Math.random() * (max - min);
    }

    public static float[] randOpenGlColor() {
        return OpenGLUtils.toOpenGLColor(
                randRange(0f, 255f),
                randRange(0f, 255f),
                randRange(0f, 255f)
        );
    }
}
