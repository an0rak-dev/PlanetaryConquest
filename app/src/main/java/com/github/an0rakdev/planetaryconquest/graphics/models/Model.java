package com.github.an0rakdev.planetaryconquest.graphics.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A Vertices based model as float coordinates.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class Model {
	/** The size of a Float in Bytes. */
	public static final int FLOAT_BYTE_SIZE = (Float.SIZE) / Byte.SIZE;
    public static final int COLOR_SIZE = 4;

    /**
	 * Returns the buffer of vertices coordinates for the model.
	 *
	 * A coordinates will be represented in X entries (one per dimension).
	 *
     * @return the vertices float buffer for the implemented model.
     */
    public abstract FloatBuffer bufferize();

    /**
	 * Returns the numbers of vertices of this model.
	 *
     * @return the number of vertices used for drawing this model.
     */
    public abstract int size();

    /**
     * Create and return a float buffer which contains the colors of each vertices in
     * the same order as <code>bufferize</code>.
     *
     * A background is represented has 4 consecutive values in the resulting buffers (rgba)
     * so the total size of this buffer will be :
     * <code>this.getSize() * 4 * FLOAT_BYTE_SIZE</code>.
     *
     * @return the bufferized colors to apply to this model.
     */
    public abstract FloatBuffer colors();

    protected FloatBuffer createFloatBuffer(final int size) {
        final ByteBuffer bb = ByteBuffer.allocateDirect(size);
        bb.order(ByteOrder.nativeOrder());
        return bb.asFloatBuffer();
    }
}
