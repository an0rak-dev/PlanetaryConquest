/* ****************************************************************************
 * OpenGLUtils.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

public class OpenGLUtils {
    private static final int DIMENSION = 3;
    private static final int FLOAT_BYTES = (Float.SIZE / Byte.SIZE);

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

    public static float[] toOpenGLColor(final float r, final float g, final float b) {
        final float[] result = new float[4];
        result[0] = r / 255f;
        result[1] = g / 255f;
        result[2] = b / 255f;
        result[3] = 1f;
        return result;
    }

    public static void clear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    public static void use(final int program) {
        GLES20.glUseProgram(program);
    }

    public static int bindVerticesToProgram(final int program, final FloatBuffer vertices, final String attribName) {
        int verticesHandle = GLES20.glGetAttribLocation(program, attribName);
        GLES20.glEnableVertexAttribArray(verticesHandle);
        GLES20.glVertexAttribPointer(verticesHandle, DIMENSION, GLES20.GL_FLOAT, false, 3 *FLOAT_BYTES, vertices);
        return verticesHandle;
    }

    public static void bindMVPToProgram(final int program, final float[] mvp, final String attribName) {
        final int mvpHandle = GLES20.glGetUniformLocation(program, attribName);
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvp, 0);
    }

    public static void setPointSizeOf(final int program, final float pointSize, final String attribName) {
        final int pointSizeHandle = GLES20.glGetUniformLocation(program, attribName);
        GLES20.glUniform1f(pointSizeHandle, pointSize);
    }

    public static void bindColorToProgram(final int program, final float[] color, final String attribName) {
        final int colorHandle = GLES20.glGetUniformLocation(program, attribName);
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
    }

    public static int bindColorToProgram(final int program, final FloatBuffer colors, final String attribName) {
        final int colorHandle = GLES20.glGetAttribLocation(program, attribName);
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colors);
        return colorHandle;
    }

    public static void drawPoints(final int count, final int... handlesToDisabled) {
        drawShape(GLES20.GL_POINTS, count, handlesToDisabled);
    }

    public static void drawTriangles(final int count, final int... handlesToDisabled) {
        drawShape(GLES20.GL_TRIANGLES, count, handlesToDisabled);
    }

    public static void drawLines(final int count, final float width, final int... handlesToDisabled) {
        GLES20.glLineWidth(width);
        drawShape(GLES20.GL_LINES, count, handlesToDisabled);
    }

    public static float[] randOpenGlColor() {
        return OpenGLUtils.toOpenGLColor(
                MathUtils.randRange(0f, 255f),
                MathUtils.randRange(0f, 255f),
                MathUtils.randRange(0f, 255f)
        );
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

    private static void drawShape(final int shapeType, final int count, final int... handlesToDisabled) {
        GLES20.glDrawArrays(shapeType, 0, count);
        for (final int handle : handlesToDisabled) {
            GLES20.glDisableVertexAttribArray(handle);
        }
    }
}
