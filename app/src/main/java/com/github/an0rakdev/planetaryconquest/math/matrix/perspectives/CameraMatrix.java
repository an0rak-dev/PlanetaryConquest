package com.github.an0rakdev.planetaryconquest.math.matrix.perspectives;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class CameraMatrix extends GenericMatrix {
    public CameraMatrix(int nbLin, int nbCol, final Coordinates eye, final Coordinates up) {
        super(nbLin, nbCol);
        Matrix.setLookAtM(this.values, 0,
                eye.x, eye.y, eye.z,
                0f,0f,0f,
                up.x, up.y, up.z);
    }

    public void translate(final float x, final float y, final float z) {
        Matrix.translateM(this.values, 0, x, y, z);
    }
}
