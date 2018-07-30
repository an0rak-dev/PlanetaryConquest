package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.math.matrix.perspectives.CameraMatrix;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.google.vr.sdk.base.Eye;

public class VRShaderProgram extends MVPShaderProgram {
    private final GenericMatrix camera;
    private final GenericMatrix mvpMatrix;

    public VRShaderProgram(Context context) {
        super(context);
        this.mvpMatrix = new Dim4Matrix();
        // This represent your position in the VR world.
        // It needs to be updated when you move.
        final Coordinates eye = new Coordinates(0, 0, 2);
        final Coordinates up = new Coordinates(1, 0, 0);
        this.camera = new CameraMatrix(4,4, eye, up);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(final Model shape) {
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
        final GenericMatrix eyeView = new Dim4Matrix();
        final GenericMatrix eyePerspective = new Dim4Matrix(eye.getPerspective(0.1f, 100f));
        realView.multiply(eyeView, this.camera);
        this.mvpMatrix.multiply(eyePerspective, realView);
    }
}
