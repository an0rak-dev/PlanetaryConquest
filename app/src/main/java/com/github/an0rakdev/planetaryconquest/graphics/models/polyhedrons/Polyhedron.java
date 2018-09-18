package com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;

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
    private FloatBuffer colors;
    private int precisionOfEachTriangle;
    private float[] color;

    /**
     * Default constructor of a polyhedron.
     */
    protected Polyhedron() {
        this.triangles = new ArrayList<>();
        this.precisionOfEachTriangle = 0;
        this.vertices = null;
        this.color = OpenGLUtils.randOpenGlColor();
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

    @Override
    public FloatBuffer colors() {
        this.precalculate();
        if (null == this.colors) {
            this.colors = this.createFloatBuffer(
                    this.size()
                            * COLOR_SIZE
                            * FLOAT_BYTE_SIZE);
            for (int i = 0; i < this.triangles.size(); i++) {
                for (int j = 0; j < Triangle.NB_VERTEX; j++) {
                    this.colors.put(this.color);
                }
            }
        }
        this.colors.position(0);
        return this.colors;
    }

    /**
     * Sets the uniform background background of this model.
     *
     * @param c the wanted background
     */
    public void background(final float[] c) {
        this.color = c;
    }

    public abstract Coordinates getPosition();

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

    public List<Coordinates> getAllCoordinates() {
        this.precalculate();
        List<Coordinates> result = new ArrayList<>();
        for (final Triangle t : this.triangles) {
            result.addAll(t.coordinates());
        }
        return result;
    }

    private void precalculate() {
        if (this.triangles.isEmpty()) {
            this.triangles.addAll(this.generate());
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (null == o || !(o instanceof Polyhedron)) {
            return false;
        }

        final Polyhedron that = (Polyhedron)o;
        return this.getPosition().equals(that.getPosition());
    }

    public float[] model() {
        final float[] result = new float[16];
        Matrix.setIdentityM(result, 0);
        Matrix.translateM(result, 0, this.getPosition().x, this.getPosition().y, this.getPosition().z);
        return result;
    }
}
