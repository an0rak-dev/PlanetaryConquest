package com.github.an0rakdev.planetaryconquest.graphics.models.lines;

import com.github.an0rakdev.planetaryconquest.graphics.models.points.Points;

public abstract class Lines extends Points {
    private float width;

    public Lines() {
        this.width = 1f;
    }
    public void width(final float w) {
        if (w > 0f) {
            this.width = w;
        }
    }

    public float width() {
        return this.width;
    }
}
