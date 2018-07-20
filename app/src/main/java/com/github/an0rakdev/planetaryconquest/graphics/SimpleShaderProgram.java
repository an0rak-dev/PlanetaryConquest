package com.github.an0rakdev.planetaryconquest.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleShaderProgram extends ShaderProgram {
    private static final String TAG = "SimpleShaderProgram";

    public SimpleShaderProgram(final Context context) {
        super(context);
        this.addShader(R.raw.simple_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(final Model shape) {
        GLES20.glUseProgram(this.program);
        // Add the vertices position to the shader's program.
        int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT,
                false, 3 * shape.getVerticesStride(),
                shape.getVerticesBuffer());
        // Add the fragments' color to the shader's program.
        int colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getFragmentsColor(), 0);
        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
