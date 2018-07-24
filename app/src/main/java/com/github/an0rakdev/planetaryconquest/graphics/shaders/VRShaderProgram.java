package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.google.vr.sdk.base.Eye;

public class VRShaderProgram extends MVPShaderProgram {
    private final float[] camera;
    private final float[] mvpMatrix;

    public VRShaderProgram(Context context) {
        super(context);
        this.camera = this.createMatrix();
        this.mvpMatrix = this.createMatrix();
        // This represent your position in the VR world.
        // It needs to be updated when you move.
        Matrix.setLookAtM(this.camera, 0,
                0.0f, 0.0f, 2f,
                0.0f, 0.0f, 0.0f,
                1f, 0.0f, 0.0f);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(final Model shape) {
        GLES20.glUseProgram(this.program);
        final int verticesHandle = this.applyVerticesAndColor(shape);
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, this.mvpMatrix, 0);
        // Draw
        this.render(shape, verticesHandle);
    }

    public void adaptToEye(final Eye eye) {
        Matrix.setIdentityM(this.mvpMatrix, 0);
        final float realView[] = this.createMatrix();
        Matrix.multiplyMM(realView, 0, eye.getEyeView(), 0, camera, 0);
        Matrix.multiplyMM(this.mvpMatrix,
                0, eye.getPerspective(0.1f, 100f), 0, realView, 0);
    }
}
