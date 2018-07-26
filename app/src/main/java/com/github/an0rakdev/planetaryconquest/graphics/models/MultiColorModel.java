package com.github.an0rakdev.planetaryconquest.graphics.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public abstract class MultiColorModel extends Model {
    private FloatBuffer colorsBuffer;
    public MultiColorModel() {
        super();
        List<Float> colorsComponents = this.getColorsComponents();
        final ByteBuffer bb = ByteBuffer.allocateDirect(colorsComponents.size()
            * Float.BYTES);
        bb.order(ByteOrder.nativeOrder());
        this.colorsBuffer = bb.asFloatBuffer();
        for (final Float component : colorsComponents) {
            this.colorsBuffer.put(component);
        }
        this.colorsBuffer.position(0);
    }
    @Override
    public boolean hasSeveralColors() {
        return true;
    }

    public int getColorsStride() {
        return 4;
    }

    public FloatBuffer getColorsBuffer() {
        return this.colorsBuffer;
    }

    protected abstract List<Float> getColorsComponents();
}
