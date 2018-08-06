package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.perspectives.CameraMatrix;
import com.google.vr.sdk.base.Eye;

public class VRShaderProgram extends MVPShaderProgram {
    private final CameraMatrix camera;
    private final GenericMatrix mvpMatrix;

    public VRShaderProgram(Context context) {
        super(context);
        this.mvpMatrix = new Dim4Matrix();
        // This represent your position in the VR world.
        // It needs to be updated when you translate.
        final Coordinates eye = new Coordinates(0, 0, 2);
        final Coordinates up = new Coordinates(0, 1, 0);
        this.camera = new CameraMatrix(4,4, eye, up);
    }

    @Override
    public void draw(final TriangleBasedModel shape) {
        GLES20.glUseProgram(this.program);
        final int verticesHandle = this.applyVertices(shape);
        final int colorHandle = this.applyColors(shape);
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
                this.mvpMatrix.getValues(), 0);
        // Draw
        this.render(shape, verticesHandle, colorHandle);
    }

    public void adaptToEye(final Eye eye) {
        this.mvpMatrix.reset();
        final GenericMatrix realView = new Dim4Matrix();
        final GenericMatrix eyeView = new Dim4Matrix(eye.getEyeView());
        final GenericMatrix eyePerspective = new Dim4Matrix(eye.getPerspective(0.1f, 100f));
        realView.multiply(eyeView, this.camera);
        this.mvpMatrix.multiply(eyePerspective, realView);
    }

    public void moveCameraOf(final float x, final float y, final float z) {
        this.camera.translate(x, y, z);
    }
}
