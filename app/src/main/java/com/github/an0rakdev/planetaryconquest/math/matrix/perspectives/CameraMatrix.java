package com.github.an0rakdev.planetaryconquest.math.matrix.perspectives;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class CameraMatrix extends GenericMatrix {
    private final Coordinates up;
    private Coordinates eye;
    private Coordinates center;
    public CameraMatrix(int nbLin, int nbCol, final Coordinates eye, final Coordinates up) {
        super(nbLin, nbCol);
        this.eye = eye;
        this.up = up;
        this.center = new Coordinates();
        this.recalculate();
    }

    public void setCenter(final Coordinates center) {
        this.center = center;
        this.recalculate();
    }

    public void moveForward(final float speed) {
        Coordinates centerWithSpeed = new Coordinates(
                speed * this.center.x,
                speed * this.center.y,
                speed * this.center.z);
        this.eye.x += centerWithSpeed.x;
        this.eye.y += centerWithSpeed.y;
        this.eye.z += centerWithSpeed.z;

        this.recalculate();
    }

    private void recalculate() {
        Matrix.setLookAtM(this.values, 0,
                this.eye.x, this.eye.y, this.eye.z,
                // This addition will made the 360° possible
                this.eye.x + center.x, this.eye.y + center.y, this.eye.z  + center.z,
                this.up.x, this.up.y, this.up.z);
    }
}
