package com.github.an0rakdev.planetaryconquest.math.matrix.perspectives;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class FrustumPerspectiveMatrix extends GenericMatrix {
    public FrustumPerspectiveMatrix(final int nbLin, final int nbCol, final float ratio) {
        super(nbLin, nbCol);
        this.reset();
        Matrix.frustumM(this.values, 0, -ratio, ratio, -1, 1,
                3, 10);
    }
}
