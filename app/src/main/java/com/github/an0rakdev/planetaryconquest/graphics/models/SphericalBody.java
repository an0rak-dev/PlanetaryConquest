package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;

public class SphericalBody {
    private final Polyhedron shape;

    public SphericalBody(Coordinates position, float size) {
        this.shape = new Sphere(position, size);
        this.shape.precision(3);
    }

    public void background(int r, int g, int b) {
        this.shape.background(OpenGLUtils.toOpenGLColor(r,g,b));
    }

    public Polyhedron getShape() {
        return this.shape;
    }

    public float[] model() {
        return this.shape.model();
    }

    public void moveForward(float distance) {
        this.shape.move(0,0, distance);
    }
}
