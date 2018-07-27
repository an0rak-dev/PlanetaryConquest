package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Model {
    protected List<Coordinates> vertexCoords;
    private FloatBuffer vertices;
    private int nbOfVertices;

    public Model() {
        this.vertexCoords = new ArrayList<>();
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
        return this.vertices;
    }

    public abstract boolean hasSeveralColors();

    /**
     * @return the number of vertices used for drawing this model.
     */
    public int getNbOfVertices() {
        if (0 == this.nbOfVertices) {
            this.populateBuffer();
        }
        return this.nbOfVertices;
    }

    protected abstract void calculateCoordonates(final List<Coordinates> coordsToFill);

    private void populateBuffer() {
        this.calculateCoordonates(this.vertexCoords);
        this.nbOfVertices = vertexCoords.size();
        final ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoords.size()
                * Coordinates.DIMENSION * this.getVerticesStride());
        bb.order(ByteOrder.nativeOrder());
        this.vertices = bb.asFloatBuffer();
        for (final Coordinates coord : vertexCoords) {
            this.vertices.put(coord.x);
            this.vertices.put(coord.y);
            this.vertices.put(coord.z);
        }
        this.vertices.position(0);
    }
}
