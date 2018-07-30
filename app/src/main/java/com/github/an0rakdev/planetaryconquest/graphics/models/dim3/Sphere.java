package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.TrianglePrimitive;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.List;

public class Sphere extends Model {
    final Coordinates center;
    final float radius;

    public Sphere(final Coordinates center, final float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected void fillTriangles(List<TrianglePrimitive> triangles) {
        final Octahedron octahedron = new Octahedron(this.center, this.radius, this.radius);
        octahedron.precision(this.getPrecision());
        triangles.addAll(octahedron.calculateAllTriangles());
    }
}
