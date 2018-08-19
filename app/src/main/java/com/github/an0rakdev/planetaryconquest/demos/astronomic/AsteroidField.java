package com.github.an0rakdev.planetaryconquest.demos.astronomic;

import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class AsteroidField {
    private final List<Polyhedron> asteroids;
    private final Coordinates minBound;
    private final Coordinates maxBound;

    public AsteroidField(final int capacity, final Coordinates min, final Coordinates max) {
        this.asteroids = new ArrayList<>(capacity);
        this.minBound = min;
        this.maxBound = max;
    }

    public List<Polyhedron> asteroids() {
        if (this.asteroids.isEmpty()) {
            this.fill();
        }
        return this.asteroids;
    }

    private void fill() {
        // TODO
    }
}
