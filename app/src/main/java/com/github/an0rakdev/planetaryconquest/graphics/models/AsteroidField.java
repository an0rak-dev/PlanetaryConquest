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
 /*       while (this.field.size() < count) {
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
            */

        final float radius = 0.3f;
            // Center
        final Coordinates center = new Coordinates(0, 0, 5);
        final Sphere asteroid = new Sphere(center, radius);
        asteroid.background(asteroidColor);
        asteroid.precision(1);
        this.field.add(asteroid);
        this.verticesBuffers.put(asteroid, asteroid.bufferize());
        this.colorsBuffers.put(asteroid, asteroid.colors());
        this.buffersSizes.put(asteroid, asteroid.size());

            // Left
        final Coordinates center1 = new Coordinates(2, 0, 5);
        final Sphere asteroid1 = new Sphere(center1, radius);
        asteroid1.background(asteroidColor);
        asteroid1.precision(1);
        this.field.add(asteroid1);
        this.verticesBuffers.put(asteroid1, asteroid1.bufferize());
        this.colorsBuffers.put(asteroid1, asteroid1.colors());
        this.buffersSizes.put(asteroid1, asteroid1.size());

            // Right
        final Coordinates center2 = new Coordinates(-2, 0, 5);
        final Sphere asteroid2 = new Sphere(center2, radius);
        asteroid2.background(asteroidColor);
        asteroid2.precision(1);
        this.field.add(asteroid2);
        this.verticesBuffers.put(asteroid2, asteroid2.bufferize());
        this.colorsBuffers.put(asteroid2, asteroid2.colors());
        this.buffersSizes.put(asteroid2, asteroid2.size());

            // Top
        final Coordinates center3 = new Coordinates(0, 2, 5);
        final Sphere asteroid3 = new Sphere(center3, radius);
        asteroid3.background(asteroidColor);
        asteroid3.precision(1);
        this.field.add(asteroid3);
        this.verticesBuffers.put(asteroid3, asteroid3.bufferize());
        this.colorsBuffers.put(asteroid3, asteroid3.colors());
        this.buffersSizes.put(asteroid3, asteroid3.size());

            // Bottom
        final Coordinates center4 = new Coordinates(0, -2, 5);
        final Sphere asteroid4 = new Sphere(center4, radius);
        asteroid4.background(asteroidColor);
        asteroid4.precision(1);
        this.field.add(asteroid4);
        this.verticesBuffers.put(asteroid4, asteroid4.bufferize());
        this.colorsBuffers.put(asteroid4, asteroid4.colors());
        this.buffersSizes.put(asteroid4, asteroid4.size());
   //     }
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
