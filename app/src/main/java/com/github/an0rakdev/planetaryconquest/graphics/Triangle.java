package com.github.an0rakdev.planetaryconquest.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Triangle implements Model {
    private FloatBuffer vertices;
    private int nbOfVertices;

    public Triangle() {
        final List<Float> coords = new ArrayList<>();
        final Float z= 0f;
        final Float y= 0.5f;
        // First point
        coords.add(0.0f);
        coords.add(y);
        coords.add(z);
        // Second point
        coords.add(-0.5f);
        coords.add(y * -0.5f);
        coords.add(z);
        // Third point
        coords.add(0.5f);
        coords.add(y * -0.5f);
        coords.add(z);
        this.nbOfVertices = coords.size() / 3;
        final ByteBuffer bb = ByteBuffer.allocateDirect(coords.size()
                * this.getVerticesStride());
        bb.order(ByteOrder.nativeOrder());
        this.vertices = bb.asFloatBuffer();
        for (final Float f : coords) { this.vertices.put(f.floatValue()); }
        this.vertices.position(0);
    }

    @Override
    public FloatBuffer getVerticesBuffer() {
        return this.vertices;
    }

    @Override
    public int getVerticesStride() {
        return (Float.SIZE / Byte.SIZE);
    }

    @Override
    public int getNbOfVertices() {
        return this.nbOfVertices;
    }

    @Override
    public float[] getFragmentsColor() {
        return new float[]{
          0.636f, 0.795f, 0.222f, 1f
        };
    }
}
