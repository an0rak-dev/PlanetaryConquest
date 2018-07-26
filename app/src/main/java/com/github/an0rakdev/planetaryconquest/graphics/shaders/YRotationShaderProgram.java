package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.Coordinates;
import com.github.an0rakdev.planetaryconquest.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.graphics.Rotation;

public class YRotationShaderProgram extends MVPShaderProgram implements Rotation {
    private final GenericMatrix rotationMatrix;

    public YRotationShaderProgram(Context context) {
        super(context);
        this.rotationMatrix = new Dim4Matrix();
    }

    @Override
    protected GenericMatrix applyTransformations() {
        final GenericMatrix mvpTransfo = super.applyTransformations();
        final GenericMatrix transformations = new Dim4Matrix();
        transformations.multiply(mvpTransfo, this.rotationMatrix);
        return transformations;
    }

    @Override
    public void rotate(float angle) {
        final Coordinates axis = new Coordinates(0, 1, 0);
        this.rotationMatrix.changeToRotation(angle, axis);
    }
}
