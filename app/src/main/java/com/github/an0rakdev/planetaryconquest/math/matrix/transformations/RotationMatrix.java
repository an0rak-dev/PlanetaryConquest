package com.github.an0rakdev.planetaryconquest.math.matrix.transformations;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.Rotation;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class RotationMatrix extends GenericMatrix implements Rotation {
    private final Coordinates axis;
    public RotationMatrix(int nbLin, int nbCol, final Coordinates axis) {
        super(nbLin, nbCol);
        this.axis = axis;
    }

    @Override
    public void rotate(float angle) {
        this.reset();
        Matrix.setRotateM(this.values, 0, angle,
                this.axis.x, this.axis.y, this.axis.z);
    }
}
