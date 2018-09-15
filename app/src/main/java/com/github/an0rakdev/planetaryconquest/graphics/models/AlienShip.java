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
                147, 96, 55
        ));
    }

    @Override
    public Coordinates getPosition() {
        return this.position;
    }

    @Override
    protected void fillTriangles(final List<Triangle> triangles) {
        final Coordinates bottomRight = new Coordinates(this.position.x + 1,
                this.position.y - 1, this.position.z - 1);
        final Coordinates bottomLeft = new Coordinates(this.position.x - 1,
                this.position.y - 1, this.position.z - 1);
        final Coordinates bottomBack = new Coordinates(this.position.x,
                this.position.y - 1, this.position.z + 1);
        final Coordinates top = new Coordinates(this.position.x,
                this.position.y + 1, this.position.z);

        final Triangle front = new Triangle(top, bottomRight, bottomLeft);
        final Triangle right = new Triangle(top, bottomBack, bottomRight);
        final Triangle left = new Triangle(top, bottomBack, bottomLeft);
        final Triangle back = new Triangle(bottomBack, bottomRight, bottomLeft);
        triangles.add(front);
        triangles.add(right);
        triangles.add(left);
        triangles.add(back);
    }
}
