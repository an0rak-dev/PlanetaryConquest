package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.MathUtils;

import java.nio.FloatBuffer;

public class StarsCloud extends Model {
    private final FloatBuffer buffer;
    private final int count;

    public StarsCloud(int count, int minX, int maxX, int minY, int maxY, int backZ, int frontZ) {
        buffer = this.createFloatBuffer(count * MathUtils.DIMENSION * MathUtils.FLOAT_BYTES);
        this.count = count;
        for (int i = 0; i < count / 2; i++) {
            buffer.put(MathUtils.randRange(minX, maxX));
            buffer.put(MathUtils.randRange(minY, maxY));
            buffer.put(frontZ);
            buffer.put(MathUtils.randRange(minX, maxX));
            buffer.put(MathUtils.randRange(minY, maxY));
            buffer.put(backZ);
        }
    }

    @Override
    public FloatBuffer bufferize() {
        buffer.position(0);
        return buffer;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public FloatBuffer colors() {
        return null;
    }
}
