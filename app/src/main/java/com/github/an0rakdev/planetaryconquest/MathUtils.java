package com.github.an0rakdev.planetaryconquest;

import android.opengl.Matrix;

public class MathUtils {

    public static float randRange(final float min, final float max) {
        return min + (float) Math.random() * (max - min);
    }

    public static float angleBetween(final float[] vec1, final float[] vec2) {
        final float scalar = vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
        final float v1Norm = Matrix.length(vec1[0], vec1[1], vec1[2]);
        final float v2Norm = Matrix.length(vec2[0], vec2[1], vec2[2]);
        float cos = scalar;
        if (0 != v1Norm && 0 != v2Norm) {
            cos /= (v1Norm * v2Norm);
        }

        return (float) Math.acos(Math.max(-1f, Math.min(1f, cos)));
    }
}
