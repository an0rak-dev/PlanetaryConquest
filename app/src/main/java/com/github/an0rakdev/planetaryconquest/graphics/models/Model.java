package com.github.an0rakdev.planetaryconquest.graphics.models;

import java.nio.FloatBuffer;

public abstract class Model {
    /**
     * @return the vertices byte buffer for this shape.
     */
    public abstract FloatBuffer getVerticesBuffer();

    /**
     * @return the number of vertices used for drawing this model.
     */
    public abstract int getNbOfElements();
}
