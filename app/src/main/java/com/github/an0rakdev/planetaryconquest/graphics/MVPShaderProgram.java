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
        // Add the vertices position to the shader's program.
        final int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT,
                false, 3 * shape.getVerticesStride(),
                shape.getVerticesBuffer());
        // Add the fragments' color to the shader's program.
        final int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getFragmentsColor(), 0);
        // Apply the MVP Matrix
        this.applyModelViewProjection();
        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private void applyModelViewProjection() {
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        final float viewMatrix[] = new float[16];
        final float mvpMatrix[] = new float[16];
        Matrix.setLookAtM(viewMatrix, 0, 0, 0,-3,
                0f, 0f, 0f, 1f, 0f, 0f);
        Matrix.multiplyMM(mvpMatrix, 0, this.projectionMatrix, 0, viewMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
    }
}
