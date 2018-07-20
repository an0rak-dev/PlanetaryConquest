package com.github.an0rakdev.planetaryconquest.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.R;

public class MVPShaderProgram extends ShaderProgram {

    public MVPShaderProgram(final Context context) {
        super(context);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(Model shape) {
        GLES20.glUseProgram(this.program);
        // Apply the model's vertices and the color
        final int verticesHandle = this.applyVerticesAndColor(shape);
        // Apply the MVP Matrix
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        float[] mvpMatrix = this.applyModelViewProjection();
        final float[] scaleMatrix = new float[16];
        final float[] fullMatrix = new float[16];
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.setIdentityM(fullMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.5f, 0.5f, 0.5f);
        Matrix.multiplyMM(fullMatrix, 0, mvpMatrix, 0, scaleMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, fullMatrix, 0);
        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }

    private float[] applyModelViewProjection() {
        final float viewMatrix[] = new float[16];
        final float mvpMatrix[] = new float[16];
        Matrix.setLookAtM(viewMatrix, 0, 0, 0,-3,
                0f, 0f, 0f, 1f, 0f, 0f);
        Matrix.multiplyMM(mvpMatrix, 0, this.projectionMatrix, 0, viewMatrix, 0);
        return mvpMatrix;
    }

    private int applyVerticesAndColor(final Model shape) {
        // Add the vertices position to the shader's program.
        final int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT,
                false, 3 * shape.getVerticesStride(),
                shape.getVerticesBuffer());
        // Add the fragments' color to the shader's program.
        final int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getFragmentsColor(), 0);
        return positionHandle;
    }
}
