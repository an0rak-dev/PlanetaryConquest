package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;

import java.util.ArrayList;
import java.util.List;

public final class Square extends MonoColorModel {
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
