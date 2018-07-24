package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram {
    private static final String TAG = "ShaderProgram";
    final int program;
    final Context context;
    private final List<Integer> shaders;

    ShaderProgram(final Context context) {
        this.context = context;
        this.shaders = new ArrayList<>();
        this.program = GLES20.glCreateProgram();

    }

    final void addShader(final int shaderFd, final int type) {
        final String source = this.readContentOf(shaderFd);
        if (source.isEmpty()) { return; }
        int shader = GLES20.glCreateShader(type);
        if (0 == shader) {
            Log.e(TAG, "Can't create the shader of type " + type);
            return;
        }
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int compileStatus[] = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (GLES20.GL_FALSE == compileStatus[0]) {
            Log.e(TAG, "Can't compile the shader of type " + type);
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return;
        }
        this.shaders.add(shader);
    }

    final void prepare() {
        for (final Integer shader : this.shaders) {
            GLES20.glAttachShader(this.program, shader);
        }
        GLES20.glLinkProgram(this.program);
    }

    public abstract void draw(final Model shape);

    private String readContentOf(final int fd) {
        final InputStream inputStream = this.context.getResources().openRawResource(fd);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder contentSb = new StringBuilder();
        final String lineSep = System.getProperty("line.separator");
        try {
            String line = reader.readLine();
            while (null != line) {
                contentSb.append(line).append(lineSep);
                line = reader.readLine();
            }
            reader.close();
        } catch (final IOException ex) {
            Log.e(TAG, "Unable to read the content of " + fd);
        }
        return contentSb.toString();
    }

    final void render(final Model shape, final int verticesHandle) {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.getNbOfVertices());
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }

    final float[] createMatrix() {
        final float matrix[] = new float[16];
        Matrix.setIdentityM(matrix, 0);
        return matrix;
    }
}
