package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class AsteroidField {
    private final List<Sphere> field;
    private FloatBuffer verticesBuffer;
    private FloatBuffer colorBuffer;
    private final float[] asteroidColor;
    private int verticesCount;

    public AsteroidField(final int count, final float minSize, final float maxSize,
                         final float minX, final float minY, final float minZ,
                         final float maxX, final float maxY, final float maxZ,
                         final float red, final float green, final float blue) {
        this.field = new ArrayList<>(count);
        asteroidColor = OpenGLUtils.toOpenGLColor(red, green, blue);
        while (this.field.size() < count) {
            final float radius = MathUtils.randRange(minSize, maxSize);
            final Coordinates center = new Coordinates(
                    MathUtils.randRange(minX, maxX),
                    MathUtils.randRange(minY, maxY),
                    MathUtils.randRange(minZ, maxZ));
            final Sphere asteroid = new Sphere(center, radius);
            asteroid.precision(1);
            this.field.add(asteroid);
        }
    }

    public List<Sphere> asteroids() {
        return this.field;
    }

    public FloatBuffer vertices() {
        this.bufferize();
        return this.verticesBuffer;
    }

    public FloatBuffer colors() {
        this.bufferize();
        return this.colorBuffer;
    }

    public int verticesCount() {
        this.bufferize();
        return this.verticesCount;
    }

    public void removeAll(final List<Sphere> asteroids) {
        this.field.removeAll(asteroids);
        this.verticesBuffer = null; // Force recalculation of the Vertices & Color buffers.
    }

    private void bufferize() {
        if (this.verticesBuffer == null) {
            List<Coordinates> coords = new ArrayList<>();
            for (final Sphere asteroid : this.field) {
                coords.addAll(asteroid.getAllCoordinates());
            }
            this.verticesCount = coords.size();
            ByteBuffer verticesBB = ByteBuffer.allocateDirect(coords.size() * Coordinates.DIMENSION
                    * (Float.SIZE / Byte.SIZE));
            verticesBB.order(ByteOrder.nativeOrder());
            this.verticesBuffer = verticesBB.asFloatBuffer();

            ByteBuffer colorBB = ByteBuffer.allocateDirect(coords.size() * 4 * (Float.SIZE / Byte.SIZE));
            colorBB.order(ByteOrder.nativeOrder());
            this.colorBuffer = colorBB.asFloatBuffer();

            for (final Coordinates c : coords) {
                this.verticesBuffer.put(c.x);
                this.verticesBuffer.put(c.y);
                this.verticesBuffer.put(c.z);
                this.colorBuffer.put(this.asteroidColor);
            }
        }
        this.verticesBuffer.position(0);
        this.colorBuffer.position(0);
    }
}
