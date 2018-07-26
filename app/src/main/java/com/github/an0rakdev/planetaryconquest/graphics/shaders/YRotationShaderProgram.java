package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.transformations.RotationMatrix;
import com.github.an0rakdev.planetaryconquest.math.Rotation;

public class YRotationShaderProgram extends MVPShaderProgram {
    private final GenericMatrix rotationMatrix;

    public YRotationShaderProgram(Context context) {
        super(context);
        final Coordinates axis = new Coordinates(0, 1, 0);
        this.rotationMatrix = new RotationMatrix(4,4, axis);
    }

    @Override
    protected GenericMatrix applyTransformations() {
        final GenericMatrix mvpTransfo = super.applyTransformations();
        final GenericMatrix transformations = new Dim4Matrix();
        transformations.multiply(mvpTransfo, this.rotationMatrix);
        return transformations;
    }

    public void applyRotation(final float angle) {
        ((Rotation) this.rotationMatrix).rotate(angle);
    }
}
