package com.github.an0rakdev.planetaryconquest.graphics;

import android.opengl.GLES20;
import android.util.Log;

public class OpenGLUtils {

    private static final String TAG = "OpenGLUtils";

    public static int addVertexShaderToProgram(final String source, final int program) {
        final int vertexShader = compileShader(source, GLES20.GL_VERTEX_SHADER, program);
        if (0 == vertexShader) {
            logOpenGLError("Unable to add the vertex shader to the program.");
        }
        return vertexShader;
    }

    public static int addFragmentShaderToProgram(final String source, final int program) {
        final int fragmentShader = compileShader(source, GLES20.GL_FRAGMENT_SHADER, program);
        if (0 == fragmentShader) {
            logOpenGLError("Unable to add the fragment shader to the program.");
        }
        return fragmentShader;
    }

    public static void linkProgram(final int program, final int... shaders) {
        final int[] status = new int[1];
        GLES20.glLinkProgram(program);
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if (GLES20.GL_FALSE == status[0]) {
            logOpenGLError("Unable to link the program.");
        }
        for (final int shader : shaders) {
            GLES20.glDetachShader(program, shader);
            GLES20.glDeleteShader(shader);
        }
    }

    public static int newProgram() {
        final int result = GLES20.glCreateProgram();
        if (0 == result) {
            logOpenGLError("Unable to create a new program");
        }
        return result;
    }

    private static int compileShader(final String source, final int type, final int program) {
        final int[] status = new int[1];
        final int shader = GLES20.glCreateShader(type);
        if (0 == shader) {
            return 0;
        }
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (GLES20.GL_FALSE == status[0]) {
            Log.e(TAG, "Unable to compile the shader ! ");
            Log.e(TAG, "OpenGL Error Code : "+ GLES20.glGetError());
            return 0;
        }
        GLES20.glAttachShader(program, shader);
        return shader;
    }

    private static void logOpenGLError(final String message) {
        Log.e(TAG, message);
        Log.e(TAG, "OpenGL error code : " + GLES20.glGetError());
    }
}
