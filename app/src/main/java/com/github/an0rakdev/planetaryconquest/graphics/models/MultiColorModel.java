package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.graphics.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public abstract class MultiColorModel extends Model {
    private FloatBuffer colorsBuffer;
    public MultiColorModel() {
        super();
        List<Color> colorsComponents = this.getColorsComponents();
        final ByteBuffer bb = ByteBuffer.allocateDirect(colorsComponents.size()
            * Color.SIZE);
        bb.order(ByteOrder.nativeOrder());
        this.colorsBuffer = bb.asFloatBuffer();
        for (final Color component : colorsComponents) {
            this.colorsBuffer.put(component.r);
            this.colorsBuffer.put(component.g);
            this.colorsBuffer.put(component.b);
            this.colorsBuffer.put(component.a);
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

    protected abstract List<Color> getColorsComponents();
}
