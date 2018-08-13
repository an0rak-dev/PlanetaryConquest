package com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for the triangle based models.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class Polyhedron extends Model {
    private final List<Triangle> triangles;
    private FloatBuffer vertices;
    private int precisionOfEachTriangle;

    /**
     * Default constructor of a polyhedron.
     */
    Polyhedron() {
        this.triangles = new ArrayList<>();
        this.precisionOfEachTriangle = 0;
        this.vertices = null;
    }

    /**
     * Sets the number of subtriangles per triangle.
     *
     * If a model has a high precision, then it will had more details (more triangles)
     * but it will by longer to bufferize it the first time.
     *
     * @param precision the precision of this shape (no changes are made if this value
     *                  is < 1).
     */
    public void precision(final int precision) {
        if (precision >= 1) {
            this.precisionOfEachTriangle = precision;
        }
    }

    @Override
    public FloatBuffer bufferize() {
        if (null == this.vertices) {
            this.precalculate();
            this.vertices = this.createFloatBuffer(this.size()
                    * Coordinates.DIMENSION
                    * FLOAT_BYTE_SIZE);
            for (final Triangle t : this.triangles) {
                for (final Coordinates coord : t.coordinates()) {
                    this.vertices.put(coord.x);
                    this.vertices.put(coord.y);
                    this.vertices.put(coord.z);
                }
            }
        }
        this.vertices.position(0);
        return this.vertices;
    }

    @Override
    public int size() {
        this.precalculate();
        return this.triangles.size() * Triangle.NB_VERTEX;
    }

    /**
     * Create and return a float buffer which contains the colors of each vertices in
     * the same order as <code>bufferize</code>.
     *
     * A background is represented has 4 consecutive values in the resulting buffers (rgba)
     * so the total size of this buffer will be :
     * <code>this.getSize() * 4 * FLOAT_BYTE_SIZE</code>.
     *
     * @return the bufferized colors to apply to this model.
     */
    public FloatBuffer colors() {
        this.precalculate();
        final FloatBuffer colorsBuffer = this.createFloatBuffer(
                this.size()
                * Color.SIZE
                * FLOAT_BYTE_SIZE);
        for (final Triangle triangle : this.triangles) {
            for (int i = 0; i < Triangle.NB_VERTEX; i++) {
                colorsBuffer.put(triangle.color.r);
                colorsBuffer.put(triangle.color.g);
                colorsBuffer.put(triangle.color.b);
                colorsBuffer.put(triangle.color.a);
            }
        }
        colorsBuffer.position(0);
        return colorsBuffer;
    }

    /**
     * Sets the uniform background background of this model.
     *
     * @param c the wanted background
     */
    public void background(final Color c) {
        this.precalculate();
        for (final Triangle t : this.triangles) {
            t.color = c;
        }
    }

    /**
     * Fill the given list with the triangles which composed this model.
     *
     * @param triangles the triangles to fill.
     */
    protected abstract void fillTriangles(final List<Triangle> triangles);

    /**
     * Generate the list of all the triangles used for this model, precision applied.
     *
     * @return the list of all the triangles of this model.
     */
    final List<Triangle> generate() {
        final List<Triangle> result = new ArrayList<>();
        final List<Triangle> basics = new ArrayList<>();
        this.fillTriangles(basics);
        if (0 == this.precisionOfEachTriangle) {
            result.addAll(basics);
        } else {
            for (final Triangle triangle : basics) {
                result.addAll(triangle.split(this.precisionOfEachTriangle));
            }
        }
        return result;
    }

    private void precalculate() {
        if (this.triangles.isEmpty()) {
            this.triangles.addAll(this.generate());
        }
    }
}
