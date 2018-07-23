package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.graphics.Scaling;

public class ScaleShaderProgram extends MVPShaderProgram implements Scaling {
    private final float scaleMatrix[];

    public ScaleShaderProgram(final Context context) {
        super(context);
        this.scaleMatrix = new float[16];
        Matrix.setIdentityM(this.scaleMatrix, 0);
    }

    @Override
    protected float[] applyTransformations() {
        final float mvpTransfo[] = super.applyTransformations();
        final float transformations[] = new float[16];
        Matrix.setIdentityM(transformations, 0);
        Matrix.multiplyMM(transformations, 0, mvpTransfo, 0, this.scaleMatrix, 0);
        return transformations;
    }

    @Override
    public void rescale(float x, float y, float z) {
        Matrix.setIdentityM(this.scaleMatrix, 0);
        Matrix.scaleM(this.scaleMatrix, 0, x, y ,z);
    }
}
