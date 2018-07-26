package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.math.matrix.perspectives.CameraMatrix;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.perspectives.FrustumPerspectiveMatrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;

public class MVPShaderProgram extends ShaderProgram {
    private GenericMatrix projectionMatrix;
    private final GenericMatrix viewMatrix;

    public MVPShaderProgram(final Context context) {
        super(context);
        final Coordinates eyePosition = new Coordinates(0, 0, -3);
        final Coordinates upPosition = new Coordinates(0, 1, 0);
        this.viewMatrix = new CameraMatrix(4,4, eyePosition, upPosition);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    public void adaptToScene(final int sceneWidth, final int sceneHeight) {
        float sceneRatio = (float) sceneWidth / sceneHeight;
        this.projectionMatrix = new FrustumPerspectiveMatrix(4,4, sceneRatio);
    }

    @Override
    public void draw(Model shape) {
        GLES20.glUseProgram(this.program);
        // Apply the model's vertices and the color
        final int verticesHandle = this.applyVertices(shape);
        this.applyColors(shape);
        // Apply the transformations matrix
        final GenericMatrix transformations = this.applyTransformations();
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
                transformations.getValues(), 0);
        // Draw
        this.render(shape, verticesHandle);
    }

    protected GenericMatrix applyTransformations() {
        final GenericMatrix transformationMatrix = new Dim4Matrix();
        transformationMatrix.multiply(this.projectionMatrix, this.viewMatrix);
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
