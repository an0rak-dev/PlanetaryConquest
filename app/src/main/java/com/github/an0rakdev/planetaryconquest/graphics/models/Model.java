package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Model {
    protected List<Coordinates> vertexCoords;
    private final FloatBuffer vertices;
    private final int nbOfVertices;

    public Model() {
        this.vertexCoords = new ArrayList<>();
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

    /**
     * @return the number of bytes for one vertex in this model.
     */
    public int getVerticesStride() {
        return (Float.SIZE / Byte.SIZE);
    }

    /**
     * @return the vertices byte buffer for this shape.
     */
    public FloatBuffer getVerticesBuffer() {
        return this.vertices;
    }

    /**
     * @return the colors to use for those fragments (one color = 4 entries in the array).
     */
    public abstract float[] getFragmentsColor();

    /**
     * @return the number of vertices used for drawing this model.
     */
    public int getNbOfVertices() {
        return this.nbOfVertices;
    }

    protected abstract void calculateCoordonates(final List<Coordinates> coordsToFill);
}
