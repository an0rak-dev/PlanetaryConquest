package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.PointBasedModel;

public class PointShaderProgram extends ShaderProgram<PointBasedModel> {
    public PointShaderProgram(final Context context) {
        super(context);
        this.drawType = GLES20.GL_POINTS;
        this.addShader(R.raw.point_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(final PointBasedModel shape) {
        GLES20.glUseProgram(this.program);
        // Apply the model's vertices and the color
        final int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT,
                false, 3,
                shape.getVerticesBuffer());
        int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getColor(), 0);

        int pointHandler = GLES20.glGetUniformLocation(this.program, "vPointSize");
        GLES20.glUniform1f(pointHandler, 1);
        // Draw
        this.render(shape, positionHandle, -1);
    }
}
