package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.graphics.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public abstract class MultiColorModel extends Model {
    public static final int VERTEX_PER_TRIANGLE = 3;
    private FloatBuffer colorsBuffer;
    public MultiColorModel() {
        super();
        List<Color> colorsComponents = this.getColorsComponents();
        final ByteBuffer bb = ByteBuffer.allocateDirect(colorsComponents.size()
            * VERTEX_PER_TRIANGLE * Color.SIZE * Float.BYTES);
        bb.order(ByteOrder.nativeOrder());
        this.colorsBuffer = bb.asFloatBuffer();
        for (final Color component : colorsComponents) {
            for (int i = 0; i < VERTEX_PER_TRIANGLE; i++) {
                this.colorsBuffer.put(component.r);
                this.colorsBuffer.put(component.g);
                this.colorsBuffer.put(component.b);
                this.colorsBuffer.put(component.a);
            }
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

    /** One color per triangle, not per verticle */
    protected abstract List<Color> getColorsComponents();
}
