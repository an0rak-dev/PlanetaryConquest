package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
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

    public MVPShaderProgram(final Context context, final boolean useSeveralColors) {
        super(context);
        this.projectionMatrix = new Dim4Matrix();
        final Coordinates eyePosition = new Coordinates(0f, 0f, -3f);
        final Coordinates upPosition = new Coordinates(0f, 1f, 0f);
        this.viewMatrix = new CameraMatrix(4,4, eyePosition, upPosition);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        if (useSeveralColors) {
            this.addShader(R.raw.multicolor_fragment, GLES20.GL_FRAGMENT_SHADER);
        } else {
            this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        }
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
        final int colorHandler = this.applyColors(shape);
        // Apply the transformations matrix
        final GenericMatrix transformations = this.applyTransformations();
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
                transformations.getValues(), 0);
        // Draw
        this.render(shape, verticesHandle, colorHandler);
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

    final int applyColors(final Model shape) {
        if (shape.hasSeveralColors()) {
            final int colorHandler = GLES20.glGetAttribLocation(this.program, "vColors");
            GLES20.glEnableVertexAttribArray(colorHandler);
            final MultiColorModel multiColorShape = (MultiColorModel) shape;
            GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT,
                    false,
                    0,
                    multiColorShape.getColorsBuffer());
            return colorHandler;
        } else {
            final int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
            GLES20.glUniform4fv(colorHandle, 1, ((MonoColorModel)shape).getFragmentsColor(), 0);
            return -1;
        }
    }
}
