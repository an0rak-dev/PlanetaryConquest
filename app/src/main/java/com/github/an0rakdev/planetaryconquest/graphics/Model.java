package com.github.an0rakdev.planetaryconquest.graphics;

import java.nio.FloatBuffer;

public interface Model {
    /**
     * @return the number of bytes for one vertex in this model.
     */
    int getVerticesStride();

    /**
     * @return the vertices byte buffer for this shape.
     */
    FloatBuffer getVerticesBuffer();

    /**
     * @return the colors to use for those fragments (one color = 4 entries in the array).
     */
    float[] getFragmentsColor();

    /**
     * @return the number of vertices used for drawing this model.
     */
    int getNbOfVertices();
}
