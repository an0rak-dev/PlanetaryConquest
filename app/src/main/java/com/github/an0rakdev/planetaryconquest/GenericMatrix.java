package com.github.an0rakdev.planetaryconquest;

import android.opengl.Matrix;

public class GenericMatrix {
    private final float values[];

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

    public final void changeToFrustumPerspective(final float ratio) {
        this.reset();
        Matrix.frustumM(this.values, 0, -ratio, ratio, -1, 1,
                3, 10);
    }

    public final void changeToCamera(final Coordinates eye, final Coordinates center,
                                     final Coordinates up) {
        Matrix.setLookAtM(this.values, 0,
                eye.x, eye.y, eye.z,
                center.x, center.y, center.z,
                up.x, up.y, up.z);
    }

    public final void changeToScale(final float x, final float y, final float z) {
        this.reset();
        Matrix.scaleM(this.values, 0, x, y ,z);
    }

    public final void changeToRotation(final float angle, final Coordinates axis) {
        this.reset();
        Matrix.setRotateM(this.values, 0, angle, axis.x, axis.y, axis.z);
    }

    public final void multiply(final GenericMatrix m1, final GenericMatrix m2) {
        Matrix.multiplyMM(this.values, 0, m1.values, 0, m2.values, 0);
    }

    public void reset() {
        Matrix.setIdentityM(this.values, 0);
    }
}
