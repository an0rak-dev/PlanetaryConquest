package com.github.an0rakdev.planetaryconquest;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderProgram {
    private static final String TAG = "ShaderProgram";
    private int program;

    public ShaderProgram() {
        this.program = GLES20.glCreateProgram();
    }

    public void addShader(final String source, final int type, final String shaderName) {
        int shader = GLES20.glCreateShader(type);
        if (0 == shader) {
            Log.e(TAG, "Can't create the " + shaderName + " shader");
            return;
        }
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int compileStatus[] = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (GLES20.GL_FALSE == compileStatus[0]) {
            Log.e(TAG, "Can't compile the " + shaderName + " shader.");
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return;
        }
        GLES20.glAttachShader(this.program, shader);
    }

    public void draw(final Triangle shape) {
        GLES20.glLinkProgram(this.program);
        GLES20.glUseProgram(this.program);
        // Add the vertices position to the shader's program.
        int positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT,
                false, 3 * shape.getVerticesStride(),
                shape.getVerticesBuffer());
        // Add the fragments' color to the shader's program.
        int colorHandle = GLES20.glGetAttribLocation(this.program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, shape.getFragmentsColor(), 0);
        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
