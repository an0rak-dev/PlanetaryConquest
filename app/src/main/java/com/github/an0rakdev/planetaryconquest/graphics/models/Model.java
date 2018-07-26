package com.github.an0rakdev.planetaryconquest.graphics.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public abstract class Model {
    private final FloatBuffer vertices;
    private final int nbOfVertices;

    public Model() {
        List<Float> coords = this.calculateCoordonates();
        this.nbOfVertices = coords.size() / 3;
        final ByteBuffer bb = ByteBuffer.allocateDirect(coords.size()
                * this.getVerticesStride());
        bb.order(ByteOrder.nativeOrder());
        this.vertices = bb.asFloatBuffer();
        for (final Float f : coords) {
            this.vertices.put(f);
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

    protected abstract List<Float> calculateCoordonates();
}
