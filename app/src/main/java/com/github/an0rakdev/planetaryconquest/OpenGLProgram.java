package com.github.an0rakdev.planetaryconquest;

import android.opengl.GLES20;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;

public class OpenGLProgram {
    public enum DrawType {
        TRIANGLES,
        LINES,
        POINTS
    };

    private final int program;
    private final DrawType type;
    private String mvpAttribName;
    private String verticesAttribName;
    private String colorAttribName;

    public OpenGLProgram(DrawType type) {
        this.program =  GLES20.glCreateProgram();
        this.type = type;
        if (0 == this.program) {
            throw new IllegalStateException("Unable to create a new OpenGL program ! ");
        }
    }

    public void compile(String vertexShader, String fragmentShader) {
        final int vHandle = this.compileShader(vertexShader, GLES20.GL_VERTEX_SHADER);
        final int cHandle = this.compileShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER);
        final int[] status = new int[1];
        GLES20.glLinkProgram(this.program);
        GLES20.glGetProgramiv(this.program, GLES20.GL_LINK_STATUS, status, 0);
        if (GLES20.GL_FALSE == status[0]) {
            this.logError("Unable to link the program.");
        }
        GLES20.glDetachShader(this.program, vHandle);
        GLES20.glDetachShader(this.program, cHandle);
        GLES20.glDeleteShader(vHandle);
        GLES20.glDeleteShader(cHandle);
    }

    public void activate() {
        GLES20.glUseProgram(this.program);
    }

    public void setAttributesNames(String mvp, String vertices, String fragments) {
        this.mvpAttribName = mvp;
        this.verticesAttribName = vertices;
        this.colorAttribName = fragments;
    }

    public void useMVP(float[] modelViewProjection) {
        final int mvpHandle = GLES20.glGetUniformLocation(this.program, this.mvpAttribName);
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, modelViewProjection, 0);
    }

    public void draw(Model shape) {
        this.draw(shape, null);
    }

    public void draw(Model shape, final float[] singleColor) {
        final int vHandle = OpenGLUtils.bindVerticesToProgram(this.program, shape.bufferize(), this.verticesAttribName);
        int cHandle = -1;
        int type = 0;
        switch (this.type) {
            case TRIANGLES:
                type = GLES20.GL_TRIANGLES;
                break;
            case POINTS :
                type = GLES20.GL_POINTS;
                break;
        }
        if (null == singleColor) {
            cHandle = OpenGLUtils.bindColorToProgram(this.program, shape.colors(), this.colorAttribName);
        } else {
            OpenGLUtils.bindColorToProgram(this.program, singleColor, this.colorAttribName);
        }
        GLES20.glDrawArrays(type, 0, shape.size());

        if (-1 < cHandle) {
            GLES20.glDisableVertexAttribArray(cHandle);
        }
        GLES20.glDisableVertexAttribArray(vHandle);
    }

    public void passValue(String attribName, float value) {
        final int valueHandle = GLES20.glGetUniformLocation(this.program, attribName);
        GLES20.glUniform1f(valueHandle, value);
    }

    private int compileShader(String shaderSource, int shaderType) {
        final int[] status = new int[1];
        final int shader = GLES20.glCreateShader(shaderType);
        if (0 == shader) {
            return 0;
        }
        GLES20.glShaderSource(shader, shaderSource);
        GLES20.glCompileShader(shader);
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (GLES20.GL_FALSE == status[0]) {
            this.logError("Unable to compile the shader !");
            return 0;
        }
        GLES20.glAttachShader(this.program, shader);
        return shader;
    }

    private void logError(String message) {
        Log.e("OpenGLProgram", message);
        Log.e("OpenGLProgram", "OpenGL Error Code : "+ GLES20.glGetError());
    }
}
