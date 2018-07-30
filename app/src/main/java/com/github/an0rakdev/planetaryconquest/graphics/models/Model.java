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
    private int nbOfVertices;

    public Model() {
        this.triangles = new ArrayList<>();
        this.vertices = null;
        this.nbOfVertices = 0;
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
            this.populateBuffer();
        }
        this.vertices.position(0);
        return this.vertices;
    }

    public FloatBuffer getColors() {
        if (null == this.colorsBuffer) {
            final ByteBuffer bb = ByteBuffer.allocateDirect(this.triangles.size()
                    * TrianglePrimitive.NB_VERTEX * Color.SIZE * Float.BYTES);
            bb.order(ByteOrder.nativeOrder());
            this.colorsBuffer = bb.asFloatBuffer();
            for (final TrianglePrimitive triangle : this.triangles) {
                for (int i = 0; i < TrianglePrimitive.NB_VERTEX; i++) {
                    this.colorsBuffer.put(triangle.getColor().r);
                    this.colorsBuffer.put(triangle.getColor().g);
                    this.colorsBuffer.put(triangle.getColor().b);
                    this.colorsBuffer.put(triangle.getColor().a);
                }
            }
        }
        this.colorsBuffer.position(0);
        return this.colorsBuffer;
    }

    /**
     * @return the number of vertices used for drawing this model.
     */
    public int getNbOfVertices() {
        if (0 == this.nbOfVertices) {
            this.populateBuffer();
        }
        return this.nbOfVertices;
    }

    protected abstract void calculateTriangles(final List<TrianglePrimitive> triangles);

    private void populateBuffer() {
        this.calculateTriangles(this.triangles);
        this.nbOfVertices = triangles.size() * TrianglePrimitive.NB_VERTEX;
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
        this.vertices.position(0);
    }
}
