package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.graphics.Scaling;

public class ScaleShaderProgram extends MVPShaderProgram implements Scaling {
    private final GenericMatrix scaleMatrix;

    public ScaleShaderProgram(final Context context) {
        super(context);
        this.scaleMatrix = new Dim4Matrix();
    }

    @Override
    protected GenericMatrix applyTransformations() {
        final GenericMatrix mvpTransfo = super.applyTransformations();
        final GenericMatrix transformations = new Dim4Matrix();
        transformations.multiply(mvpTransfo, this.scaleMatrix);
        return transformations;
    }

    @Override
    public void rescale(float x, float y, float z) {
        this.scaleMatrix.changeToScale(x,y,z);
    }
}
