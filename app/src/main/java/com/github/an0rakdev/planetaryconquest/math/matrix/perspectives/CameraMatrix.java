package com.github.an0rakdev.planetaryconquest.math.matrix.perspectives;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class CameraMatrix extends GenericMatrix {
    public CameraMatrix(int nbLin, int nbCol, final Coordinates eye, final Coordinates up) {
        super(nbLin, nbCol);
        Matrix.setLookAtM(this.values, 0,
                eye.x, eye.y, eye.z,
                0,0,0,
                up.x, up.y, up.z);
    }
}
