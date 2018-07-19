package com.github.an0rakdev.planetaryconquest.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram {
    private static final String TAG = "ShaderProgram";
    final int program;
    final Context context;
    private List<Integer> shaders;

    public ShaderProgram(final Context context) {
        this.context = context;
        this.shaders = new ArrayList<>();
        this.program = GLES20.glCreateProgram();
    }

    final void addShader(final String source, final int type) {
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

    public abstract void draw(final Triangle shape);
}
