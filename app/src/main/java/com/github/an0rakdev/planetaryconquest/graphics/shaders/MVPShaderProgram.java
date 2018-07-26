package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MVPShaderProgram extends ShaderProgram {
    private final float projectionMatrix[];

    public MVPShaderProgram(final Context context) {
        super(context);
        this.projectionMatrix = this.createMatrix();
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    public void adaptToScene(final int sceneWidth, final int sceneHeight) {
        float sceneRatio = (float) sceneWidth / sceneHeight;
        Matrix.setIdentityM(this.projectionMatrix, 0);
        Matrix.frustumM(this.projectionMatrix, 0,
                -sceneRatio, sceneRatio, -1, 1, 3, 100);
    }

    @Override
    public void draw(Model shape) {
        GLES20.glUseProgram(this.program);
        // Apply the model's vertices and the color
        final int verticesHandle = this.applyVertices(shape);
        this.applyColors(shape);
        // Apply the transformations matrix
        final float[] transformations = this.applyTransformations();
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, transformations, 0);

        // Draw
        this.render(shape, verticesHandle);
    }

    protected float[] applyTransformations() {
        final float transformationMatrix[] = this.createMatrix();
        final float viewMatrix[] = this.createMatrix();
        Matrix.setLookAtM(viewMatrix, 0, 0, 0,-3,
                0f, 0f, 0f, 0f, 1f, 0f);
        Matrix.multiplyMM(transformationMatrix, 0, this.projectionMatrix, 0, viewMatrix, 0);
        return transformationMatrix;
    }

    final int applyVertices(final Model shape) {
        // Add the vertices position to the shader's program.
        final int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT,
                false, 3 * shape.getVerticesStride(),
                shape.getVerticesBuffer());
        return positionHandle;
    }

    private final void applyColors(final Model shape) {
        final int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getFragmentsColor(), 0);
    }
}
