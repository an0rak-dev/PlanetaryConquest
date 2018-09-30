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

    public void draw(final int openglProgram) {
        // TODO Use a complex object which will holds the program, and the name of its attributes.
        final int moonVHandle = OpenGLUtils.bindVerticesToProgram(openglProgram, this.shape.bufferize(), "vVertices");
        final int moonCHandle = OpenGLUtils.bindColorToProgram(openglProgram, this.shape.colors(), "vColors");
        OpenGLUtils.drawTriangles(this.shape.size(), moonVHandle, moonCHandle);

    }
}
