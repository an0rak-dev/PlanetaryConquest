package com.github.an0rakdev.planetaryconquest.demos.astronomic;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Octahedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class AsteroidField {
    private final List<Polyhedron> asteroids;
    private final int count;
    private Coordinates minBound;
    private Coordinates maxBound;
    private float minSize;
    private float maxSize;
    private Color color;

    public AsteroidField(final int count) {
        this.asteroids = new ArrayList<>(count);
        this.count = count;
        this.color = Color.random(true);
    }

    public void setBounds(final Coordinates min, final Coordinates max) {
        this.minBound = min;
        this.maxBound = max;
        this.asteroids.clear();
    }

    public void setMinSize(final float size) {
        this.minSize = size;
        this.asteroids.clear();
    }

    public void setMaxSize(final float size) {
        this.maxSize = size;
        this.asteroids.clear();
    }

    public void setDefaultColor(final Color color) {
    	this.color = color;
	}

    public List<Polyhedron> asteroids() {
        if (this.asteroids.isEmpty()) {
            this.fill();
        }
        return this.asteroids;
    }

    public void moveH(final float distance) {
    	for (final Polyhedron asteroid : this.asteroids) {
            ((Sphere)asteroid).move(distance, 0, 0);
		}
	}

    private void fill() {
        while (this.asteroids.size() < this.count) {
            final Coordinates center = new Coordinates(
                    this.random(this.minBound.x, this.maxBound.x),
                    this.random(this.minBound.y, this.maxBound.y),
                    this.random(this.minBound.z, this.maxBound.z)
            );
            final float radius = this.random(this.minSize, this.maxSize);
            final Polyhedron asteroid = new Sphere(center, radius);
            asteroid.precision(3);
            asteroid.background(this.color);
            this.asteroids.add(asteroid);
        }
    }

    private float random(final float min, final float max) {
        return min + (float) Math.random() * (max - min);
    }
}
