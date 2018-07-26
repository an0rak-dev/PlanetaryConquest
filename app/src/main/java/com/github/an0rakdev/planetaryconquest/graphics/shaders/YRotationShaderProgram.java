package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.graphics.Rotation;

public class YRotationShaderProgram extends MVPShaderProgram implements Rotation {
    private final float[] rotationMatrix;

    public YRotationShaderProgram(Context context) {
        super(context);
        this.rotationMatrix = this.createMatrix();
    }

    @Override
    protected float[] applyTransformations() {
        final float mvpTransfo[] = super.applyTransformations();
        final float transformations[] = this.createMatrix();
        Matrix.multiplyMM(transformations, 0, mvpTransfo, 0, this.rotationMatrix, 0);
        return transformations;
    }

    @Override
    public void rotate(float angle) {
        Matrix.setIdentityM(this.rotationMatrix, 0);
        Matrix.setRotateM(this.rotationMatrix, 0, angle, 0f, -1f, 0f);
    }
}
