package com.github.an0rakdev.planetaryconquest.graphics.models;

import java.util.ArrayList;
import java.util.List;

public class Icosahedron extends Model {
    private List<Float> coords;
    private static final float TOP[] = {0f, 0.5f, 0f};
    private static final float LEFT[] = {-0.25f, 0f, 0f};
    private static final float RIGHT[] = {0.25f, 0f, 0f};
    private static final float BEHIND[] = {0f, 0.25f, 0.5f};

    @Override
    public float[] getFragmentsColor() {
        return new float[] {
            0.583f,  0.771f,  0.014f, 1f,
            0.971f,  0.572f,  0.833f, 1f,
            0.055f,  0.953f,  0.042f, 1f,
            0.483f,  0.596f,  0.789f, 1f
        };
    }

    @Override
    protected List<Float> calculateCoordonates() {
        if (null == this.coords) {
            this.coords = new ArrayList<>();
            // Front
            this.addPoint(TOP);
            this.addPoint(RIGHT);
            this.addPoint(LEFT);

            // Right
            this.addPoint(TOP);
            this.addPoint(BEHIND);
            this.addPoint(RIGHT);

            // Left
            this.addPoint(BEHIND);
            this.addPoint(TOP);
            this.addPoint(LEFT);

            // Bottom
            this.addPoint(BEHIND);
            this.addPoint(RIGHT);
            this.addPoint(LEFT);
        }
        return this.coords;
    }

    private void addPoint(final float[] point) {
        this.coords.add(point[0]);
        this.coords.add(point[1]);
        this.coords.add(point[2]);
    }
}
