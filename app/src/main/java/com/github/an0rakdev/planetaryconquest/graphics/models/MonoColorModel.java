package com.github.an0rakdev.planetaryconquest.graphics.models;

public abstract class MonoColorModel extends Model {
    @Override
    public float[] getFragmentsColor() {
        return new float[]{
                0.636f, 0.795f, 0.222f, 1f
        };
    }
}
