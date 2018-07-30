package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim2.Triangle;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Model {
    protected List<TrianglePrimitive> triangles;
    private FloatBuffer vertices;
    private FloatBuffer colorsBuffer;
    private int precisionOfEachTriangle;

    public Model() {
        this.triangles = new ArrayList<>();
        this.vertices = null;
        this.precisionOfEachTriangle = 0;
    }

    public void precision(final int precision) {
        if (precision >= 1) {
            this.precisionOfEachTriangle = precision;
        }
    }

    protected int getPrecision() {
        return this.precisionOfEachTriangle;
    }

    /**
     * @return the number of bytes for one vertex in this model.
     */
    public int getVerticesStride() {
        return Float.BYTES;
    }

    /**
     * @return the vertices byte buffer for this shape.
     */
    public FloatBuffer getVerticesBuffer() {
        if (null == this.vertices) {
            this.calculateAllTriangles();
            final ByteBuffer bb = ByteBuffer.allocateDirect(triangles.size()
                    * TrianglePrimitive.NB_VERTEX * Coordinates.DIMENSION * this.getVerticesStride());
            bb.order(ByteOrder.nativeOrder());
            this.vertices = bb.asFloatBuffer();
            for (final TrianglePrimitive t : this.triangles) {
                for (final Coordinates coord : t.getCoordinates()) {
                    this.vertices.put(coord.x);
                    this.vertices.put(coord.y);
                    this.vertices.put(coord.z);
                }
            }
        }
        this.vertices.position(0);
        return this.vertices;
    }

    public FloatBuffer getColors() {
        this.calculateAllTriangles();
        final ByteBuffer bb = ByteBuffer.allocateDirect(this.triangles.size()
                * TrianglePrimitive.NB_VERTEX * Color.SIZE * Float.BYTES);
        bb.order(ByteOrder.nativeOrder());
        this.colorsBuffer = bb.asFloatBuffer();
        for (final TrianglePrimitive triangle : this.triangles) {
            for (int i = 0; i < TrianglePrimitive.NB_VERTEX; i++) {
                this.colorsBuffer.put(triangle.color.r);
                this.colorsBuffer.put(triangle.color.g);
                this.colorsBuffer.put(triangle.color.b);
                this.colorsBuffer.put(triangle.color.a);
            }
        }
        this.colorsBuffer.position(0);
        return this.colorsBuffer;
    }

    /**
     * @return the number of vertices used for drawing this model.
     */
    public int getNbOfVertices() {
        this.calculateAllTriangles();
        return this.triangles.size() * TrianglePrimitive.NB_VERTEX;
    }

    protected abstract void fillTriangles(final List<TrianglePrimitive> triangles);

    public List<TrianglePrimitive> calculateAllTriangles() {
        if (this.triangles.isEmpty()) {
            final List<TrianglePrimitive> basics = new ArrayList<>();
            this.fillTriangles(basics);
            if (0 == this.precisionOfEachTriangle) {
                this.triangles.addAll(basics);
            } else {
                for (final TrianglePrimitive triangle : basics) {
                    this.triangles.addAll(triangle.split(this.precisionOfEachTriangle));
                }
            }
        }
        return this.triangles;
    }

    public void setBackgroundColor(final Color c) {
        this.calculateAllTriangles();
        for (final TrianglePrimitive t : this.triangles) {
            t.color = c;
        }
    }
}
