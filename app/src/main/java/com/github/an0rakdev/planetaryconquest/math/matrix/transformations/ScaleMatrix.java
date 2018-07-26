package com.github.an0rakdev.planetaryconquest.math.matrix.transformations;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Scaling;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class ScaleMatrix extends GenericMatrix implements Scaling {
    public ScaleMatrix(int nbLin, int nbCol) {
        super(nbLin, nbCol);

    }

    @Override
    public void rescale(float x, float y, float z) {
        this.reset();
        Matrix.scaleM(this.values, 0, x, y ,z);
    }
}
