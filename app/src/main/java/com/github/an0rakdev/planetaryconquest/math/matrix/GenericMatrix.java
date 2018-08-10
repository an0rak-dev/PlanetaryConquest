package com.github.an0rakdev.planetaryconquest.math.matrix;

import android.opengl.Matrix;

public abstract class GenericMatrix {
    protected final float values[];

    public GenericMatrix(final int nbLin, final int nbCol) {
        this.values = new float[nbLin * nbCol];
        this.reset();
    }

    public GenericMatrix(final float[] values) {
        this.values = values;
    }

    public final float[] getValues() {
        return this.values;
    }

    public final void multiply(final GenericMatrix m1, final GenericMatrix m2) {
        Matrix.multiplyMM(this.values, 0, m1.values, 0, m2.values, 0);
    }

    public final void translate(final float x, final float y, final float z) {
        Matrix.translateM(this.values, 0, x, y, z);
    }

    public void reset() {;
        Matrix.setIdentityM(this.values, 0);
    }
}
