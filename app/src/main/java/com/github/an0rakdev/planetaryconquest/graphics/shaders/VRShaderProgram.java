package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.google.vr.sdk.base.Eye;

public class VRShaderProgram extends ShaderProgram {
    private float[] eyeView;
    private float[] perspective;

    public VRShaderProgram(Context context) {
        super(context);
        this.eyeView = new float[16];
        Matrix.setIdentityM(this.eyeView, 0);
        this.perspective = new float[16];
        Matrix.setIdentityM(this.perspective, 0);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(final Model shape) {
        GLES20.glUseProgram(this.program);
        float mvpMatrix[] = new float[16];
        Matrix.setIdentityM(mvpMatrix, 0);

        final int verticesHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(verticesHandle);
        GLES20.glVertexAttribPointer(verticesHandle, 3, GLES20.GL_FLOAT,
                false, 3 * shape.getVerticesStride(),
                shape.getVerticesBuffer());
        // Add the fragments' color to the shader's program.
        final int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getFragmentsColor(), 0);
        // Set the camera (ie. the eye)
        final float camera[] = new float[16];
        Matrix.setIdentityM(camera, 0);
        Matrix.setLookAtM(camera, 0,
                0.0f, 0.0f, -2f,
                0.0f, 0.0f, 0.0f,
                1f, 0.0f, 0.0f);
        final float realView[] = new float[16];
        Matrix.setIdentityM(realView, 0);
        Matrix.multiplyMM(realView, 0, this.eyeView, 0, camera, 0);
        final float fullMvp[] = new float[16];
        Matrix.setIdentityM(fullMvp, 0);
        Matrix.multiplyMM(fullMvp, 0, this.perspective, 0, realView, 0);
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, fullMvp, 0);
        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }

    public void adaptToEye(final Eye eye) {
        this.eyeView = eye.getEyeView();
        this.perspective = eye.getPerspective(0.1f, 100f);
    }
}
