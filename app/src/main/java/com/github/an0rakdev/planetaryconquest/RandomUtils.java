package com.github.an0rakdev.planetaryconquest;

public class RandomUtils {

    public static float randRange(final float min, final float max) {
        return min + (float) Math.random() * (max - min);
    }
}
