package com.github.an0rakdev.planetaryconquest.math;

public class Coordinates {
    public static final int DIMENSION = 3;
    public float x;
    public float y;
    public float z;

    public Coordinates() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Coordinates(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
