package com.github.an0rakdev.planetaryconquest.graphics.shaders.transformations;

import android.content.Context;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.transformations.ScaleMatrix;
import com.github.an0rakdev.planetaryconquest.math.Scaling;

public class ScaleShaderProgram extends MVPShaderProgram {
    private final GenericMatrix scaleMatrix;

    public ScaleShaderProgram(final Context context, final boolean useSeveralColors) {
        super(context, useSeveralColors);
        this.scaleMatrix = new ScaleMatrix(4,4);
    }

    @Override
    public void draw(Model shape) {
        super.draw(shape);
    }

    @Override
    protected GenericMatrix applyTransformations() {
        final GenericMatrix mvpTransfo = super.applyTransformations();
        final GenericMatrix transformations = new Dim4Matrix();
        transformations.multiply(mvpTransfo, this.scaleMatrix);
        return transformations;
    }

    public void setScaleTo(float x, float y, float z) {
        ((Scaling) this.scaleMatrix).rescale(x,y,z);
    }
}
