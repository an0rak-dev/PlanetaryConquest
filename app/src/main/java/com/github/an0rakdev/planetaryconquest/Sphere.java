package com.github.an0rakdev.planetaryconquest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

class Sphere {
    private static final int LATITUDE_PORTION = 30;
    private static final int LONGITUDE_PORTION = 30;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer normalBuffer;
    private ShortBuffer drawOrderBuffer;

    Sphere(final int radius) {
        final float latFrac = (float)Math.PI/(float)LATITUDE_PORTION;
        final float longFrac = 2f * (float)Math.PI/(float)LONGITUDE_PORTION;
        final List<Float> normalData = new ArrayList<>();
        final List<Float> textureData = new ArrayList<>();
        final List<Float> vertexData = new ArrayList<>();
        final List<Integer> drawOrder = new ArrayList<>();

        // Calculate datas of normals, textures and vertex.
        for (int lat = 0; lat < LATITUDE_PORTION; lat++) {
            final float theta = (float)lat * latFrac;
            final float sinTheta = (float) Math.sin(theta);
            final float cosTheta = (float) Math.sin(theta);
            for (int lon = 0; lon < LONGITUDE_PORTION; lon++) {
                final float phi = (float)lon * longFrac;
                final float sinPhi = (float) Math.sin(phi);
                final float cosPhi = (float) Math.cos(phi);
                final float fracX = sinTheta * cosPhi;
                final float fracY = cosTheta;
                final float fracZ = sinPhi * sinTheta;
                normalData.add(fracX);
                normalData.add(fracY);
                normalData.add(fracZ);
                textureData.add(1 - ((float) lon / (float)LONGITUDE_PORTION));
                textureData.add(1 - ((float) lat / (float)LATITUDE_PORTION));
                vertexData.add(fracX * radius);
                vertexData.add(fracY * radius);
                vertexData.add(fracZ * radius);

                final int firstIndice = lat * (LONGITUDE_PORTION+1) + lon;
                final int secondIndice = firstIndice + LONGITUDE_PORTION + 1;
                drawOrder.add(firstIndice);
                drawOrder.add(secondIndice);
                drawOrder.add(firstIndice + 1);
                drawOrder.add(secondIndice);
                drawOrder.add(secondIndice + 1);
                drawOrder.add(firstIndice + 1);
            }
        }
        this.vertexBuffer = this.bufferize(vertexData);
        this.textureBuffer = this.bufferize(textureData);
        this.normalBuffer = this.bufferize(normalData);
        ByteBuffer bb = ByteBuffer.allocateDirect(drawOrder.size() * Short.SIZE);
        bb.order(ByteOrder.nativeOrder());
        this.drawOrderBuffer = bb.asShortBuffer();
        for (final Integer s : drawOrder) {
            this.drawOrderBuffer.put(s.shortValue());
        }
        this.drawOrderBuffer.position(0);
    }

    private FloatBuffer bufferize(final List<Float> floatValues) {
        ByteBuffer bb = ByteBuffer.allocateDirect(floatValues.size() * Float.SIZE);
        bb.order(ByteOrder.nativeOrder());
        final FloatBuffer result = bb.asFloatBuffer();
        for (final Float f : floatValues) {
            result.put(f.floatValue());
        }
        result.position(0);
        return result;
    }
}
