/* ****************************************************************************
 * AlienShip.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Triangle;

import java.util.List;

public class AlienShip extends Polyhedron {
    private Coordinates position;
    private float size;

    public AlienShip(final Coordinates position, final float size) {
        super();
        this.position = position;
        this.background(OpenGLUtils.toOpenGLColor(
                200, 200, 200
        ));
        this.size = size;
    }

    @Override
    public Coordinates getPosition() {
        return this.position;
    }

    @Override
    protected void fillTriangles(final List<Triangle> triangles) {
        final float deltaX = this.size / 4;
        final float deltaY = this.size / 8;
        Coordinates backUp = new Coordinates(this.position.x,
                this.position.y + deltaY, this.position.z + this.size);
        Coordinates backDown = new Coordinates(this.position.x,
                this.position.y - deltaY, this.position.z + this.size);
        Coordinates backMiddle = new Coordinates(this.position.x,
                this.position.y, this.position.z + this.size);
        Coordinates left = new Coordinates(this.position.x - deltaX,
                this.position.y, this.position.z + this.size);
        Coordinates right = new Coordinates(this.position.x + deltaX,
                this.position.y, this.position.z + this.size);
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
