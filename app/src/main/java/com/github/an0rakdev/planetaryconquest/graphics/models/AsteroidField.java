package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsteroidField {
    private final List<Sphere> field;
    private final Map<Sphere, FloatBuffer> verticesBuffers;
    private final Map<Sphere, FloatBuffer> colorsBuffers;
    private final Map<Sphere, Integer> buffersSizes;

    public AsteroidField(final int count, final float minSize, final float maxSize,
                         final float minX, final float minY, final float minZ,
                         final float maxX, final float maxY, final float maxZ,
                         final float red, final float green, final float blue) {
        this.field = new ArrayList<>(count);
        final float[] asteroidColor = OpenGLUtils.toOpenGLColor(red, green, blue);

        this.verticesBuffers = new HashMap<>();
        this.colorsBuffers = new HashMap<>();
        this.buffersSizes = new HashMap<>();
        while (this.field.size() < count) {
            final float radius = MathUtils.randRange(minSize, maxSize);
            final Coordinates center = new Coordinates(
                    MathUtils.randRange(minX, maxX),
                    MathUtils.randRange(minY, maxY),
                    MathUtils.randRange(minZ, maxZ));
            final Sphere asteroid = new Sphere(center, radius);
            asteroid.background(asteroidColor);
            asteroid.precision(1);
            this.field.add(asteroid);
            this.verticesBuffers.put(asteroid, asteroid.bufferize());
            this.colorsBuffers.put(asteroid, asteroid.colors());
            this.buffersSizes.put(asteroid, asteroid.size());
        }
    }

    public List<Sphere> asteroids() {
        return this.field;
    }

    public FloatBuffer vertices(final Sphere asteroid) {
        return this.verticesBuffers.get(asteroid);
    }

    public FloatBuffer colors(final Sphere asteroid) {
        return this.colorsBuffers.get(asteroid);
    }

    public Integer size(final Sphere asteroid) {
        return this.buffersSizes.get(asteroid);
    }

    public void removeAll(final List<Sphere> asteroids) {
        this.field.removeAll(asteroids);
        for (final Sphere asteroid : asteroids) {
            this.verticesBuffers.remove(asteroid);
            this.colorsBuffers.remove(asteroid);
            this.buffersSizes.remove(asteroid);
        }
    }
}
