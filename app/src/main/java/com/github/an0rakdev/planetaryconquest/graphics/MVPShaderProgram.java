package com.github.an0rakdev.planetaryconquest.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.R;

public class MVPShaderProgram extends ShaderProgram {
    private final float projectionMatrix[];

    public MVPShaderProgram(final Context context) {
        super(context);
        this.projectionMatrix = new float[16]; // 4 * 4 matrix
        Matrix.setIdentityM(this.projectionMatrix, 0);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    public void adaptToScene(final int sceneWidth, final int sceneHeight) {
        float sceneRatio = (float) sceneWidth / sceneHeight;
        Matrix.setIdentityM(this.projectionMatrix, 0);
        Matrix.frustumM(this.projectionMatrix, 0,
                -sceneRatio, sceneRatio, -1, 1, 3, 7);
    }

    @Override
    public void draw(Model shape) {
        GLES20.glUseProgram(this.program);
        // Apply the model's vertices and the color
        final int verticesHandle = this.applyVerticesAndColor(shape);
        // Apply the transformations matrix
        final float[] transformations = this.applyTransformations();
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, transformations, 0);
        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }

    protected float[] applyTransformations() {
        final float transformationMatrix[] = new float[16];
        final float viewMatrix[] = new float[16];
        Matrix.setIdentityM(transformationMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0,-3,
                0f, 0f, 0f, 1f, 0f, 0f);
        Matrix.multiplyMM(transformationMatrix, 0, this.projectionMatrix, 0, viewMatrix, 0);
        return transformationMatrix;
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
