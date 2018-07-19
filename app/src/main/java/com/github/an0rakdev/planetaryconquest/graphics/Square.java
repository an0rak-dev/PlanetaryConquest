package com.github.an0rakdev.planetaryconquest.graphics;

import java.util.ArrayList;
import java.util.List;

public class Square extends Model {

    @Override
    public float[] getFragmentsColor() {
        return new float[]{
                0.636f, 0.795f, 0.222f, 1f
        };
    }

    @Override
    protected List<Float> calculateCoordonates() {
        final List<Float> coords = new ArrayList<>();
        // First Triangle.
        coords.add(0.25f);
        coords.add(0.5f);
        coords.add(0f);
        coords.add(0.25f);
        coords.add(-0.5f);
        coords.add(0f);
        coords.add(-0.25f);
        coords.add(0.5f);
        coords.add(0f);
        // Second triangle
        coords.add(0.25f);
        coords.add(-0.5f);
        coords.add(0f);
        coords.add(-0.25f);
        coords.add(0.5f);
        coords.add(0f);
        coords.add(-0.25f);
        coords.add(-0.5f);
        coords.add(0f);
        return coords;
    }
}
