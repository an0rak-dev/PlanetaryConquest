package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;

import java.util.ArrayList;
import java.util.List;

public final class Triangle extends MonoColorModel {
    @Override
    protected List<Float> calculateCoordonates() {
        final List<Float> coords = new ArrayList<>();
        final Float z = 0f;
        final Float y = 0.5f;
        // First point
        coords.add(0.0f);
        coords.add(y);
        coords.add(z);
        // Second point
        coords.add(-0.5f);
        coords.add(y * -0.5f);
        coords.add(z);
        // Third point
        coords.add(0.5f);
        coords.add(y * -0.5f);
        coords.add(z);
        return coords;
    }
}
