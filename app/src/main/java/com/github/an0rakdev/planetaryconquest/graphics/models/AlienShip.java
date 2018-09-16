package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Triangle;

import java.util.List;

public class AlienShip extends Polyhedron {
    private Coordinates position;

    public AlienShip(final Coordinates position) {
        super();
        this.position = position;
        this.background(OpenGLUtils.toOpenGLColor(
                200, 200, 200
        ));
    }

    @Override
    public Coordinates getPosition() {
        return this.position;
    }

    @Override
    protected void fillTriangles(final List<Triangle> triangles) {
        final float deltaX = 0.6f;
        final float deltaY = 0.25f;
        final float length = 3;
        Coordinates backUp = new Coordinates(this.position.x,
                this.position.y + deltaY, this.position.z + length);
        Coordinates backDown = new Coordinates(this.position.x,
                this.position.y - deltaY, this.position.z + length);
        Coordinates backMiddle = new Coordinates(this.position.x,
                this.position.y, this.position.z + length);
        Coordinates left = new Coordinates(this.position.x - deltaX,
                this.position.y, this.position.z + length);
        Coordinates right = new Coordinates(this.position.x + deltaX,
                this.position.y, this.position.z + length);
        triangles.add(new Triangle(this.position, backUp, right));
        triangles.add(new Triangle(this.position, left, backUp));
        triangles.add(new Triangle(this.position, backDown, right));
        triangles.add(new Triangle(this.position, left, backDown));
        triangles.add(new Triangle(backUp, backMiddle, left));
        triangles.add(new Triangle(backUp, right, backMiddle));
        triangles.add(new Triangle(backMiddle, backDown, left));
        triangles.add(new Triangle(backMiddle, right, backDown));
    }
}
