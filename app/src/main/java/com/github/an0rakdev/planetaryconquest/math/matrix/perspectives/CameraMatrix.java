package com.github.an0rakdev.planetaryconquest.math.matrix.perspectives;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;

public class CameraMatrix extends GenericMatrix {
    private final Coordinates up;
    private Coordinates position;
    private Coordinates lookAt;
    public CameraMatrix(int nbLin, int nbCol, final Coordinates position, final Coordinates up) {
        super(nbLin, nbCol);
        this.position = position;
        this.up = up;
        this.lookAt = new Coordinates();
        this.recalculate();
    }

    public void setLookAt(final Coordinates center) {
        this.lookAt = center;
        this.recalculate();
    }

    public Coordinates getLookAt() {
        return lookAt;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void moveForward(final float speed) {
        Coordinates centerWithSpeed = new Coordinates(
                speed * this.lookAt.x,
                speed * this.lookAt.y,
                speed * this.lookAt.z);
        this.position.x += centerWithSpeed.x;
        this.position.y += centerWithSpeed.y;
        this.position.z += centerWithSpeed.z;

        this.recalculate();
    }

    private void recalculate() {
        Matrix.setLookAtM(this.values, 0,
                this.position.x, this.position.y, this.position.z,
                // This addition will made the 360° possible
                this.position.x + lookAt.x, this.position.y + lookAt.y, this.position.z  + lookAt.z,
                this.up.x, this.up.y, this.up.z);
    }
}
