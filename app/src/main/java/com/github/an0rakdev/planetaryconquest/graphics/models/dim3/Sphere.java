package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends MonoColorModel {

    @Override
    protected List<Float> calculateCoordonates() {
        final List<Float> coords = new ArrayList<>();
        // TODO Base this on a normalized Icosahedron model.
        return coords;
    }
}
