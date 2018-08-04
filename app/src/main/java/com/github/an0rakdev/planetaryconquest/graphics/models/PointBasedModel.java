package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.graphics.Color;

public abstract class PointBasedModel extends Model {
    protected Color color;

    PointBasedModel() {
        this.color = Color.BLACK;
    }

    public float[] getColor() {
        final float result[] = new float[4];
        result[0] = this.color.r;
        result[1] = this.color.g;
        result[2] = this.color.b;
        result[3] = this.color.a;
        return result;
    }
}
