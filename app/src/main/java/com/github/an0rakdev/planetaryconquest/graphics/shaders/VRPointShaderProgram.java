package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.perspectives.CameraMatrix;
import com.google.vr.sdk.base.Eye;

public class VRPointShaderProgram extends PointShaderProgram {
    private final CameraMatrix camera;

    public VRPointShaderProgram(Context context, int pointSize) {
        super(context, pointSize);
        final Coordinates eye = new Coordinates(0, 0, 0);
        final Coordinates up = new Coordinates(0, 1, 0);
        this.camera = new CameraMatrix(4,4, eye, up);
        this.camera.setLookAt(new Coordinates(0f, 0f, 1f));
    }

    public void adaptToEye(final Eye eye) {
        this.mvpMatrix.reset();
        final GenericMatrix realView = new Dim4Matrix();
        final GenericMatrix eyeView = new Dim4Matrix(eye.getEyeView());
        final GenericMatrix eyePerspective = new Dim4Matrix(eye.getPerspective(0.1f, 100f));
        realView.multiply(eyeView, this.camera);
        this.mvpMatrix.multiply(eyePerspective, realView);
    }
}
